package com.zq.dytool.adbmodel.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zq.dytool.R;
import com.zq.dytool.adbmodel.MainActivity;
import com.zq.dytool.adbmodel.javabean.EventBean;
import com.zq.dytool.adbmodel.javabean.EventListBean;
import com.zq.dytool.adbmodel.tools.AppPackageManager;
import com.zq.dytool.adbmodel.tools.NormalTool;
import com.zq.dytool.adbmodel.tools.SimulateTool;
import com.zq.dytool.adbmodel.wss.WssServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author 张迁-zhangqian
 * @Data 2020/3/6 4:34 PM
 * @Package com.zq.dytool.adbmodel.service
 **/
public class DYForegroundService extends Service {
    private static final String TAG = "DYForegroundService";
    private MessageBinder messageBinder = new MessageBinder();
    private WssServer wssServer;
    private boolean isrun = false;
    //创建基本线程池
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(50));
    private List<Runnable> runnables = new ArrayList<>();
    private boolean isShutDown = false;

    public class MessageBinder extends Binder {
        public boolean handleGEvent(final Context mContext, String json) {
            final EventListBean eventListBean = new Gson().fromJson(json,
                    new TypeToken<EventListBean>() {
                    }.getType());

            if (eventListBean.isShut_down()) {
                isShutDown = true;
                return true;
            }

            Runnable command = new Runnable() {
                @Override
                public void run() {
                    if (eventListBean.isRefresh()) {
                        for (int i = 0; i < eventListBean.getRefresh_times(); i++) {
                            if (isShutDown) return;
                            handleEvent(mContext, eventListBean);
                        }
                    } else {
                        if (isShutDown) return;
                        handleEvent(mContext, eventListBean);
                    }
                }
            };
            isShutDown = false;
            threadPoolExecutor.execute(command);

            return true;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messageBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 在API11之后构建Notification的方式
        Notification.Builder builder = new Notification.Builder
                (this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class);
         builder.setContentIntent(PendingIntent.
                getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.launcher)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("抖音辅助工具") // 设置下拉列表里的标题
                .setSmallIcon(R.drawable.launcher) // 设置状态栏内的小图标
                .setContentText("抖音辅助工具正在运行中") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 处理按键事件
     *
     * @param mContext
     * @param eventListBean
     */
    private void handleEvent(Context mContext, EventListBean eventListBean) {

        // 0: 打开app； 1：延迟时间； 2：点击； 3：滑动； 4：系统事件 5:发送text 6:发送中文 7：发送B64 8:copy；
        for (EventBean eb : eventListBean.getBody()) {
            if (isShutDown) return;
            switch (eb.getType()) {
                case 0://DYWrapper.DY_PACKAGENAME
                    if (AppPackageManager.checkPackInfo(mContext, eb.getApp_package())) {
                        AppPackageManager.openPackage(mContext, eb.getApp_package());
                    } else {
                        Toast.makeText(mContext, String.format("没有安装应用：%s", eb.getApp_package()), Toast.LENGTH_LONG).show();
                    }

                    break;
                case 1:
                    try {
                        Thread.sleep(eb.getDelay_time());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "延迟发生错误", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 2:
                    SimulateTool.exeClick(eb.getClick_potion().getX(), eb.getClick_potion().getY());
                    break;
                case 3:
                    SimulateTool.exeSwipe(eb.getSwipe().getX1(), eb.getSwipe().getY1(), eb.getSwipe().getX2(), eb.getSwipe().getY2(), eb.getSwipe().getTime());
                    break;
                case 4:
                    SimulateTool.exeEvent(eb.getKey_event_code());
                    break;
                case 5:
                    SimulateTool.exeText(eb.getText());
                    break;
                case 6:
                    SimulateTool.exezhText(eb.getText());
                    break;
                case 7:
                    SimulateTool.exeB64Text(eb.getText());
                    break;
                case 8:
                    NormalTool.copy(mContext, eb.getUser_url());
                    break;
                case 9:
                    AppPackageManager.setTopApp(mContext);
                    break;
                case 10:

                    break;
                default:
                    Log.e(TAG, "handleEvent: " + eb.toString());
                    break;
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}

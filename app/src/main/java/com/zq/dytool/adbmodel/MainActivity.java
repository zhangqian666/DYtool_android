package com.zq.dytool.adbmodel;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.zq.dytool.R;
import com.zq.dytool.adbmodel.service.DYForegroundService;
import com.zq.dytool.adbmodel.tools.NormalTool;
import com.zq.dytool.adbmodel.tools.SimulateTool;
import com.zq.dytool.adbmodel.wss.WssServer;

import java.util.Calendar;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private AppCompatButton btnTest;
    private TextView tvServiceStatus;
    private TextView tvRootStatus;
    private TextView tvDevInfo;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messageBinder = (DYForegroundService.MessageBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(TAG, "onServiceDisconnected: ");
        }
    };
    private DYForegroundService.MessageBinder messageBinder;
    private WssServer wssServer;
    private String macFromHardware;
    private boolean isConnected = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        MApplication.base_x = NormalTool.getX(this);
        MApplication.base_y = NormalTool.getY(this);
        Log.e(TAG, "windows: " + MApplication.base_x + " : " + MApplication.base_y);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnTest = findViewById(R.id.btn_test_point);
        tvServiceStatus = findViewById(R.id.tv_service_status);
        tvRootStatus = findViewById(R.id.tv_root_status);
        tvDevInfo = findViewById(R.id.tv_dev_info);

        //判断是否root
        if (SimulateTool.isRoot(mContext)) {
            MApplication.isRoot = true;
            tvRootStatus.setText("设备已经Root，请正常操作");
        } else {
            MApplication.isRoot = false;
            tvRootStatus.setText("未root，需要root设备");
        }

        //启动前台服务，防止服务被关闭
        Intent intent = new Intent(mContext, DYForegroundService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);

        //socket长链接
        wssServer = new WssServer();
        wssServer.setOnMsgListener(new WssServer.OnMsgListener() {

            @Override
            public void onMessage(String str) {
                Log.e(TAG, "onMessage: " + str);
                Message msg = new Message();
                msg.obj = str;
                handler.sendMessage(msg);
                if (str.startsWith("{")) {
                    if (MApplication.isRoot) {
                        if (!messageBinder.handleGEvent(mContext, str)) {
                            Log.e(TAG, String.format("onMessage: %s", "上个任务还在运行"));
                        }
                    } else {
                        Log.e(TAG, String.format("onMessage: %s", "设备没有root"));
                    }
                }
            }

            @Override
            public void onConnect(boolean isConnect) {
                isConnected = isConnect;
                if (isConnect) {
                    tvServiceStatus.setText("已经连接上服务器");
                } else {
                    tvServiceStatus.setText("未连接服务器");
                }
            }
        });

        //获取mac地址
        macFromHardware = NormalTool.getMacFromHardware(mContext);
        if (macFromHardware == null || macFromHardware.isEmpty()) {
            macFromHardware = "default:" + Calendar.getInstance().getTime().getTime();
        }
        String model = "/" + Build.MODEL + "-" + Build.VERSION.SDK_INT;

        tvDevInfo.setText(String.format("MAC:%s\nmodel:%s\nSDK:%s\nrelease:%s", macFromHardware, Build.MODEL, Build.VERSION.SDK_INT, Build.VERSION.RELEASE));
        wssServer.init(DYWrapper.wssUrl + macFromHardware + model.replace(" ", ""));

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, TestActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnected) {
            tvServiceStatus.setText("已经连接上服务器");
        } else {
            tvServiceStatus.setText("未连接服务器");
        }
    }

    @Override
    public void onBackPressed() {

    }
}

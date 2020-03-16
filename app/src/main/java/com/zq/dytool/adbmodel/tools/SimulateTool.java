package com.zq.dytool.adbmodel.tools;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author 张迁-zhangqian
 * @Data 2020/3/3 1:33 PM
 * @Package com.zq.dytool
 **/
public class SimulateTool {
    private static final String TAG = "SimulateTool";
    static int keyevent_home = 3;// Home
    public int keyevent_back = 4;// Back
    public int keyevent_up = 19;//Up
    public int keyevent_down = 20;//Down
    public int keyevent_left = 21;//Left
    public int keyevent_right = 22;//Right
    public int keyevent_ok = 23;//Select/Ok
    public int keyevent_vo_add = 24;//Volume+
    public int keyevent_vo_down = 25;// Volume-
    public int keyevent_menu = 82;// Menu 菜单

    /**
     * execShellCmd("getevent -p");  
     * execShellCmd("sendevent /dev/input/event0 1 158 1");  
     * execShellCmd("sendevent /dev/input/event0 1 158 0");  
     * execShellCmd("input keyevent 3");//home  
     * execShellCmd("input text  'helloworld!' ");  
     * execShellCmd("input tap 168 252");  
     * execShellCmd("input swipe 100 250 200 280"); 
     */
    public static boolean isRoot(Context context) {
        boolean isRooted = false;
        try {
            Process process = Runtime.getRuntime().exec("su");
            return isRooted = true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "需要获取root权限", Toast.LENGTH_LONG).show();
            return isRooted = false;
        }
    }

    public static void exeClick(double x, double y) {
        x = NormalTool.parseX(x);
        y = NormalTool.parseY(y);
        execShellCmd(String.format("input tap %f %f", x, y));
    }

    public static void exeSwipe(double x1, double y1, double x2, double y2, long time) {
        Log.e(TAG, "start exeSwipe: " + x1 + "--" + y1 + "--" + x2 + "--" + y2);
        x1 = NormalTool.parseX(x1);
        y1 = NormalTool.parseY(y1);
        x2 = NormalTool.parseX(x2);
        y2 = NormalTool.parseY(y2);
        Log.e(TAG, "end exeSwipe: " + x1 + "--" + y1 + "--" + x2 + "--" + y2);
        execShellCmd(String.format("input swipe %f %f %f %f %d", x1, y1, x2, y2, time));
    }

    public static void exeText(String text) {
        execShellCmd(String.format("input text " + text));
//        exeSwipe(96, 1275, 99, 1275, 500);
//        exeClick(80, 1000);
    }

    public static void exezhText(String text) {
        //am broadcast -a ADB_INPUT_B64 --es msg `echo '你好嗎? Hello?' | base64`
        //am broadcast -a ADB_INPUT_B64 --es msg `echo '你好嗎? Hello?' | base64`
        //am broadcast -a ADB_INPUT_TEXT --es msg
        execShellCmd(String.format(String.format("am broadcast -a ADB_INPUT_TEXT --es msg %s", text)));
    }

    public static void exeB64Text(String text) {
        //am broadcast -a ADB_INPUT_B64 --es msg `echo '你好嗎? Hello?' | base64`
        //am broadcast -a ADB_INPUT_B64 --es msg `echo '你好嗎? Hello?' | base64`
        //am broadcast -a ADB_INPUT_TEXT --es msg
        execShellCmd(String.format(String.format("am broadcast -a ADB_INPUT_B64 --es msg `echo '%s' | base64`", text)));
    }

    public static void exeEvent(int event) {
        execShellCmd(String.format("input keyevent %d", event));
    }

    private static void simulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }


    private static void execShellCmd(String cmd) {
        try {
            Log.e(TAG, String.format("execShellCmd: %s", cmd));

            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.write(cmd.getBytes());
            dataOutputStream.writeBytes("\n");
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}

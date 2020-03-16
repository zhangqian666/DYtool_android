package com.zq.dytool.adbmodel.tools;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.zq.dytool.adbmodel.MApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @Author 张迁-zhangqian
 * @Data 2020/3/3 5:48 PM
 * @Package com.zq.dytool
 **/
public class NormalTool {
    /**
     * 复制内容到剪切板
     *
     * @param copyStr
     * @return
     */
    public static boolean copy(Context context, String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int getX(Context context) {
        //1、通过WindowManager获取
        WindowManager manager = ((AppCompatActivity) context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        return width;
    }

    public static int getY(Context context) {
        //1、通过WindowManager获取
        WindowManager manager = ((AppCompatActivity) context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int height = outMetrics.heightPixels;
        return height;
    }

    public static double parseX(double x) {
        return x / 1080.0 * MApplication.base_x;
    }

    public static double parseY(double y) {
        return y / 1920.0 * MApplication.base_y;
    }

    public static double testParseX(double x) {
        return x / MApplication.base_x * 1080.0;
    }

    public static double testParseY(double y) {
        return y / MApplication.base_y * 1920.0;
    }

    public static String change(String str) {
        try {
            String strGBK = URLEncoder.encode(str, "UTF-8");
            System.out.println(strGBK);
            String strUTF8 = URLDecoder.decode(str, "GBK");
            return strUTF8;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "123";
    }

    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @param context
     * @return
     */
    private static String getMacDefault(Context context) {
        String mac = null;
        if (context == null) {
            return mac;
        }

        WifiManager wifi = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return mac;
        }
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {

        }
        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     *
     * @return
     */
    private static String getMacAddress() {
        String WifiAddress = null;
        try {
            WifiAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WifiAddress;
    }

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return
     */
    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            Log.d("Utils", "all:" + all.size());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }
                Log.d("Utils", "macBytes:" + macBytes.length + "," + nif.getName());

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMacFromHardware(Context context) {

        String macAddress = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//5.0以下
            macAddress = getMacDefault(context);
            if (macAddress != null) {
                Log.d("Utils", "android 5.0以前的方式获取mac" + macAddress);
                macAddress = macAddress.replaceAll(":", "");
                if (macAddress.equalsIgnoreCase("020000000000") == false) {
                    return macAddress;
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            macAddress = getMacAddress();
            if (macAddress != null) {
                Log.d("Utils", "android 6~7 的方式获取的mac" + macAddress);
                macAddress = macAddress.replaceAll(":", "");
                if (macAddress.equalsIgnoreCase("020000000000") == false) {
                    return macAddress;
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            macAddress = getMacFromHardware();
            if (macAddress != null) {
                Log.d("Utils", "android 7以后 的方式获取的mac" + macAddress);
                macAddress = macAddress.replaceAll(":", "");
                if (macAddress.equalsIgnoreCase("020000000000") == false) {
                    return macAddress;
                }
            }
        }

        Log.d("Utils", "没有获取到MAC");
        return null;
    }


}

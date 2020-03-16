package com.zq.dytool.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 张迁-zhangqian
 * @Data 2020/3/4 12:39 AM
 * @Package com.zq.dytool
 **/
public class WechatUtils {
    public static String CONTENT;

    public static String NAME;

    private static final String TAG = "autoSendMsg";

    public static void findDialogAndClick(AccessibilityService paramAccessibilityService, String paramString1, String paramString2) {
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
            return;
        List<AccessibilityNodeInfo> list1 = accessibilityNodeInfo.findAccessibilityNodeInfosByText(paramString1);
        List<AccessibilityNodeInfo> list2 = accessibilityNodeInfo.findAccessibilityNodeInfosByText(paramString2);
        if (!list1.isEmpty() && !list2.isEmpty())
            for (AccessibilityNodeInfo accessibilityNodeInfo1 : list1) {
                if (accessibilityNodeInfo1 != null && paramString1.equals(accessibilityNodeInfo1.getText())) {
                    performClick(accessibilityNodeInfo1);
                    return;
                }
            }
    }

    public static void findTextAndClick(AccessibilityService paramAccessibilityService, String paramString) {
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
            return;
        List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByText(paramString);
        if (list != null && !list.isEmpty()) {
            for (AccessibilityNodeInfo accessibilityNodeInfo1 : list) {
                if (accessibilityNodeInfo1 != null && (paramString.equals(accessibilityNodeInfo1.getText()) || paramString.equals(accessibilityNodeInfo1.getContentDescription()))) {
                    performClick(accessibilityNodeInfo1);
                    return;
                }
            }
        }
    }

    public static String findTextById(AccessibilityService paramAccessibilityService, String paramString) {
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo != null) {
            List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(paramString);
            if (list != null && list.size() > 0)
                return ((AccessibilityNodeInfo) list.get(0)).getText().toString();
        }
        return null;
    }

    /**
     * com.ss.android.ugc.aweme:id/as
     *
     * @param paramAccessibilityService
     * @param paramString
     * @param index
     * @return
     */
    public static void findListIndexById(AccessibilityService paramAccessibilityService, String paramString, int index) {
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo != null) {
            List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(paramString);
            if (list != null && list.size() > 0) {
                AccessibilityNodeInfo child = list.get(0).getChild(0).getChild(0);
                performClick(child);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean findViewByIdAndPasteContent(AccessibilityService paramAccessibilityService, String paramString1, String paramString2) {
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo != null) {
            List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(paramString1);
            if (list != null && !list.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putCharSequence("ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE", paramString2);
                ((AccessibilityNodeInfo) list.get(0)).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean findViewByTextAndPasteContent(AccessibilityService paramAccessibilityService, String paramString1, String paramString2) {
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo != null) {
            List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByText(paramString1);
            if (list != null && !list.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putCharSequence("ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE", paramString2);
                ((AccessibilityNodeInfo) list.get(0)).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
                return true;
            }
            return false;
        }
        return false;
    }

    public static void findViewIdAndClick(AccessibilityService paramAccessibilityService, String paramString) {
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
            return;
        List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(paramString);
        if (list != null && !list.isEmpty())
            for (AccessibilityNodeInfo accessibilityNodeInfo1 : list) {
                if (accessibilityNodeInfo1 != null) {
                    performClick(accessibilityNodeInfo1);
                    return;
                }
            }
    }

    public static List<AccessibilityNodeInfo> findViewListById(AccessibilityService paramAccessibilityService, String paramString) {
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        return (accessibilityNodeInfo == null) ? arrayList : accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(paramString);
    }

    public static void performBack(AccessibilityService paramAccessibilityService) {
        if (paramAccessibilityService == null)
            return;
        if (Build.VERSION.SDK_INT >= 16) {
            try {
                Thread.sleep(200L);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            paramAccessibilityService.performGlobalAction(1);
        }
    }

    public static void performClick(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        if (paramAccessibilityNodeInfo == null)
            return;
        if (paramAccessibilityNodeInfo.isClickable()) {
            paramAccessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.e(TAG, "performClick: " + AccessibilityNodeInfo.ACTION_CLICK);
            return;
        }
        performClick(paramAccessibilityNodeInfo.getParent());
    }

    public static void getViewId(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null)return;
        if (accessibilityNodeInfo.getChildCount() > 0) {

            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {

                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                Log.e(TAG, "c: " + child.getClassName()
                        + ";i:" + child.getViewIdResourceName()
                        + ";t: " + child.getText()
                        + ";c:" + child.getChildCount()
                        + ";\n");
                if (child.getChildCount() > 0) {
                    getViewId(child);
                }
            }

        }
    }

    public static void getViewId(AccessibilityWindowInfo accessibilityWindowInfo, StringBuffer mainStr) {

        if (accessibilityWindowInfo.getChildCount() > 0) {
            mainStr.append(" { list : {");
            for (int i = 0; i < accessibilityWindowInfo.getChildCount(); i++) {
                AccessibilityWindowInfo child = accessibilityWindowInfo.getChild(i);
                mainStr.append("c: " + child.getClass()
                        + ";i:" + child.getId()
                        + ";t: " + child.getTitle()
                        + ";c:" + child.getChildCount()
                        + ";\n");
                if (child.getChildCount() > 0) {
                    getViewId(child, mainStr);
                }
            }
            mainStr.append(" }} ");
        }
    }
}

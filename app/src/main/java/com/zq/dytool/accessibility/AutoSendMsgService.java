package com.zq.dytool.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 张迁-zhangqian
 * @Data 2020/3/3 11:32 PM
 * @Package com.zq.dytool
 **/
public class AutoSendMsgService extends AccessibilityService {

    public static final int SEND_FAIL = 0;

    public static int SEND_STATUS = 0;

    public static final int SEND_SUCCESS = 1;

    private static final String TAG = "AutoSendMsgService";

    public static boolean hasSend;

    public static int isOpenGoodsDetailWin;

    public static int isOpenGoodsListWin;

    public static int isOpenShareWin = 0;

    private List<String> allNameList = new ArrayList<String>();

    private int mRepeatCount;

    static {
        isOpenGoodsListWin = 0;
        isOpenGoodsDetailWin = 0;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e(TAG, "onServiceConnected: ");
    }

    private AccessibilityNodeInfo TraversalAndFindContacts() {
        List<String> list = this.allNameList;
        if (list != null)
            list.clear();
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list1 = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ih");
        boolean bool = false;
        if (list1 != null && !list1.isEmpty())
            while (true) {
                List<AccessibilityNodeInfo> list2 = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j1");
                List<AccessibilityNodeInfo> list3 = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/iy");
                boolean bool1 = bool;
                if (list2 != null) {
                    bool1 = bool;
                    if (!list2.isEmpty()) {
                        int i = 0;
                        while (true) {
                            bool1 = bool;
                            if (i < list2.size()) {
                                if (i == 0)
                                    this.mRepeatCount = 0;
                                AccessibilityNodeInfo accessibilityNodeInfo1 = list3.get(i);
                                String str = ((AccessibilityNodeInfo) list2.get(i)).getText().toString();
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("nickname = ");
                                stringBuilder.append(str);
                                Log.d("autoSendMsg", stringBuilder.toString());
                                if (str.equals(WechatUtils.NAME))
                                    return accessibilityNodeInfo1;
                                if (!this.allNameList.contains(str)) {
                                    this.allNameList.add(str);
                                    bool1 = bool;
                                } else {
                                    bool1 = bool;
                                    if (this.allNameList.contains(str)) {
                                        StringBuilder stringBuilder1 = new StringBuilder();
                                        stringBuilder1.append("mRepeatCount = ");
                                        stringBuilder1.append(this.mRepeatCount);
                                        Log.d("autoSendMsg", stringBuilder1.toString());
                                        bool1 = bool;
                                        if (this.mRepeatCount == 3) {
                                            if (bool) {
                                                Log.d("autoSendMsg", "");
                                                hasSend = true;
                                                return null;
                                            }
                                            bool1 = true;
                                        }
                                        this.mRepeatCount++;
                                    }
                                }
                                i++;
                                bool = bool1;
                                continue;
                            }
                            break;
                        }
                    }
                }
                if (!bool1) {
                    ((AccessibilityNodeInfo) list1.get(0)).performAction(4096);
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    bool = bool1;
                    continue;
                }
                return null;
            }
        return null;
    }

    private boolean copy(String paramString) {
        try {
            ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Label", paramString));
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private void handleBuyGoods() {
        List<AccessibilityNodeInfo> list = WechatUtils.findViewListById(this, "com.ss.android.ugc.aweme:id/fbu");
        if (list.size() > 0)
            WechatUtils.performClick(list.get(0));
    }

    private void handleFlow_ChatUI() {
        String str = WechatUtils.findTextById(this, "com.tencent.mm:id/ha");
        if (!TextUtils.isEmpty(str) && str.equals(WechatUtils.NAME)) {
            if (WechatUtils.findViewByIdAndPasteContent(this, "com.tencent.mm:id/a_z", WechatUtils.CONTENT)) {
                sendContent();
                return;
            }
            WechatUtils.findViewIdAndClick(this, "com.tencent.mm:id/a_x");
            try {
                Thread.sleep(100L);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            if (WechatUtils.findViewByIdAndPasteContent(this, "com.tencent.mm:id/a_z", WechatUtils.CONTENT)) {
                sendContent();
                return;
            }
        } else {
            WechatUtils.findViewIdAndClick(this, "com.tencent.mm:id/h9");
        }
    }

    private void handleFlow_ContactInfoUI() {
        WechatUtils.findTextAndClick(this, "");
    }

    private void handleFlow_LaunchUI() {
        try {
            WechatUtils.findTextAndClick(this, "");
            Thread.sleep(50L);
            WechatUtils.findTextAndClick(this, "");
            Thread.sleep(200L);
            AccessibilityNodeInfo accessibilityNodeInfo = TraversalAndFindContacts();
            if (accessibilityNodeInfo != null) {
                WechatUtils.performClick(accessibilityNodeInfo);
            } else {
                SEND_STATUS = 0;
                resetAndReturnApp();
            }
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    private void handleOpenCart() {
        Log.d("autoSendMsg", "handleOpenCart");
        List<AccessibilityNodeInfo> list = WechatUtils.findViewListById(this, "com.ss.android.ugc.aweme:id/au");
        if (list != null && !list.isEmpty())
            for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
                if (accessibilityNodeInfo.getChildCount() > 0) {
                    accessibilityNodeInfo = accessibilityNodeInfo.getChild(0);
                    if (accessibilityNodeInfo != null && accessibilityNodeInfo.getChildCount() > 0) {
                        WechatUtils.performClick(accessibilityNodeInfo.getChild(0));
                        return;
                    }
                }
            }
    }

    private void openShareLink() {
        Log.e(TAG, "openShareLink: ");
        AccessibilityNodeInfo accessibilityNodeInforoot = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = accessibilityNodeInforoot.findAccessibilityNodeInfosByText("打开看看");
//        List<AccessibilityNodeInfo> list = WechatUtils.findTextAndClick(this, "com.ss.android.ugc.aweme:id/f84");
        if (list != null && !list.isEmpty())
            for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
                if (accessibilityNodeInfo != null) {
                    Log.e(TAG, "openShareLink: " + accessibilityNodeInfo.toString());
                    WechatUtils.performClick(accessibilityNodeInfo);
                    return;
                }
            }
    }

    private void resetAndReturnApp() {
        hasSend = true;
        for (ActivityManager.RunningTaskInfo runningTaskInfo : ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getRunningTasks(3)) {
            if (getPackageName().equals(runningTaskInfo.topActivity.getPackageName()))
                return;
        }
    }

    private void sendContent() {
        WechatUtils.findTextAndClick(this, "发送");
        SEND_STATUS = 1;
        resetAndReturnApp();
    }

    private void sleep(int paramInt) {
        long l = paramInt;
        try {
            Thread.sleep(l);
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    /**
     * 说点什么...
     */
    private void handleMsg() {
        WechatUtils.findTextAndClick(this, "说点什么...");
        sleep(2000);
        WechatUtils.findViewByTextAndPasteContent(this, "说点什么", WechatUtils.CONTENT);
        sendContent();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
        String str = paramAccessibilityEvent.getClassName().toString();
        Log.e(TAG, "onAccessibilityEvent: " + str + "  action : "
                + paramAccessibilityEvent.getAction());

        if (paramAccessibilityEvent.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)//32
            return;
        Log.d("autoSendMsg", str);
        if (hasSend)
            return;
        if (" ".equalsIgnoreCase(str))
            Log.d("autoSendMsg", "into Main");
        if ("com.ss.android.ugc.aweme.live.LivePlayActivity".equalsIgnoreCase(str)) {
            Log.d("autoSendMsg", "into Live");
//            if (isOpenGoodsListWin == 0) {
//                sleep(2000);
//                handleOpenCart();
//                isOpenGoodsListWin = 1;
//            }
//            handleMsg();
            sleep(2000);
            while (true) {
                sleep(10000);
                List<AccessibilityWindowInfo> accessibilityNodeInfolist = this.getWindows();
                if (accessibilityNodeInfolist == null)
                    return;
//                StringBuffer stringBuffer = new StringBuffer();
                for (AccessibilityWindowInfo ani : accessibilityNodeInfolist) {

                    WechatUtils.getViewId(ani.getRoot());
//                    Log.e(TAG, "onAccessibilityEvent: " + stringBuffer);
                }


//                 WechatUtils.findViewIdAndClick(this, "com.ss.android.ugc.aweme:id/fub"); // 小时榜
//                 WechatUtils.findViewIdAndClick(this, "com.ss.android.ugc.aweme:id/c_c"); // 小时榜
//                 WechatUtils.findViewIdAndClick(this, "com.ss.android.ugc.aweme:id/a31"); // 小时榜
//                 WechatUtils.findViewIdAndClick(this, "com.ss.android.ugc.aweme:id/c_b"); // 小时榜
                WechatUtils.findListIndexById(this, "com.ss.android.ugc.aweme:id/as", 0);
                sleep(2000);
                WechatUtils.findViewByTextAndPasteContent(this,
                        "com.ss.android.ugc.aweme:id/ao_"
                        , WechatUtils.CONTENT);
                sendContent();

            }
        }
        if ("com.bytedance.android.livesdk.livecommerce.a.b".equalsIgnoreCase(str) && isOpenGoodsDetailWin == 0) {
            sleep(2000);
            handleBuyGoods();
            isOpenGoodsDetailWin = 1;
        }
        if ("com.ss.android.ugc.aweme.feed.share.b".equalsIgnoreCase(str) && isOpenShareWin == 0) {
            sleep(2000);
            openShareLink();
            isOpenShareWin = 1;
        }
    }


    @Override
    public void onInterrupt() {

    }
}

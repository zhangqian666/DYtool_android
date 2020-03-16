package com.zq.dytool.accessibility;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.zq.dytool.R;

public class DYHandleActivity extends AppCompatActivity {
    private AccessibilityManager accessibilityManager;
    private String content;
    private AppCompatEditText sendContent;
    private AppCompatTextView sendStatus;

    private AppCompatButton start;

    Handler statusHandler = new Handler() {
        public void handleMessage(Message param1Message) {
            super.handleMessage(param1Message);
            setSendStatusText(param1Message.what);
        }
    };

    private void checkAndStartService() {
        this.accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        this.content = this.sendContent.getText().toString();
        if (TextUtils.isEmpty(this.content)) {
            Toast.makeText((Context) this, "发送内容不能空", Toast.LENGTH_LONG).show();
            return;
        }

        if (!this.accessibilityManager.isEnabled()) {
            openService();
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                statusHandler.sendEmptyMessage(goDY());
            }
        }).start();
    }

    private boolean copy(String paramString) {
        try {
            ClipboardManager cliboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData label = ClipData.newPlainText("Label", paramString);
            assert cliboardManager != null;
            cliboardManager.setPrimaryClip(label);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private int goDY() {
        try {
            AutoSendMsgService.isOpenShareWin = 0;
            AutoSendMsgService.isOpenGoodsListWin = 0;
            AutoSendMsgService.isOpenGoodsDetailWin = 0;
            setValue("1", this.content);
            AutoSendMsgService.hasSend = false;
            copy("#在抖音，记录美好生活#【可爱的球球】正在直播，来和我一起支持TA吧。点击下方链接，直接观看直播！ https://v.douyin.com/tgn94J/");
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName("com.ss.android.ugc.aweme", "com.ss.android.ugc.aweme.main.MainActivity");
            startActivity(intent);
            while (true) {
                if (AutoSendMsgService.hasSend)
                    return AutoSendMsgService.SEND_STATUS;
                try {
                    Thread.sleep(500L);
                } catch (Exception exception) {
                    openService();
                    exception.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return AutoSendMsgService.SEND_STATUS;
        }
    }

    private void init() {
        this.start = findViewById(R.id.btn_send);
        this.sendContent = findViewById(R.id.text_context);
        this.sendStatus = findViewById(R.id.text_status);
        this.start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                checkAndStartService();
            }
        });
    }

    private void openService() {
        try {
            startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
            Toast.makeText((Context) this, "打开服务", Toast.LENGTH_LONG).show();
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    private void setSendStatusText(int paramInt) {
        if (paramInt == 1) {
            this.sendStatus.setText("信息发送成功");
            return;
        }
        this.sendStatus.setText("信息发送失败");
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_d_y_handle);
        init();
    }

    public void setValue(String paramString1, String paramString2) {
        WechatUtils.NAME = paramString1;
        WechatUtils.CONTENT = paramString2;
        AutoSendMsgService.hasSend = false;
    }
}

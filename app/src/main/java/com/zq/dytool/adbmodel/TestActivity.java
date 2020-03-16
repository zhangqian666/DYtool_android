package com.zq.dytool.adbmodel;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.zq.dytool.R;
import com.zq.dytool.adbmodel.tools.NormalTool;

public class TestActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private TextView tv_down;
    private TextView tv_move;
    private TextView tv_up;

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tv_down = findViewById(R.id.tv_down);
        tv_move = findViewById(R.id.tv_move);
        tv_up = findViewById(R.id.tv_up);
        tv_down.setText("(按下ACTION_DOWN): 0,0");
//        tv_move.setText(String.format("(当前ACTION_MOVE): 0,0"));
//        tv_up.setText(String.format("(抬起ACTION_UP): 0,0"));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                tv_down.setText(String.format("(按下ACTION_DOWN): %f,%f", NormalTool.testParseX(event.getX()), NormalTool.testParseY(event.getY())));
                break;
//            case MotionEvent.ACTION_MOVE:
//                tv_move.setText(String.format("(当前ACTION_MOVE): %f,%f", event.getX(), event.getY()));
//                break;
//            case MotionEvent.ACTION_UP:
//                tv_up.setText(String.format("(抬起ACTION_UP): %f,%f", event.getX(), event.getY()));
//                break;
            default:
                break;
        }

        return super.onTouchEvent(event);

    }
}

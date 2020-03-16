package com.zq.dytool.adbmodel;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @Author 张迁-zhangqian
 * @Data 2020/3/3 1:22 PM
 * @Package com.zq.dytool
 **/
public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mContext = this;
//        ButterKnife.bind(this);
        initView(savedInstanceState);
    }

    public abstract int getContentView();

    public abstract void initView(Bundle savedInstanceState);
}

package com.haibin.calendarviewproject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;

import com.haibin.calendarviewproject.base.activity.BaseActivity;

/**
 * 测试界面
 * Created by huanghaibin on 2018/8/7.
 */

public class TestActivity extends BaseActivity {

    private FrameLayout mLayout;

    public static void show(Context context) {
        context.startActivity(new Intent(context, TestActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mLayout = (FrameLayout) findViewById(R.id.customLayout);
    }

    @Override
    protected void initData() {
        mLayout.post(new Runnable() {
            @Override
            public void run() {
                View view = findViewById(R.id.emptyView);
                view.requestLayout();

            }
        });
    }
}

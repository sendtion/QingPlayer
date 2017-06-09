package com.sendtion.qingplayer.ui;

import android.content.Intent;
import android.os.Bundle;

import com.badoo.mobile.util.WeakHandler;
import com.umeng.analytics.MobclickAgent;


public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private WeakHandler mWeakHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);

        mWeakHandler = new WeakHandler();
        mWeakHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWeakHandler.removeCallbacksAndMessages(null);
    }
}

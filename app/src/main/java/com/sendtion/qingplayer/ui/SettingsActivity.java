package com.sendtion.qingplayer.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.sendtion.qingplayer.R;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

/**
 * 设置界面
 */
public class SettingsActivity extends BaseActivity {
    private static final String TAG = "SettingsActivity";

    private RelativeLayout layout_setting_clear_cache;//清除缓存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout_setting_clear_cache = (RelativeLayout) findViewById(R.id.layout_setting_clear_cache);
        layout_setting_clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清理缓存
                GSYVideoManager.clearAllDefaultCache(SettingsActivity.this);
                showToast("缓存清理完毕");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

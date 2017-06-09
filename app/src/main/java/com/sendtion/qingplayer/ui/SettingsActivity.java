package com.sendtion.qingplayer.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sendtion.qingplayer.R;
import com.sendtion.qingplayer.util.AppUtils;
import com.sendtion.qingplayer.util.CommonUtil;
import com.sendtion.qingplayer.util.ConstantUtils;
import com.sendtion.qingplayer.util.MarketUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * 设置界面
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SettingsActivity";
    private Context mContext;
    private SharedPreferences sp;

    private RelativeLayout layout_setting_play_mode;//播放模式
    private RelativeLayout layout_setting_media_codec;//开启硬解码
    private RelativeLayout layout_setting_clear_cache;//清除缓存
    private RelativeLayout layout_setting_donate;//打赏作者
    private RelativeLayout layout_setting_rating;//评分
    private RelativeLayout layout_setting_check_update;//检查更新
    private RelativeLayout layout_setting_app_info;//应用信息
    private RelativeLayout layout_setting_about;//关于

    private SwitchCompat switch_media_codec;//开启硬解码
    private TextView tv_play_mode;//播放模式
    private TextView tv_version_name;//版本号

    private String playMode;//播放模式
    private boolean isEnableCodec;//是否开启硬解码
    private String[] playModes = new String[]{ConstantUtils.PLAY_ORDER, ConstantUtils.PLAY_LOOP,
            ConstantUtils.PLAY_STOP};

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

        mContext = this;
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        isEnableCodec = sp.getBoolean("isEnableCodec", false);
        playMode = sp.getString("playMode", ConstantUtils.PLAY_ORDER);

        layout_setting_play_mode = (RelativeLayout) findViewById(R.id.layout_setting_play_mode);
        layout_setting_media_codec = (RelativeLayout) findViewById(R.id.layout_setting_media_codec);
        layout_setting_clear_cache = (RelativeLayout) findViewById(R.id.layout_setting_clear_cache);
        layout_setting_donate = (RelativeLayout) findViewById(R.id.layout_setting_donate);
        layout_setting_rating = (RelativeLayout) findViewById(R.id.layout_setting_rating);
        layout_setting_check_update = (RelativeLayout) findViewById(R.id.layout_setting_check_update);
        layout_setting_app_info = (RelativeLayout) findViewById(R.id.layout_setting_app_info);
        layout_setting_about = (RelativeLayout) findViewById(R.id.layout_setting_about);

        tv_play_mode = (TextView) findViewById(R.id.tv_play_mode);
        tv_play_mode.setText(playMode);

        switch_media_codec = (SwitchCompat) findViewById(R.id.switch_media_codec);
        switch_media_codec.setChecked(isEnableCodec);

        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tv_version_name.setText(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        layout_setting_play_mode.setOnClickListener(this);
        layout_setting_media_codec.setOnClickListener(this);
        layout_setting_clear_cache.setOnClickListener(this);
        layout_setting_donate.setOnClickListener(this);
        layout_setting_rating.setOnClickListener(this);
        layout_setting_check_update.setOnClickListener(this);
        layout_setting_app_info.setOnClickListener(this);
        layout_setting_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_setting_play_mode:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(playModes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playMode = playModes[which];
                        sp.edit().putString("playMode", playMode).commit();
                        tv_play_mode.setText(playMode);
                    }
                });
                builder.create().show();
                break;
            case R.id.layout_setting_media_codec:
                isEnableCodec = !isEnableCodec;
                sp.edit().putBoolean("isEnableCodec", isEnableCodec).commit();
                switch_media_codec.setChecked(isEnableCodec);
                break;
            case R.id.layout_setting_clear_cache:
                //清理缓存
                GSYVideoManager.clearAllDefaultCache(mContext);
                showToast("缓存清理完毕");
                break;
            case R.id.layout_setting_check_update:
                BmobUpdateAgent.forceUpdate(mContext);
                break;
            case R.id.layout_setting_donate:
                CommonUtil.selecteDonateWay(mContext);//扫描二维码支付
                break;
            case R.id.layout_setting_rating:
                try {
                    String str = "market://details?id=" + getPackageName();
                    Intent localIntent = new Intent("android.intent.action.VIEW");
                    localIntent.setData(Uri.parse(str));
                    startActivity(localIntent);
                } catch (ActivityNotFoundException e) {
                    List<String> pkgs = MarketUtils.queryInstalledMarketPkgs(mContext);
                    if (pkgs != null && pkgs.size() > 0){
                        MarketUtils.launchAppDetail(mContext, getPackageName(), pkgs.get(0));
                    } else {
                        showToast("抱歉，你没有安装应用市场");
                        Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.sendtion.qingplayer#opened");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.layout_setting_app_info://应用信息
                AppUtils.toAppDetailSetting(mContext);
                break;
            case R.id.layout_setting_about://关于
                Intent intent = new Intent();
                intent.setClass(mContext, AboutActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

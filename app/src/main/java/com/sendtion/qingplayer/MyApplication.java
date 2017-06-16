package com.sendtion.qingplayer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

import com.sendtion.qingplayer.util.ConstantKey;

import java.io.File;

/**
 * Created by ShengDecheng on 2017/6/8.
 * Blog: http://sendtion.cn
 * GitHub: https://github.com/sendtion
 * 描述：
 */

public class MyApplication extends Application {
    public static final String TAG = "MyApplication";

    private SharedPreferences mSharedPreferences;

    public static String videoShotDirPath;
    public static String videoCacheDirPath;

    @Override
    public void onCreate() {
        super.onCreate();

        mSharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        // 设置截图保存路径
        videoShotDirPath = mSharedPreferences.getString(ConstantKey.VIDEO_SHOT_DIR, "");
        if (TextUtils.isEmpty(videoShotDirPath)) {
            File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File defaultShotDir = new File(dcim, "/HPlayer/VideoShots/");
            if (!defaultShotDir.exists()) {
                defaultShotDir.mkdirs();
            }
            videoShotDirPath = defaultShotDir.getPath();
            mSharedPreferences.edit().putString(ConstantKey.VIDEO_SHOT_DIR, videoShotDirPath).apply();
        }

        // 设置视频缓存路径
        videoCacheDirPath = mSharedPreferences.getString(ConstantKey.VIDEO_CACHE_DIR, "");
        if (TextUtils.isEmpty(videoCacheDirPath)) {
            File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File defaultCacheDir = new File(dcim, "/HPlayer/VideoCache/");
            if (!defaultCacheDir.exists()) {
                defaultCacheDir.mkdirs();
            }
            videoCacheDirPath = defaultCacheDir.getPath();
            mSharedPreferences.edit().putString(ConstantKey.VIDEO_CACHE_DIR, videoCacheDirPath).apply();
        }
    }
}

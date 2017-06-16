package com.sendtion.qingplayer.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.sendtion.qingplayer.R;
import com.sendtion.qingplayer.util.ConstantUtils;

/**
 * 视频播放界面
 */
public class PlayerActivity extends BaseActivity {
    private static final String TAG = "PlayerActivity";
    private Context mContext;
    private SharedPreferences sp;

//    private NormalGSYVideoPlayer gsyVideoPlayer;
    private String mVideoPath = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    private String mVideoTitle = "";

    private String playMode;//播放模式
    private boolean isEnableCodec;//是否开启硬解码
    private boolean isPlay;
    private boolean isPause;

//    private OrientationUtils orientationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();//隐藏标题栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏状态栏
        setContentView(R.layout.activity_player);

        initViews();
    }

    private void initViews() {
        mContext = this;
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        isEnableCodec = sp.getBoolean("isEnableCodec", false);
        playMode = sp.getString("playMode", ConstantUtils.PLAY_ORDER);

        Intent intent = getIntent();
        mVideoPath = intent.getStringExtra("videoPath");
        mVideoTitle = intent.getStringExtra("videoTitle");

//        gsyVideoPlayer = (NormalGSYVideoPlayer) findViewById(R.id.VideoView);

        //增加封面
        //gsyVideoPlayer.setThumbImageView(holder.imageView);

        //url
        //final String url = "http://baobab.wdjcdn.com/14564977406580.mp4";

        //设置播放url，第一个url，第二个开始缓存，第三个使用默认缓存路径，第四个设置title
//        gsyVideoPlayer.setUp(mVideoPath, false , null, mVideoTitle);
//
//        //非全屏下，不显示title
//        gsyVideoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
//        //非全屏下不显示返回键
//        gsyVideoPlayer.getBackButton().setVisibility(View.VISIBLE);
//
//        //打开非全屏下触摸效果
//        gsyVideoPlayer.setIsTouchWiget(true);
//        //开启自动旋转
//        gsyVideoPlayer.setRotateViewAuto(true);
//        //全屏首先横屏
//        gsyVideoPlayer.setLockLand(false);
//        //是否需要全屏动画效果
//        gsyVideoPlayer.setShowFullAnimation(false);
//        //不需要非wifi状态提示
//        gsyVideoPlayer.setNeedShowWifiTip(false);
//        //需要横屏锁屏播放按键
//        gsyVideoPlayer.setNeedLockFull(true);
//
//        //防止错位
//        //gsyVideoPlayer.setPlayTag(TAG);
//        //gsyVideoPlayer.setPlayPosition(position);
//
//        //循环播放
//        if (playMode.equals(ConstantUtils.PLAY_LOOP)){
//            gsyVideoPlayer.setLooping(true);
//        }
//
//        //如果一个列表的所有视频缓存路劲都一一致，那么配置自定义路径的方法
//        //gsyVideoPlayer.setUp(mVideoPath, true, new File(FileUtils.getTestPath(), ""));
//
//        //开启硬解码-额··反正我自己用不开
//        if (isEnableCodec){
//            GSYVideoType.enableMediaCodec() ;
//        } else {//默认不开启硬解码
//            GSYVideoType.disableMediaCodec();
//        }
//
//        //设置显示比例，默认SCREEN_TYPE_DEFAULT ，自适应
//        //GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9);
//
//        //外部辅助的旋转，帮助全屏
//        orientationUtils = new OrientationUtils(this, gsyVideoPlayer);
//        //初始化不打开外部的旋转
//        orientationUtils.setEnable(false);
//
//        //全屏按钮点击事件
//        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //直接横屏
//                orientationUtils.resolveByClick();
//                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
//                gsyVideoPlayer.startWindowFullscreen(PlayerActivity.this, true, true);
//            }
//        });
//
//        //返回按钮点击事件
//        gsyVideoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        //锁屏按钮点击事件
//        gsyVideoPlayer.setLockClickListener(new LockClickListener() {
//            @Override
//            public void onClick(View view, boolean lock) {
//                if (orientationUtils != null) {
//                    //配合下方的onConfigurationChanged
//                    orientationUtils.setEnable(!lock);
//                }
//            }
//        });
//
//        gsyVideoPlayer.setStandardVideoAllCallBack(sampleListener);
//
//        //立即播放
//        gsyVideoPlayer.startPlayLogic();
    }

//    StandardVideoAllCallBack sampleListener = new StandardVideoAllCallBack() {
//
//        @Override
//        public void onClickStartThumb(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onClickBlank(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onClickBlankFullscreen(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onPrepared(String url, Object... objects) {
//            //开始播放了才能旋转和全屏
//            orientationUtils.setEnable(true);
//            isPlay = true;
//        }
//
//        @Override
//        public void onClickStartIcon(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onClickStartError(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onClickStop(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onClickStopFullscreen(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onClickResume(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onClickResumeFullscreen(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onClickSeekbar(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onClickSeekbarFullscreen(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onAutoComplete(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onEnterFullscreen(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onQuitFullscreen(String url, Object... objects) {
//            if (orientationUtils != null) {
//                orientationUtils.backToProtVideo();
//            }
//        }
//
//        @Override
//        public void onQuitSmallWidget(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onEnterSmallWidget(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onTouchScreenSeekVolume(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onTouchScreenSeekPosition(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onTouchScreenSeekLight(String url, Object... objects) {
//
//        }
//
//        @Override
//        public void onPlayError(String url, Object... objects) {
//
//        }
//    };
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        //如果旋转了就全屏
//        if (isPlay && !isPause) {
//            if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
//                //启动全屏
//                if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
//                    gsyVideoPlayer.startWindowFullscreen(PlayerActivity.this, true, true);
//                }
//            } else {
//                //关闭全屏
//                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
//                if (gsyVideoPlayer.isIfCurrentIsFullscreen()) {
//                    StandardGSYVideoPlayer.backFromWindowFull(this);
//                }
//                if (orientationUtils != null) {
//                    orientationUtils.setEnable(true);
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onPageStart(TAG);
//        MobclickAgent.onResume(this);
//
//        isPause = false;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
//        MobclickAgent.onPause(this);
//
//        isPause = true;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        GSYVideoPlayer.releaseAllVideos();
//        //GSYPreViewManager.instance().releaseMediaPlayer();
//        if (orientationUtils != null)
//            orientationUtils.releaseListener();
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (orientationUtils != null) {
//            orientationUtils.backToProtVideo();
//        }
//        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
//            return;
//        }
//        super.onBackPressed();
//    }
}

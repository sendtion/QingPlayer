package com.sendtion.qingplayer.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sendtion.qingplayer.R;
import com.sendtion.qingplayer.adapter.VideoListAdapter;
import com.sendtion.qingplayer.bean.MediaInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹下视频列表
 */
public class VideoActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "VideoActivity";
    private Context mContext;
    private Toolbar toolbar;//默认标题栏
    private boolean isDelete;//是否长按删除

    private String album;//专辑

    private ListView mVideoListView;
    //private VideoAdapter mVideoAdapter;
    private VideoListAdapter mVideoAdapter;
    private List<MediaInfo> videoInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_video);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDelete){
                    isDelete = false;
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                    invalidateOptionsMenu();
                    setTitle(album);
                } else {
                    finish();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_video);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaInfo mediaInfo = videoInfoList.get(0);
                Intent intent = new Intent(mContext, Player2Activity.class);
//                intent.putExtra("videoPath", mVideoAdapter.getVideoPath(0));
//                intent.putExtra("videoTitle", mVideoAdapter.getVideoTitle(0));
                intent.putExtra("videoPath", mediaInfo.getPath());
                intent.putExtra("videoTitle", mediaInfo.getName());
                intent.putExtra("resolution", mediaInfo.getResolution());
                startActivity(intent);
            }
        });

        mContext = this;
        videoInfoList = new ArrayList<>();

        Intent intent = getIntent();
        album = intent.getStringExtra("album");
        setTitle(album);

        mVideoListView = (ListView) findViewById(R.id.list_video);
        //mVideoAdapter = new VideoAdapter(this);
        mVideoAdapter = new VideoListAdapter();
        mVideoAdapter.setVideoInfoList(videoInfoList);

        mVideoListView.setAdapter(mVideoAdapter);
        mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                MediaInfo mediaInfo = videoInfoList.get(position);
//                String resolution = mediaInfo.getResolution();
//                boolean isLandscape = false;
//                String[] resolut = resolution.split("x");
//                if (Integer.parseInt(resolut[0]) > Integer.parseInt(resolut[1])){
//                    isLandscape = true;
//                }
                Intent intent = new Intent(mContext, Player2Activity.class);
//                intent.putExtra("videoPath", mVideoAdapter.getVideoPath(position));
//                intent.putExtra("videoTitle", mVideoAdapter.getVideoTitle(position));
                intent.putExtra("videoPath", mediaInfo.getPath());
                intent.putExtra("videoTitle", mediaInfo.getName());
                intent.putExtra("landscape", true);
                startActivity(intent);
            }
        });

        mVideoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //弹出对话框
                isDelete = true;
                toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
                invalidateOptionsMenu();//执行它可以调用onPrepareOptionsMenu()方法
                setTitle("1 Item");
                return true;
            }
        });

        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //查询条件：指定文件夹下的视频，不能使用ALBUM查看，因为有的ALBUM为空，导致查询的视频不全
        String selection = MediaStore.Video.Media.BUCKET_DISPLAY_NAME+"=?";
        String[] selectionArgs = {album};
        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        //创建loader
        return new CursorLoader(this, MediaStore.Video.Media.getContentUri("external"), null,
                selection, selectionArgs, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //loader加载完成
        //mVideoAdapter.swapCursor(cursor);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                videoInfoList.add(getMediaInfo(cursor));
                cursor.moveToNext();
            }
        }
        //loader加载完成
        //mVideoAdapter.swapCursor(cursor);
        mVideoAdapter.setVideoInfoList(videoInfoList);
        mVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //在loader销毁时调用
        //mVideoAdapter.swapCursor(null);
    }

    public class VideoAdapter extends SimpleCursorAdapter {

        public VideoAdapter(Context context) {
            //flags:CursorAdapter.FLAG_AUTO_REQUERY和CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            //默认被设置为CursorAdapter.FLAG_AUTO_REQUERY,但是3.0以上被废弃
//            super(context, android.R.layout.simple_list_item_1, null,
//                    new String[]{MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA},
//                    new int[]{android.R.id.text1, android.R.id.text2}, 0);
            super(context, R.layout.list_item_video, null,
                    new String[]{MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.RESOLUTION + " "
                            + MediaStore.Video.Media.SIZE, generateTime(Long.parseLong(MediaStore.Video.Media.DURATION))},
                    new int[]{R.id.tv_video_name, R.id.tv_video_size, R.id.tv_video_time}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            this.mContext = context;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_video_thumb);
            String thumbImage = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
            Glide.with(context).load(thumbImage).into(imageView);
            //imageView.setImageURI(Uri.parse(thumbImage));
            //imageView.setImageResource(R.drawable.video);
//            TextView textView = (TextView) view.findViewById(R.id.tv_video_time);
//            int size = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
//            textView.setText(size+"");
            super.bindView(view, context, cursor);
        }

        @Override
        public void setViewBinder(ViewBinder viewBinder) {
            super.setViewBinder(viewBinder);
        }

        @Override
        public void setViewImage(ImageView v, String value) {
            super.setViewImage(v, value);
        }

        @Override
        public void setViewText(TextView v, String text) {
            super.setViewText(v, text);
        }

        @Override
        public long getItemId(int position) {
            final Cursor cursor = getCursor();
            if (cursor == null || cursor.getCount() == 0 || position >= cursor.getCount()) {
                return 0;
            }
            cursor.moveToPosition(position);
            return cursor.getLong(0);
        }

        public String getVideoPath(int position) {
            final Cursor cursor = getCursor();
            if (cursor.getCount() == 0) {
                return "";
            }
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        }

        public String getVideoTitle(int position) {
            final Cursor cursor = getCursor();
            if (cursor.getCount() == 0) {
                return "";
            }
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
        }

    }

    //显示媒体详细信息
    private MediaInfo getMediaInfo(Cursor cursor){
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setMediaID(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
        mediaInfo.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
        mediaInfo.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
        mediaInfo.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
        mediaInfo.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
        mediaInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)));
        mediaInfo.setUpdateTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED)));
        mediaInfo.setMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)));
        mediaInfo.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
        mediaInfo.setThumbPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
        mediaInfo.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM)));
        mediaInfo.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST)));
        mediaInfo.setBookMark(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BOOKMARK)));
        mediaInfo.setBacketName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)));
        mediaInfo.setBacketID(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID)));
        mediaInfo.setCategory(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.CATEGORY)));
        mediaInfo.setTakeDate(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)));
        mediaInfo.setDescription(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BOOKMARK)));
        mediaInfo.setBookMark(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DESCRIPTION)));
        mediaInfo.setLanguage(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.LANGUAGE)));
        mediaInfo.setLatitude(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.LATITUDE)));
        mediaInfo.setLongitude(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE)));
        mediaInfo.setTags(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TAGS)));
        mediaInfo.setPrivate(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.IS_PRIVATE)));
        mediaInfo.setResolution(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION)));
        mediaInfo.setMiniThumbPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MINI_THUMB_MAGIC)));

//        Log.i(TAG, "###ID: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
//        Log.i(TAG, "###时长: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
//        Log.i(TAG, "###标题: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
//        Log.i(TAG, "###路径: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
//        Log.i(TAG, "###文件名: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
//        Log.i(TAG, "###创建时间: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)));
//        Log.i(TAG, "###修改时间: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED)));
//        Log.i(TAG, "###文件类型: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)));
//        Log.i(TAG, "###大小: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
//        Log.i(TAG, "###缩略图: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
//        Log.i(TAG, "###专辑: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM)));
//        Log.i(TAG, "###艺术家: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST)));
//        Log.i(TAG, "###书签: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BOOKMARK)));
//        Log.i(TAG, "###空间名: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)));
//        Log.i(TAG, "###空间id: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID)));
//        Log.i(TAG, "###策略: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.CATEGORY)));
//        Log.i(TAG, "###拍摄日期: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)));
//        Log.i(TAG, "###描述: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DESCRIPTION)));
//        Log.i(TAG, "###语言: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.LANGUAGE)));
//        Log.i(TAG, "###纬度: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.LATITUDE)));
//        Log.i(TAG, "###经度: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE)));
//        Log.i(TAG, "###标签: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TAGS)));
//        Log.i(TAG, "###是否私有: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.IS_PRIVATE)));
//        Log.i(TAG, "###分辨率: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION)));
//        Log.i(TAG, "###小缩略图: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MINI_THUMB_MAGIC)));
        return mediaInfo;
    }

    public String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_video_info:
                showToast("详情");
                break;
            case R.id.action_video_delete:
                showToast("删除");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isDelete){
            menu.findItem(R.id.action_video_info).setVisible(true);
            menu.findItem(R.id.action_video_delete).setVisible(true);
        } else {
            menu.findItem(R.id.action_video_info).setVisible(false);
            menu.findItem(R.id.action_video_delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (isDelete){
                isDelete = false;
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                invalidateOptionsMenu();//调用onPrepareOptionsMenu()方法
                setTitle(album);
            } else {
                finish();
            }
        }
        return false;
    }
}

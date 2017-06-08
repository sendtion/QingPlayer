package com.sendtion.qingplayer.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sendtion.qingplayer.R;
import com.sendtion.qingplayer.adapter.AlbumListAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MainActivity";
    private Context mContext;

    //private MediaQueryTask mediaQueryTask;
    //private List<MediaInfo> videoInfoList;
    private ListView mVideoListView;
    private VideoAdapter mVideoAdapter;
    //private VideoListAdapter videoListAdapter;
    private AlbumListAdapter albumListAdapter;
    private Map<String, Integer> albumMap;
    private List<String> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /** 设置是否对日志信息进行加密, 默认false(不加密). */
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
        MobclickAgent.setDebugMode( true );//打开调试模式

        mContext = this;
        //videoInfoList = new ArrayList<>();
        albumMap = new HashMap<>();
        albumList = new ArrayList<>();

        mVideoListView = (ListView) findViewById(R.id.fileListView);
        //mVideoAdapter = new VideoAdapter(this);
        //videoListAdapter = new VideoListAdapter();
        //videoListAdapter.setVideoInfoList(videoInfoList);
        albumListAdapter = new AlbumListAdapter();
        albumListAdapter.setAlbumList(albumList);
        albumListAdapter.setAlbumMap(albumMap);
        mVideoListView.setAdapter(albumListAdapter);

        mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                Intent intent = new Intent(mContext, VideoActivity.class);
                //intent.putExtra("videoPath", videoInfoList.get(position).getPath());
                //showMediaInfo(position);
                //intent.putExtra("videoPath", mVideoAdapter.getVideoPath(position));
                //intent.putExtra("videoTitle", mVideoAdapter.getVideoTitle(position));
                intent.putExtra("album", albumList.get(position));
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(1, null, this);

    }

    //显示媒体详细信息
    private void showMediaInfo(int position){
        final Cursor cursor = mVideoAdapter.getCursor();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(position);
            Log.i(TAG, "###ID: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
            Log.i(TAG, "###时长: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
            Log.i(TAG, "###标题: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
            Log.i(TAG, "###路径: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
            Log.i(TAG, "###文件名: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
            Log.i(TAG, "###创建时间: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)));
            Log.i(TAG, "###修改时间: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED)));
            Log.i(TAG, "###文件类型: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)));
            Log.i(TAG, "###大小: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
            Log.i(TAG, "###缩略图: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
            Log.i(TAG, "###专辑: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM)));
            Log.i(TAG, "###艺术家: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST)));
            Log.i(TAG, "###书签: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BOOKMARK)));
            Log.i(TAG, "###空间名: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)));
            Log.i(TAG, "###空间id: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID)));
            Log.i(TAG, "###策略: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.CATEGORY)));
            Log.i(TAG, "###拍摄日期: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)));
            Log.i(TAG, "###描述: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DESCRIPTION)));
            Log.i(TAG, "###语言: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.LANGUAGE)));
            Log.i(TAG, "###纬度: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.LATITUDE)));
            Log.i(TAG, "###经度: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE)));
            Log.i(TAG, "###标签: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TAGS)));
            Log.i(TAG, "###是否私有: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.IS_PRIVATE)));
            Log.i(TAG, "###分辨率: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION)));
            Log.i(TAG, "###小缩略图: "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MINI_THUMB_MAGIC)));
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //查询条件：指定文件夹下的视频
        String selection = MediaStore.Video.Media.SIZE+" > ?";
        String[] selectionArgs = {"0"};
        String orderBy = "UPPER(" + MediaStore.Video.Media.ALBUM + ")";
        //创建loader
        return new CursorLoader(this, MediaStore.Video.Media.getContentUri("external"), null,
                selection, selectionArgs, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //Android 检索相册视频文件 - 移动开发 - IT问道
        //http://www.itwendao.com/article/detail/145716.html
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
            albumMap.clear();
            albumList.clear();
            while (!cursor.isAfterLast()){
                //不能使用ALBUM查看，应该使用BUCKET_DISPLAY_NAME，因为有的ALBUM为空，导致查询的视频不全
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                if (album != null && album.length() > 0){
                    if (!albumMap.containsKey(album)){
                        albumMap.put(album, 1);
                        albumList.add(album);
                    } else {
                        albumMap.put(album, albumMap.get(album) + 1);
                    }
                }
                cursor.moveToNext();
            }
        }
        //loader加载完成
        //mVideoAdapter.swapCursor(cursor);
        albumListAdapter.setAlbumList(albumList);
        albumListAdapter.setAlbumMap(albumMap);
        albumListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //在loader销毁时调用
        //mVideoAdapter.swapCursor(null);
    }

    public class VideoAdapter extends SimpleCursorAdapter {
        private Context mContext;

        public VideoAdapter(Context context) {
            //flags:CursorAdapter.FLAG_AUTO_REQUERY和CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            //默认被设置为CursorAdapter.FLAG_AUTO_REQUERY,但是3.0以上被废弃
//            super(context, android.R.layout.simple_list_item_1, null,
//                    new String[]{MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA},
//                    new int[]{android.R.id.text1, android.R.id.text2}, 0);
            super(context, R.layout.list_item_folder, null,
                    new String[]{MediaStore.EXTRA_MEDIA_ALBUM, MediaStore.Video.VideoColumns.ALBUM},
                    new int[]{R.id.tv_album_name, R.id.tv_video_count}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            this.mContext = context;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
//            ImageView imageView = (ImageView) view.findViewById(R.id.iv_video_thumb);
//            String thumbImage = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
//            Glide.with(context).load(thumbImage).into(imageView);
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

        public String getVideoAlbum(int position) {
            final Cursor cursor = getCursor();
            if (cursor.getCount() == 0) {
                return "";
            }
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.action_settings://设置
                intent.setClass(MainActivity.this, SettingsActivity.class);
                break;
            case R.id.action_about://关于
                intent.setClass(MainActivity.this, AboutActivity.class);
                break;
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        System.out.println("w"+bitmap.getWidth());
        System.out.println("h"+bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 获取视频缩略图
     * @param videoPath
     * @return
     */
    public static Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        //getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

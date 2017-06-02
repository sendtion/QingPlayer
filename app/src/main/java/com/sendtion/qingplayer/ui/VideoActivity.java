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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sendtion.qingplayer.R;

/**
 * 文件夹下视频列表
 */
public class VideoActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "VideoActivity";
    private Context mContext;
    private String album;//专辑

    private ListView mVideoListView;
    private VideoAdapter mVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_video);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_video);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("videoPath", mVideoAdapter.getVideoPath(0));
                intent.putExtra("videoTitle", mVideoAdapter.getVideoTitle(0));
                startActivity(intent);
            }
        });

        mContext = this;

        Intent intent = getIntent();
        album = intent.getStringExtra("album");
        setTitle(album);

        mVideoListView = (ListView) findViewById(R.id.list_video);
        mVideoAdapter = new VideoAdapter(this);

        mVideoListView.setAdapter(mVideoAdapter);
        mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("videoPath", mVideoAdapter.getVideoPath(position));
                intent.putExtra("videoTitle", mVideoAdapter.getVideoTitle(position));
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //查询条件：指定文件夹下的视频
        String selection = MediaStore.Video.Media.ALBUM+"=?";
        String[] selectionArgs = {album};
        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        //创建loader
        return new CursorLoader(this, MediaStore.Video.Media.getContentUri("external"), null,
                selection, selectionArgs, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //loader加载完成
        mVideoAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //在loader销毁时调用
        mVideoAdapter.swapCursor(null);
    }

    public class VideoAdapter extends SimpleCursorAdapter {

        public VideoAdapter(Context context) {
            //flags:CursorAdapter.FLAG_AUTO_REQUERY和CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            //默认被设置为CursorAdapter.FLAG_AUTO_REQUERY,但是3.0以上被废弃
//            super(context, android.R.layout.simple_list_item_1, null,
//                    new String[]{MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA},
//                    new int[]{android.R.id.text1, android.R.id.text2}, 0);
            super(context, R.layout.list_item_video, null,
                    new String[]{MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.RESOLUTION},
                    new int[]{R.id.tv_video_name, R.id.tv_video_time}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
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
}

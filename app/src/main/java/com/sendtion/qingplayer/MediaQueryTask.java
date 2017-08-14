package com.sendtion.qingplayer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.sendtion.qingplayer.bean.MediaInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShengDecheng on 2017/5/16.
 * Blog: http://sendtion.cn
 * GitHub: https://github.com/sendtion
 * 描述：视频查找任务
 */

// MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
// MediaStore.Video.Media.DATA：视频文件路径；
// MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
// MediaStore.Video.Media.TITLE: 视频标题 : testVideo
// MediaStore.Video.Media.DATE_ADDED ：创建时间
// MediaStore.Video.Media.DATE_MODIFIED ：修改时间
// MediaStore.Video.Media.HEIGHT ：高度
// MediaStore.Video.Media.MIME_TYPE ：文件类型
// MediaStore.Video.Media.SIZE ：视频大小
// MediaStore.Video.VideoColumns.ALBUM ：专辑
// MediaStore.Video.VideoColumns.ARTIST ：艺术家
// MediaStore.Video.VideoColumns.BOOKMARK ：书签
// MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME
// MediaStore.Video.VideoColumns.BUCKET_ID
// MediaStore.Video.VideoColumns.CATEGORY
// MediaStore.Video.VideoColumns.DATE_TAKEN
// MediaStore.Video.VideoColumns.DESCRIPTION
// MediaStore.Video.VideoColumns.DURATION
// MediaStore.Video.VideoColumns.LANGUAGE
// String[] projection = { MediaStore.Video.VideoColumns.DATA,
// MediaStore.Video.Media.DISPLAY_NAME,
// MediaStore.Video.Media.TITLE,
// MediaStore.Video.Media.DATE_ADDED, MediaStore.Video.Media.SIZE,
// MediaStore.Video.VideoColumns.ALBUM,
// MediaStore.Video.VideoColumns.ARTIST,
// MediaStore.Video.VideoColumns.BOOKMARK,
// MediaStore.Video.VideoColumns.DURATION, };
// Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
// Cursor c = this.getContentResolver().query(uri, projection, null, null, null);

public class MediaQueryTask extends AsyncTask<Context, Integer, List<MediaInfo>> {

    private Context context;
    private ArrayList<MediaInfo> mediaInfoList;
    private QueryListener listener;

    public MediaQueryTask(Context context) {
        this.context = context;
        mediaInfoList = new ArrayList<>();
    }

    public void setQueryListener(QueryListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<MediaInfo> doInBackground(Context... params) {
        context = params[0];

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Cursor exCursor = context.getContentResolver()
                    .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            null, null, null, null);
            getMediaInfo(context, exCursor);
        }

        return mediaInfoList;
    }

    @Override
    protected void onPostExecute(List<MediaInfo> mediaInfoList) {
        if (listener != null) {
            listener.onResult(mediaInfoList);
        }
    }

    private void getMediaInfo(Context context, Cursor cursor) {
        while (cursor.moveToNext()) {
            String path = cursor.getString(
                    cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            File file = new File(path);
            if (!file.exists()) {// 文件不存在
                continue;
            }
            MediaInfo mediaInfo = new MediaInfo();
            mediaInfo.setPath(path);
            mediaInfo.setName(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
            mediaInfo.setSize(Integer.parseInt(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Video.Media.SIZE))));
            mediaInfo.setTitle(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
            mediaInfo.setUpdateTime(Integer.parseInt(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))));

            mediaInfo.setMediaID(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Video.Media._ID)));
            mediaInfo.setMimeType(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)));
            mediaInfo.setDuration(cursor.getLong(
                    cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
            mediaInfo.setResolution(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION)));
            mediaInfo.setArtist(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Video.Media.ARTIST)));
            mediaInfo.setAlbum(cursor.getString(
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)));

            // 缓存缩略图
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mediaInfo.getPath(),
                    MediaStore.Images.Thumbnails.MICRO_KIND);
            //String thumbnailPath = FileManager.saveBitmap(context, bitmap, mediaInfo.getMediaID());
            //mediaInfo.setThumbnailPath(thumbnailPath);

            mediaInfoList.add(mediaInfo);

        }

        cursor.close();

    }

    public interface QueryListener {
        void onResult(List<MediaInfo> mediaInfoList);
    }

    //以下方式可以实现，速度较慢，需要开启线程或者异步任务
    //关于Android查询本地视频文件、获取缩略图，并且让缩略图以相同大小显示、给缩略图添加白色边框效果
    //http://blog.csdn.net/a1010012805/article/details/46697587
    public List<MediaInfo> getList() {
        mediaInfoList = null;
        if (context!= null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                Log.i("", "###cursor不为空");
                mediaInfoList = new ArrayList<>();
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String album = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                    String artist = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
                    Log.i("", "###displayName = "+displayName);
                    long duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    if(thumbnail!=null){
                        Bitmap output = Bitmap.createBitmap(zoomImage(thumbnail,143,78).getWidth(),

                                zoomImage(thumbnail,143,78).getHeight(), Bitmap.Config.ARGB_8888);

                        //得到画布
                        Canvas canvas = new Canvas(output);

                        //将画布的四角圆化
                        final Paint paint = new Paint();
                        //得到与图像相同大小的区域  由构造的四个值决定区域的位置以及大小
                        final Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());
                        final RectF rectF = new RectF(rect);

                        paint.setAntiAlias(true);
                        canvas.drawARGB(0, 0, 0, 0);
                        //drawRoundRect的第2,3个参数一样则画的是正圆的一角，如果数值不同则是椭圆的一角
                        canvas.drawRoundRect(rectF, 10,10, paint);

                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                        canvas.drawBitmap(thumbnail, rect, rect, paint);


                        thumbnail = output;
                        Log.i("", "###thumbnail = "+thumbnail);

                        //id, title, album, artist, displayName, mimeType, path, size, duration,thumbnail
                        MediaInfo mediaInfo = new MediaInfo();
                        mediaInfo.setName(displayName);
                        mediaInfo.setPath(path);
                        mediaInfo.setTitle(title);
                        mediaInfo.setArtist(artist);
                        mediaInfo.setDuration(duration);
                        mediaInfo.setMediaID(id+"");
                        mediaInfo.setMimeType(mimeType);
                        mediaInfo.setSize(size);
                        mediaInfo.setAlbum(album);
                        //String thumbnailPath = FileManager.saveBitmap(context, thumbnail, mediaInfo.getMediaID());
                        //mediaInfo.setThumbnailPath(thumbnailPath);
                        mediaInfoList.add(mediaInfo);
                    }
                }
                cursor.close();
            } else {
                Log.i("", "###cursor = null");
            }
        }
        return mediaInfoList;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth)/width;
        float scaleHeight = ((float) newHeight)/height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }
}

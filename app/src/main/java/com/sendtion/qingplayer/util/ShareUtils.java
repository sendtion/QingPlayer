package com.sendtion.qingplayer.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.sendtion.qingplayer.bean.ShareItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShengDecheng on 2017/2/21.
 * Blog: http://sendtion.cn
 * GitHub: https://github.com/sendtion
 * 描述：分享工具类
 */

public class ShareUtils {

    /**
     * context
     */
    private Context mContext;
    /**
     * 扫描到的分享列表
     */
    private List<ResolveInfo> mResolveInfos = new ArrayList<>();
    /**
     * 分享的对象
     */
    private Intent mShareIntent;

    /**
     * create instance
     *
     * @param context app context
     */
    public ShareUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 分享文字笔记
     */
    public static void shareText(Context context, String content){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 分享单张图片
     * @param context
     * @param imagePath
     */
    public static void shareImage(Context context, String imagePath) {
        //String imagePath = Environment.getExternalStorageDirectory() + File.separator + "test.jpg";
        Uri imageUri = Uri.fromFile(new File(imagePath));//由文件得到uri
        Log.d("share", "uri:" + imageUri);  //输出：file:///storage/emulated/0/test.jpg

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 分享多张图片
     */
    public void shareMultipleImage(Context context) {
        ArrayList uriList = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory() + File.separator;
        uriList.add(Uri.fromFile(new File(path+"australia_1.jpg")));
        uriList.add(Uri.fromFile(new File(path+"australia_2.jpg")));
        uriList.add(Uri.fromFile(new File(path + "australia_3.jpg")));

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 分享功能
     *
     * @param context
     *            上下文
     * @param msgTitle
     *            消息标题
     * @param msgText
     *            消息内容
     * @param imgPath
     *            图片路径，不分享图片则传null
     */
    public static void shareTextAndImage(Context context, String msgTitle, String msgText, String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享到"));
    }

    /**
     * 执行分享
     *
     * @param position 用户选中的item
     */
    public void share(int position) {
        ResolveInfo resolveInfo = mResolveInfos.get(position);
        if (resolveInfo != null) {
            ComponentName chosenName = new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            Intent intent = new Intent(mShareIntent);
            intent.setComponent(chosenName);
            try {
                mContext.startActivity(intent);
            } catch (Throwable e) {
            }
        }
    }

    /**
     * 得到支持分享的应用
     *
     * @param type 分享的类型是文字还是图片
     * @return 返回支持分享的app集合
     */
    public List<ShareItem> scanShreaApp(ShareItem.ShareType type, String content) {
        mShareIntent = new Intent(Intent.ACTION_SEND);
        switch (type) {
            case Image:
                mShareIntent.setType("image/*");
                File file = new File(content);
                Uri uri = Uri.fromFile(file);
                mShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                break;
            case Text:
                mShareIntent.setType("text/plain");
                mShareIntent.putExtra(Intent.EXTRA_TEXT, content);
                break;
        }
        PackageManager packageManager = mContext.getPackageManager();
        mResolveInfos.clear();
        mResolveInfos.addAll(packageManager.queryIntentActivities(mShareIntent, 0));
        ArrayList<ShareItem> shareItems = new ArrayList<>();
        for (ResolveInfo resolveInfo : mResolveInfos) {
            ShareItem shareItem = new ShareItem(resolveInfo.loadLabel(packageManager), resolveInfo.loadIcon(packageManager));
            shareItems.add(shareItem);
        }
        return shareItems;
    }

}

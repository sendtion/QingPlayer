package com.sendtion.qingplayer.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.widget.ScrollView;
import android.widget.Toast;

import com.sendtion.qingplayer.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

/**
 * Created by sendtion on 2016/3/30.
 */
public class CommonUtil {

    /****************
     * 发起添加群流程。群号：时光笔记反馈交流群(524312880) 的 key 为： VZrwBQ7ojf3FsLkUU16h19W1_267crol
     * 调用 joinQQGroup(VZrwBQ7ojf3FsLkUU16h19W1_267crol) 即可发起手Q客户端申请加群 时光笔记反馈交流群(524312880)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || key == null || "".equals(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(
                        ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    /**
     * 判断应用是否处于后台
     * @param context
     * @return
     */
    public static boolean isAppOnBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否锁屏
     * @param context
     * @return
     */
    public static boolean isLockScreeen(Context context){
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        if (isScreenOn){
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否为手机号
     */
    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

    /**
     * 判断格式是否为email
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]" +
                "{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 保存文字到图片
     * @param context
     * @param view
     * @param text
     */
    public static void saveAsBitmap(Context context, ScrollView view, String text) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toasty.error(context, "找不到 SDCard !", Toast.LENGTH_LONG).show();
            return ;
        }
        if ("".equals(text)) {
            Toasty.info(context, "没有内容,无法保存 !", Toast.LENGTH_LONG).show();
            return ;
        }
        String filepath = Environment.getExternalStorageDirectory() +File.separator+"text.png";
        try {
            FileOutputStream stream = new FileOutputStream(filepath);
            Bitmap bitmap = createBitmap(view);
            if (bitmap!=null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG,  100, stream);
                Toasty.normal(context, "成功保存到:" + filepath, Toast.LENGTH_LONG).show();
            }
            stream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一张图片
     * @param v
     * @return
     */
    public static Bitmap createBitmap(ScrollView v) {
        int width = 0, height = 0;
        for (int i = 0; i < v.getChildCount(); i++) {
            width  += v.getChildAt(i).getWidth();
            height += v.getChildAt(i).getHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#EBEBEB"));//8B7765
        v.draw(canvas);
        return bitmap;
    }

    /**
     * 选择捐赠方式
     */
    public static void selecteDonateWay(final Context context){
        String[] donateItems = new String[]{"支付宝","微信","QQ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("捐赠方式");
        builder.setItems(donateItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0://支付宝
                        if (AppUtils.isAlipayAvilible(context)) {
                            String alipayUrl = "https://qr.alipay.com/stx08760xuscyfyppe9lr02";//支付宝收款
                            Uri uriAlipay = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=" + alipayUrl);
                            //通过连接打开支付宝捐赠
                            Intent payIntent = new Intent(Intent.ACTION_VIEW, uriAlipay);
                            context.startActivity(payIntent);
                        } else {
                            Toasty.info(context, "您的手机上没有安装支付宝或者版本过低", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1://微信
                        Bitmap bitmap_wx = BitmapFactory.decodeResource(context.getResources(), R.drawable.qrcode_pay_wx);
                        SDCardUtil.saveImageToGallery(context, "wx", bitmap_wx);
                        if (AppUtils.isWeixinAvilible(context)) {
                            Toasty.normal(context, "微信二维码已保存到相册，立即使用微信扫一扫打赏作者吧！", Toast.LENGTH_LONG).show();
                            //AppUtils.RunApp(HistoryActivity.this, "com.tencent.mm");
                            Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                            context.startActivity(intent);
                        } else {
                            Toasty.info(context, "您的手机上没有安装微信或者版本过低", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 2://QQ
                        Bitmap bitmap_qq = BitmapFactory.decodeResource(context.getResources(), R.drawable.qrcode_pay_qq);
                        SDCardUtil.saveImageToGallery(context, "qq", bitmap_qq);
                        if (AppUtils.isQQClientAvailable(context)) {
                            Toasty.normal(context, "QQ二维码已保存到相册，立即使用QQ扫一扫打赏作者吧！", Toast.LENGTH_LONG).show();
                            //AppUtils.RunApp(HistoryActivity.this, "com.tencent.mobileqq");
                            Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                            context.startActivity(intent);
                        } else {
                            Toasty.info(context, "您的手机上没有安装QQ或者版本过低", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        });
        builder.create().show();
    }

}

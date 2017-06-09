package com.sendtion.qingplayer.ui;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sendtion.qingplayer.R;
import com.sendtion.qingplayer.bean.Feedback;
import com.sendtion.qingplayer.util.MarketUtils;
import com.sendtion.qingplayer.util.ShareUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

/**
 * 关于界面
 */
public class AboutActivity extends BaseActivity {
    private static final String TAG = "AboutActivity";

    private RelativeLayout layout_about_update;//检查更新
    private RelativeLayout layout_about_rating;//给个好评
    private RelativeLayout layout_about_feedback;//在线反馈
    private TextView tv_about_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showToast("等等再分享吧~");
                String content = getResources().getString(R.string.about_share_content);
                ShareUtils.shareText(AboutActivity.this, content);
            }
        });

        layout_about_update = (RelativeLayout) findViewById(R.id.layout_about_update);
        layout_about_rating = (RelativeLayout) findViewById(R.id.layout_about_rating);
        layout_about_feedback = (RelativeLayout) findViewById(R.id.layout_about_feedback);

        tv_about_version = (TextView) findViewById(R.id.tv_about_version);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tv_about_version.setText(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        layout_about_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("检查更新");
                BmobUpdateAgent.forceUpdate(AboutActivity.this);
            }
        });
        layout_about_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("给个好评");
                try {
                    String str = "market://details?id=" + getPackageName();
                    Intent localIntent = new Intent("android.intent.action.VIEW");
                    localIntent.setData(Uri.parse(str));
                    startActivity(localIntent);
                } catch (ActivityNotFoundException e) {
                    List<String> pkgs = MarketUtils.queryInstalledMarketPkgs(AboutActivity.this);
                    if (pkgs != null && pkgs.size() > 0){
                        MarketUtils.launchAppDetail(AboutActivity.this, getPackageName(), pkgs.get(0));
                    } else {
                        showToast("抱歉，你没有安装应用市场");
                        Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.sendtion.qingplayer#opened");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        });

        layout_about_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(AboutActivity.this, SettingsActivity.class);
//                startActivity(intent);
                showAddFeedbackDialog();
            }
        });

        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                if (updateStatus == UpdateStatus.Yes) {//版本有更新

                }else if(updateStatus == UpdateStatus.No){
                    showToast("版本无更新");
                }else if(updateStatus==UpdateStatus.EmptyField){//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                    //showToast("请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。");
                }else if(updateStatus==UpdateStatus.IGNORED){
                    showToast("该版本已被忽略更新");
                }else if(updateStatus==UpdateStatus.ErrorSizeFormat){
                   // showToast("请检查target_size填写的格式，请使用file.length()方法获取apk大小。");
                }else if(updateStatus==UpdateStatus.TimeOut){
                    showToast("查询出错或查询超时");
                } else {
                    showToast("查询出错或查询超时");
                }
            }
        });
    }

    /**
     * 添加反馈对话框
     */
    private void showAddFeedbackDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("添加反馈");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_feedback, null);
        final TextView tv_device_info = (TextView) view.findViewById(R.id.tv_device_info);
        final EditText et_user_contact = (EditText) view.findViewById(R.id.et_user_contact);
        final EditText et_user_feedback = (EditText) view.findViewById(R.id.et_user_feedback);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String deviceInfo = Build.MODEL + "_" + Build.VERSION.RELEASE + "_" + packageInfo.versionName;
            tv_device_info.setText(deviceInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        builder.setView(view);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);//点击外部不能取消
        dialog.show();
        //根据输入内容智能判断dialog是否消失
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceInfo = tv_device_info.getText().toString();
                String userContact = et_user_contact.getText().toString();
                String userFeedback = et_user_feedback.getText().toString();
                sendFeedback(dialog,deviceInfo, userContact, userFeedback);//发送反馈
            }
        });
    }

    /**
     * 发送反馈
     */
    public void sendFeedback(final AlertDialog dialog, String deviceInfo, String userContact, String userFeedback){
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("发送中......");
        loading.show();

        if (userContact.length() == 0) {
            showToast("请输入联系方式");
        } else if (userFeedback.length() == 0) {
            showToast("请输入反馈内容");
        } else {
            //MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
            Feedback feedback = new Feedback();
            //feedback.setUser(user);
            feedback.setDeviceInfo(deviceInfo);
            feedback.setUserContact(userContact);
            feedback.setUserFeedback(userFeedback);
            feedback.setIsClose(0);
            feedback.setCommentCount(0);
            feedback.setFavourCount(0);
            feedback.setUnfavourCount(0);
            feedback.save(new SaveListener<String>() {

                @Override
                public void done(String objectId, BmobException e) {
                    dialog.dismiss();
                    loading.dismiss();
                    if (e == null){
                        showToast("感谢您的反馈！");
                        recreate();
                    } else {
                        showToast("反馈失败，请重试！");
                    }
                }
            });
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

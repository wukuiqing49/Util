package com.wu.base.update;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;


import com.wu.base.R;
import com.wu.base.util.AlertUtil;
import com.wu.base.util.MD5Util;
import com.wu.base.util.SharedPreferencesHelper;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 创建：wukuiqing
 * <p>
 * 时间：2017/9/22
 * <p>
 * 描述：
 */

public class DownAPKService extends Service {
    private final int notificationID = 0x10000;
    private boolean isDown;
    private boolean install;
    // 文件下载路径
    private String downUrl = "";
    private boolean isHideInstall;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("downUrl")) {
            downUrl = intent.getStringExtra("downUrl");
            install = intent.getBooleanExtra("install", true);
            if (!TextUtils.isEmpty(downUrl) && !isDown)
                DownFile(downUrl);
            else
                Log.e("下载----------------", "下载地址为空");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 下载apk
     *
     * @param file_url
     */
    private void DownFile(String file_url) {
        // 发送 推送通知

        Notification notification = initNotification(getApplicationContext(), notificationID, "商城", "正在更新 商城");

        if (notification == null) return;

        if (Build.VERSION.SDK_INT >= 26) startForeground(mNotificationID, notification);

        DownloadUtil.get().downloadApk(this,file_url, "current", "商城", new OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                updateSucess(getApplicationContext(), "下载完成", file);
                isDown = false;
            }

            @Override
            public void onDownloading(int progress) {
                isDown = true;
                upProgress(getApplicationContext(), progress);
            }

            @Override
            public void onDownloadFailed(String message) {
                showMessage(message);
                cancleNotification(getApplicationContext());
                isDown = false;
            }
        });
    }


    //-----
    Notification.Builder builder;

    int mNotificationID = 65536;
    long[] pattern = new long[]{100L, 400L, 100L, 400L};

    public Notification initNotification(Context mContext, int NotificationID, String title, String content) {
        mNotificationID = NotificationID;
        Notification notification;
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager == null) return null;
        //26(O版本)版本以上需要增加 channel
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                NotificationChannel channel = new NotificationChannel(2 + "", "版本更新", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true);
                channel.setLightColor(Color.GREEN);
                channel.setShowBadge(true);
                channel.enableVibration(true);
                channel.setDescription("商城 版本更新");
                channel.setVibrationPattern(pattern);
                channel.setLightColor(Color.GREEN);
                channel.setLockscreenVisibility(Notification.FLAG_ONLY_ALERT_ONCE);
                mNotificationManager.createNotificationChannel(channel);
                builder = new Notification.Builder(mContext, 2 + ""); //与channelId对应
            } catch (Exception e) {
                Log.e("推送适配8.0异常*************:", e.getMessage());
            }
        } else {
            builder = new Notification.Builder(mContext); //与channelId对应
            if (Build.VERSION.SDK_INT > 16) {
                //设置显示时间
                builder.setShowWhen(true);
            }
        }
        if (builder == null) return null;

        if (Build.VERSION.SDK_INT > 20) {
            //设置显示时间
            builder.setGroupSummary(false);
            builder.setGroup("商城");
        }
        builder.setSmallIcon(R.drawable.tz_icon);
        builder.setTicker(title);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setWhen(System.currentTimeMillis());//设置通知时间，一般设置的是收到通知时的System.currentTimeMillis()
        builder.setNumber(0);
        builder.setOngoing(true);  //设置 只能代码调用取消
        builder.setProgress(100, 0, false);
        builder.setOnlyAlertOnce(true);//设置只展示一次
        notification = builder.build();
        mNotificationManager.notify(mNotificationID, notification);
        return notification;
    }

    /**
     * Notification 更新进度
     *
     * @param progress
     */
    public void upProgress(Context context, int progress) {
        // 这里是处理 Notification 频繁更新卡顿问题.(部分手机可以开线程 小部分手机不兼容 华为p8 不兼容)
        if (progress % 5 == 0) {
            builder.setProgress(100, progress, false);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (mNotificationManager != null)
                mNotificationManager.notify(mNotificationID, builder.build());
        }
    }

    /**
     * 代码取消 Notification
     *
     * @param context
     */
    public void cancleNotification(Context context) {
        if (context == null) return;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) mNotificationManager.cancel(mNotificationID);
        if (builder != null) builder = null;

        if (Build.VERSION.SDK_INT >= 26) stopForeground(false);
        stopSelf();
    }

    /**
     * 拉起 App安装器
     *
     * @param mContext
     * @param content
     * @param apkFile  文件
     */
    public void updateSucess(Context mContext, String content, File apkFile) {
        if (!TextUtils.isEmpty(downUrl)) {
            try {
                //存储文件MD5 再次下载校验文件直接安装
                String md5 = MD5Util.getMD5(apkFile);
                SharedPreferencesHelper.getInstance(mContext).setValue("download_file_md5_" + downUrl, md5);
                SharedPreferencesHelper.getInstance(mContext).setValue("download_file_path_" + downUrl, apkFile.getAbsolutePath());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setAction(Intent.ACTION_VIEW);
        installIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判读版本是否在7.0以上
            String authority = mContext.getApplicationInfo().packageName + ".fileprovider";
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            installIntent.setDataAndType(FileProvider.getUriForFile(mContext, authority, apkFile), "application/vnd.android.package-archive");
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            installIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        //隐式意图拉起安装器
        PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, 0, installIntent, 0);
        builder.setContentText(content);
        builder.setContentIntent(mPendingIntent);
        builder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null)
            mNotificationManager.notify(mNotificationID, builder.build());
        if (install&&!isHideInstall) mContext.startActivity(installIntent);// 下载完成之后自动弹出安装界面

        cancleNotification(mContext);
    }

    /**
     * 线程回调到 主线程提醒
     *
     * @param message
     */
    private void showMessage(final String message) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                if (message != null &&message.length() > 1)
                    emitter.onNext(message);
                else
                    emitter.onNext("下载失败,请稍后再试!");
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String message) {
                AlertUtil.showDeftToast(getApplicationContext(), message);
            }

            @Override
            public void onError(Throwable e) {
                AlertUtil.showDeftToast(getApplicationContext(), message);
            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void onDestroy() {
        DownloadUtil.get().cancleCall();
        cancleNotification(getApplicationContext());
        super.onDestroy();
        stopSelf();
    }

}

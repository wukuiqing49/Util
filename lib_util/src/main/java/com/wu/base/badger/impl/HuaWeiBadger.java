package com.wu.base.badger.impl;

import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.wu.base.badger.Badger;

import java.util.Arrays;
import java.util.List;


public class HuaWeiBadger extends Badger {



    @Override
    public void executeBadge(Context context, Notification notification, int notificationId, int thisNotificationCount, int count) {
        setNotification(notification, notificationId, context);
        checkIsSupportedByVersion(context);
        if(!isSupportedBade){
            return;
        }

        try{
            String launcherClassName = getLauncherClassName(context);
            Bundle bunlde =new Bundle();
            bunlde.putString("package", context.getPackageName());
            bunlde.putString("class", launcherClassName);
            bunlde.putInt("badgenumber",thisNotificationCount);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList(
                "com.huawei.android.launcher"
        );
    }

boolean isSupportedBade;
    //检测EMUI版本是否支持
    public void checkIsSupportedByVersion( Context context){
        try {
            PackageManager manager =context. getPackageManager();
            PackageInfo info = manager.getPackageInfo("com.huawei.android.launcher", 0);
            if(info.versionCode>=63029){
                isSupportedBade = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}

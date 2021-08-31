package com.wu.base.badger.impl;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import com.wu.base.badger.Badger;

import java.util.Arrays;
import java.util.List;

/**
 * 创建：wukuiqing
 * <p>
 * 时间：2018/4/24
 * <p>
 * 描述：
 */

public class VIVOBadger extends Badger {



    public static final String INTENT_UPDATE_COUNTER = "launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM";
    public static final String PACKAGENAME = "packageName";
    public static final String COUNT = "notificationNum";

    @Override
    public void executeBadge(Context context, Notification notification, int notificationId, int thisNotificationCount, int count) {
        setNotification(notification, notificationId, context);
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }

        Intent intent = new Intent(INTENT_UPDATE_COUNTER);
        intent.putExtra(PACKAGENAME, context.getPackageName());
        intent.putExtra(COUNT, thisNotificationCount);
        intent.putExtra("className", launcherClassName);
        context.sendBroadcast(intent);
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList(
                "com.bbk.launcher2"
        );
    }
}

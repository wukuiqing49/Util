package com.wu.base.util;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Pair;


import com.wu.base.util.DeviceUtils;
import com.wu.base.util.FileUtils;

import java.io.File;

/**
 * @author wkq
 * @date 2021年09月03日 11:45
 * @des 获取截图工具类
 */

public class ScreenShotUtil {
    /**
     * 获取相册中最新一张图片(拍照,截图)
     * 兼容部分
     * @param context
     * @return
     */
    public static Pair<Long, String> getLatestPhoto(Context context) {
        String SCREENSHOTS_IMAGE_BUCKET_NAME = getScreenshotsPath();
        //拍摄照片的地址ID
        String CAMERA_IMAGE_BUCKET_ID = getCameraBucketId();
        //截屏照片的地址ID
        String SCREENSHOTS_IMAGE_BUCKET_ID = getBucketId(SCREENSHOTS_IMAGE_BUCKET_NAME);
        //查询路径和修改时间
        String[] projection = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_MODIFIED};
        //
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        //
        String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        String[] selectionArgsForScreenshots = {SCREENSHOTS_IMAGE_BUCKET_ID};

        //检查camera文件夹，查询并排序
        Pair<Long, String> cameraPair = null;
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
        if (cursor.moveToFirst()) {
            cameraPair = new Pair(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
        }
        //检查Screenshots文件夹
        Pair<Long, String> screenshotsPair = null;
        //查询并排序
        cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgsForScreenshots,
                MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");

        if (cursor.moveToFirst()) {
            screenshotsPair = new Pair(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        //对比
        if (cameraPair != null && screenshotsPair != null) {
            if (cameraPair.first > screenshotsPair.first) {
                screenshotsPair = null;
                return cameraPair;
            } else {
                cameraPair = null;
                return screenshotsPair;
            }

        } else if (cameraPair != null && screenshotsPair == null) {
            return cameraPair;

        } else if (cameraPair == null && screenshotsPair != null) {
            return screenshotsPair;
        }
        return null;
    }
    //你想发送图片时间限制 s
    public static final long SCREEN_SHOT_OFFET_TIME_S = 30;
    //过滤文件大小防止崩溃
    public static final int IMAGE_MAX_SIZE = 25 * 1024 * 1024;

    /**
     *  获取一定时间内更新的图片( 项目中使用这个)
     * @param context
     * @return
     */
    public static Pair<Long, String> getLatestMediaPhoto(Context context) {
        //查询路径和修改时间
        String[] projection = {MediaStore.Images.Media.DATA,MediaStore.Images.Media.DATE_MODIFIED};

        // SCREEN_SHOT_OFFET_TIME_S 表示查询30秒以内的的新插入的图片
        // /1000 的原因是:
        // SaveImageInBackgroundTask -> doInBackground()
        // -> values.put(MediaColumns.DATE_ADDED, mImageTime / 1000);
        //存储截图的时候存储的时间是 秒 所以需要/1000 变成秒

        long currentTime = System.currentTimeMillis()/ 1000 - SCREEN_SHOT_OFFET_TIME_S;
        //检查camera文件夹，查询并排序
        Pair<Long, String> mediaPair = null;
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Images.Media.DATE_ADDED + " >= ?",
                new String[]{currentTime + ""},
                MediaStore.Images.Media.DATE_ADDED + " DESC");
        if (cursor.moveToFirst()) {
           String path= cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
           if (path!=null&& FileUtils.isFileExists(path)&& FileUtils.getFileLength(path)<= IMAGE_MAX_SIZE){
               //填充更新时间 以及 地址
               //注意 cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)) 有可能是秒有可能是毫秒
               //获取的时候做判断的时候需要注意
               mediaPair = new Pair(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)),path);
           }
        }

        //对比
        if (mediaPair != null ) {
            return mediaPair;
        }
        return null;
    }

    private static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

    private static String getCameraBucketId() {
        String CAMERA_IMAGE_BUCKET_NAME = "";
        if (DeviceUtils.isMeizu()) {
            //魅族拍照图片直接放在DCIM中
            CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM";
        } else {
            CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera";
        }
        return String.valueOf(CAMERA_IMAGE_BUCKET_NAME.toLowerCase().hashCode());
    }

    /**
     * 获取截图路径
     *
     * @return
     */
    public static String getScreenshotsPath() {
        String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/Screenshots";
        File file = new File(path);
        if (!file.exists()) {
            path = Environment.getExternalStorageDirectory().toString() + "/Pictures/Screenshots";
        }
        file = null;
        return path;
    }
}

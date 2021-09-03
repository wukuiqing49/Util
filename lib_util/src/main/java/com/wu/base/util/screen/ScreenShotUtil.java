package com.wu.base.util.screen;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Pair;

import java.io.File;

/**
 * @author wkq
 * @date 2021年09月03日 11:45
 * @des
 */

public class ScreenShotUtil {
    /**
     * 获取相册中最新一张图片
     *
     * @param context
     * @return
     */
    public static Pair<Long, String> getLatestPhoto(Context context) {
        //拍摄照片的地址
        String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";
        //截屏照片的地址
//        /storage/emulated/0/Android/data/com.cnlive.strike.debug/files/DCIM   context.getExternalFilesDir(Environment.DIRECTORY_DCIM)
        String SCREENSHOTS_IMAGE_BUCKET_NAME = getScreenshotsPath();
        //拍摄照片的地址ID
        String CAMERA_IMAGE_BUCKET_ID = getBucketId(CAMERA_IMAGE_BUCKET_NAME);
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

    private static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
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

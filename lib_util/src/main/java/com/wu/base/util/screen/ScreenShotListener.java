package com.wu.base.util.screen;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author wkq
 * @date 2021年08月20日 13:06
 * @des
 */

public class ScreenShotListener {

    private static ScreenShotListener instance;
    /**
     * 外部存储器内容观察者
     */
    private MediaContentObserver mExternalObserver;

    /**
     * 内部存储器内容观察者
     */
    private MediaContentObserver mInternalObserver;


    //运行在 UI 线程的 Handler, 用于运行监听器回调
    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    long mStartListenTime;
    Point mScreenRealSize;
    private ArrayList<String> mHasCallbackPaths = new ArrayList<String>();
    OnScreenShotListener mListener;

    public static ScreenShotListener getInstance() {
        synchronized (ScreenShotListener.class) {
            if (instance == null) instance = new ScreenShotListener();
        }
        return instance;
    }

    Cursor cursor;

    public void handleMediaContentChange(Context mContext, Uri contentUri) {
        try {
            cursor = mContext.getContentResolver().query(contentUri, Build.VERSION.SDK_INT < 16 ? ScreenShotHelper.MEDIA_PROJECTIONS : ScreenShotHelper.MEDIA_PROJECTIONS_API_16, null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1");
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }
            int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);
            int widthIndex = -1;
            int heightIndex = -1;
            if (Build.VERSION.SDK_INT >= 16) {
                widthIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH);
                heightIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT);
            }

            String data = cursor.getString(dataIndex);
            long dateTaken = cursor.getLong(dateTakenIndex);

            int width = 0;
            int height = 0;

            if (widthIndex >= 0 && heightIndex >= 0) {
                width = cursor.getInt(widthIndex);
                height = cursor.getInt(heightIndex);
            } else {
                Point size = getImageSize(data);
                width = size.x;
                height = size.y;
            }
            // 处理获取到的第一行数据
            handleMediaRowData(data, dateTaken, width, height);
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    // 处理获取到的第一行数据
    private void handleMediaRowData(String data, long dateTaken, int width, int height) {
        if (checkScreenShot(data, dateTaken, width, height)) {
            if (mListener != null && !checkCallback(data)) {
                mListener.onScreenShot(data);
            }
        }

    }

    /**
     * 判断是否已回调过, 某些手机ROM截屏一次会发出多次内容改变的通知; <br></br>
     * 删除一个图片也会发通知, 同时防止删除图片时误将上一张符合截屏规则的图片当做是当前截屏.
     */
    private Boolean checkCallback(String imagePath) {
        if (mHasCallbackPaths.contains(imagePath)) {
            return true;
        }
        // 大概缓存15~20条记录便可
        if (mHasCallbackPaths.size() >= 20) {
            for (int i = 0; i < 4; i++) {
                mHasCallbackPaths.remove(0);
            }
        }
        mHasCallbackPaths.add(imagePath);
        return false;
    }

    private boolean checkScreenShot(String data, long dateTaken, int width, int height) {
        // 判断依据一: 时间判断
        // 如果加入数据库的时间在开始监听之前, 或者与当前时间相差大于10秒, 则认为当前没有截屏
        if (dateTaken < mStartListenTime || System.currentTimeMillis() - dateTaken > 60 * 1000) {
            return false;
        }


        // 判断依据二: 尺寸判断
        if (mScreenRealSize != null) {
            // 如果图片尺寸超出屏幕, 则认为当前没有截屏
            if (!(width <= mScreenRealSize.x && height <= mScreenRealSize.y)
                    || (height <= mScreenRealSize.x && width <= mScreenRealSize.y)
            ) {
                return false;
            }
        }

        // 判断依据三: 路径判断
        if (TextUtils.isEmpty(data)) {
            return false;
        }

        String lowerData = data.toLowerCase(Locale.getDefault());
        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了

        for (String keyword : ScreenShotHelper.KEYWORDS) {
            if (lowerData.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private Point getImageSize(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return new Point(options.outWidth, options.outHeight);
    }

    private void assertInMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            String methodMsg = null;
            if (stackTrace != null && stackTrace.length >= 4) {
                methodMsg = stackTrace[3].toString();
            }
        }
    }

    /**
     * 开启监听
     */
    public void startListener(Context mContext) {
        assertInMainThread();

        // 记录开始监听的时间戳
        mStartListenTime = System.currentTimeMillis();

        // 创建内容观察者
        mInternalObserver = new MediaContentObserver(mContext, MediaStore.Images.Media.INTERNAL_CONTENT_URI, mUiHandler);
        mExternalObserver = new MediaContentObserver(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mUiHandler);

        // 注册内容观察者
        mContext.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q,
                mInternalObserver
        );
        mContext.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q,
                mExternalObserver
        );
    }

    //结束监听
    public void stopListener(Context mContext) {
        // 注销内容观察者
        if (mInternalObserver != null) {
            try {
                mContext.getContentResolver().unregisterContentObserver(mInternalObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mInternalObserver = null;
        }
        if (mExternalObserver != null) {
            try {
                mContext.getContentResolver().unregisterContentObserver(mExternalObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mExternalObserver = null;
        }

        // 清空数据
        mStartListenTime = 0;
        mListener = null;
    }


    /**
     * 媒体内容观察者
     */
    private class MediaContentObserver extends ContentObserver {
        Uri contentUri;
        Context mContext;

        public MediaContentObserver(Context context, Uri uri, Handler handler) {
            super(handler);
            contentUri = uri;
            mContext = context;
        }

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MediaContentObserver(Handler handler) {
            super(handler);

        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            handleMediaContentChange(mContext, contentUri);
        }
    }

    /**
     * 设置截屏监听器回调
     */
    public void setListener(OnScreenShotListener listener) {
        this.mListener = listener;
    }

    /**
     * 截屏监听接口
     */
     public interface OnScreenShotListener {
        void onScreenShot(String picPath);
    }

}

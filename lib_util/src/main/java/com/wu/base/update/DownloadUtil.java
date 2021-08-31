package com.wu.base.update;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;


import com.wu.base.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 创建 wkq
 * <p>
 * 时间：2017/9/16
 * <p>
 * 描述： 下载的工具类
 */
public class DownloadUtil {
    Call call;
    File file;
    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param url      下载连接
     * @param saveDir  储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void downloadApk(final Context context, final String url, final String saveDir, final String name,  OnDownloadListener listener) {
        //判断下载URL为空
        if (TextUtils.isEmpty(url)) {
            listener.onDownloadFailed("下载地址为空");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(url).build();
                call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        listener.onDownloadFailed("下载异常,请检查您的网络稍后再试!!");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        saveStream(context, response, saveDir, name, listener);
                    }
                });
            }
        }).start();

    }

    /**
     * 保存图片
     */
    private void saveStream(Context context, Response response, String saveDir, String name, OnDownloadListener listener) {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        long sum = 0;
        // 储存下载文件的目录
        String savePath = isExistDir(context, saveDir);
        if (TextUtils.isEmpty(saveDir)) {
            listener.onDownloadFailed("下载异常,请稍后再试!!");
            return;
        }
        try {
            is = response.body().byteStream();
            long total = response.body().contentLength();
            if (total < 20000) {
                listener.onDownloadFailed("安装包还没有准备哈,请稍后再试!");
                return;
            }
            file = new File(savePath, name + System.currentTimeMillis() + ".apk");
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                int progress = (int) (sum * 1.0f / total * 100);
                listener.onDownloading(progress);
            }
            fos.flush();
            saveFile(context, file, listener);

        } catch (Exception e) {
            listener.onDownloadFailed("下载异常,请稍后再试!!");
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 下载完成后MD5保存
     */
    private void saveFile(Context context, File file, OnDownloadListener listener) {
        if (!FileUtils.isFileExists(file.getPath())) {
            listener.onDownloadFailed("下载失败");
            return;
        }
        String dirPath = isExistDir(context, "APK");
        if (TextUtils.isEmpty(dirPath)) return;
        String md5 = FileUtils.getFileMD5ToString(file.getPath());
        try {
            InputStream inStream = new FileInputStream(file);
            File newFile = new File(dirPath, md5 + ".apk");
            FileOutputStream outputStream = new FileOutputStream(newFile);
            int byteread = 0;
            byte[] buffer2 = new byte[1444];
            while ((byteread = inStream.read(buffer2)) != -1)
                outputStream.write(buffer2, 0, byteread);
            inStream.close();
            outputStream.close();
            listener.onDownloadSuccess(newFile);
            FileUtils.deleteFile(file);
        } catch (Exception e) {
            listener.onDownloadFailed("下载失败");
        }
    }

    /**
     * 取消下载
     */
    public void cancleCall() {
        if (call != null) call.cancel();
    }

    /**
     * 判断文件是否存在 且返回文件地址
     */
    private String isExistDir(Context context, String saveDir) {
        if (TextUtils.isEmpty(saveDir)) return null;
        String dirPath = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? context.getExternalFilesDir("") : Environment.getExternalStorageDirectory()) + File.separator + "strike" + File.separator + saveDir;
        boolean isExis = FileUtils.createOrExistsDir(dirPath);
        return isExis ? dirPath : null;
    }

}


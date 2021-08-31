package com.wu.base.update;

import java.io.File;

/**
 * 作者: 吴奎庆
 * <p>
 * 时间: 2018/8/30
 * <p>
 * 简介:
 */

public interface OnDownloadListener {
    /**
     * 下载成功
     */
    void onDownloadSuccess(File file);

    /**
     * @param progress 下载进度
     */
    void onDownloading(int progress);

    /**
     * 下载失败
     *
     * @param path
     */
    void onDownloadFailed(String path);
}

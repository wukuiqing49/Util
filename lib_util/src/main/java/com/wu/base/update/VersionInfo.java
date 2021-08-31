package com.wu.base.update;

import java.io.Serializable;

/**
 * 作者:吴奎庆
 * <p>
 * 时间:2021/2/20
 * <p>
 * 用途:
 */


public class VersionInfo implements Serializable {
    /**
     * version : 10000
     * downloadUrl : https://app.com/voa.apk
     * description : 新版本增加了更多有趣内容, 快来体验吧~
     */

    private int version;
    private String downloadUrl;
    private String description;
    public boolean forceUpdating;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

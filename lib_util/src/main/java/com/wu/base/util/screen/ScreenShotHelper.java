package com.wu.base.util.screen;

import android.provider.MediaStore;


/**
 * @author wkq
 * @date 2021年08月20日 13:02
 * @des
 */

public class ScreenShotHelper {

    /**
     * 读取媒体数据库时需要读取的列
     */
    static String[]  MEDIA_PROJECTIONS = new String[]{MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN};

    /**
     * 读取媒体数据库时需要读取的列，其中 width、height 字段在 API 16 之后才有
     */
    static String[]  MEDIA_PROJECTIONS_API_16 = new String[]{MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.WIDTH,
            MediaStore.Images.ImageColumns.HEIGHT};
    /**
     * 截屏路径判断的关键字
     */
    static String[]  KEYWORDS=new String[]{"screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap","Screenshot","SCREEN"};

}

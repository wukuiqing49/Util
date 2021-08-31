package com.wu.base.util;

import android.content.Context;
import android.view.Gravity;

import com.wu.base.R;
import com.wu.base.util.toast.ToastUtil;


/**
 * Created by xiansong on 2017/9/11.
 */

public class AlertUtil {
    /**
     * 声明全局ToastUtil.Build对象
     * 通过.build()获得ToastUtil对象
     */
    private static final ToastUtil.Build mainToast = ToastUtil.newBuild()
            //对齐参数设置
            .setGravity(Gravity.CENTER, 0, 0)
            //设置布局 Layout Id 文本视图 View Id
            .setView(R.layout.layout_toast_1, R.id.text);

    public static void showDeftToast(Context context, int messageRes) {
        mainToast.build(context).showToast(context.getString(messageRes));
    }

    public static void showDeftToast(Context context, String message) {
        mainToast.build(context).showToast(message);
    }

    public static void showLongToast(Context context, String message, int duration){
        mainToast.build(context).showToast(message,duration);
    }

    /**
     * 成功的提示
     *
     * @param context
     * @param message
     */
    public static void showSuccessToast(Context context, String message) {
        ToastUtil.Build successToast = ToastUtil.newBuild()
                //对齐参数设置
                .setGravity(Gravity.CENTER, 0, 0)
                //设置布局 Layout Id 文本视图 View Id
                .setView(R.layout.layout_toast_success, R.id.text);
        successToast.build(context).showToast(message);
    }

    /**
     * 失败的提示
     *
     * @param context
     * @param message
     */
    public static void showFailedToast(Context context, String message) {
        ToastUtil.Build failedToast = ToastUtil.newBuild()
                .setGravity(Gravity.CENTER, 0, 0)
                .setView(R.layout.layout_toast_failed, R.id.text);
        failedToast.build(context).showToast(message);
    }

    /**
     * 不比较吐司内容是否相同提示
     *
     * @param context
     * @param message
     */
    public static void showNoEqulesToast(Context context, String message) {
        ToastUtil.Build failedToast = ToastUtil.newBuild()
                .setGravity(Gravity.CENTER, 0, 0)
                .setView(R.layout.layout_toast_1, R.id.text);
        failedToast.build(context).showNoEqulesToast(message);
    }
}

package com.wu.base.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Locale;

/**
 * 系统版本信息类
 *
 * @author tangjun
 */
public class DeviceUtils {

    private static final String TAG = "DeviceUtils";
    // private static final long NO_STORAGE_ERROR = -1L;
    private static final long CANNOT_STAT_ERROR = -2L;

    /**
     * >=2.2
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * >=2.3
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * >=3.0 LEVEL:11
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * >=3.1
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * >=4.0 14
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static int getSDKVersionInt() {
        return Build.VERSION.SDK_INT;
    }

    @SuppressWarnings("deprecation")
    public static String getSDKVersion() {
        return Build.VERSION.SDK;
    }

    /**
     * 判断是否是平板电脑
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    /**
     * 获得设备型号
     *
     * @return
     */
    public static String getDeviceModel() {
        return trim(Build.MODEL);
    }

    /**
     * 检测是否魅族手机
     */
    public static boolean isMeizu() {
        return getDeviceModel().toLowerCase(Locale.getDefault()).contains("meizu");
    }

    /**
     * 检测是否HTC手机
     */
    public static boolean isHTC() {
        return getDeviceModel().toLowerCase(Locale.getDefault()).contains("htc");
    }

    public static boolean isXiaomi() {
        return getDeviceModel().toLowerCase(Locale.getDefault()).contains("xiaomi");
    }

    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }
    /**
     * 获得设备制造商
     *
     * @return
     */
    public static String getManufacturer() {
        return trim(Build.MANUFACTURER);
    }

    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return 0;
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 获取屏幕宽度
     */
    @SuppressWarnings("deprecation")
   /* public static int getScreenWidth(Context context) {
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();
    }*/

    public static int getScreenWidth(Context ctx) {
        return Math.min(
                ctx.getResources().getDisplayMetrics().widthPixels,
                ctx.getResources().getDisplayMetrics().heightPixels);
    }

    public static int getHeight(Context ctx) {
        return Math.min(
                ctx.getResources().getDisplayMetrics().widthPixels,
                ctx.getResources().getDisplayMetrics().heightPixels);
    }

    /**
     * 获得设备屏幕密度
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics metrics = context.getApplicationContext().getResources()
                .getDisplayMetrics();
        return metrics.density;
    }

    public static int[] getScreenSize(int w, int h, Context context) {
        int phoneW = getScreenWidth(context);
        int phoneH = getScreenHeight(context);

        if (w * phoneH > phoneW * h) {
            phoneH = phoneW * h / w;
        } else if (w * phoneH < phoneW * h) {
            phoneW = phoneH * w / h;
        }

        return new int[]{phoneW, phoneH};
    }

    public static int[] getScreenSize(int w, int h, int phoneW, int phoneH) {
        if (w * phoneH > phoneW * h) {
            phoneH = phoneW * h / w;
        } else if (w * phoneH < phoneW * h) {
            phoneW = phoneH * w / h;
        }
        return new int[]{phoneW, phoneH};
    }

    /**
     * 设置屏幕亮度
     */
    public static void setBrightness(final Activity context, float f) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = f;
        if (lp.screenBrightness > 1.0f)
            lp.screenBrightness = 1.0f;
        else if (lp.screenBrightness < 0.01f)
            lp.screenBrightness = 0.01f;
        context.getWindow().setAttributes(lp);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(Context ctx) {
        if (ctx != null) {
            InputMethodManager imm = ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE));
            View view = ((Activity) ctx).getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(Context ctx, View view) {
        // InputMethodManager.SHOW_FORCED);
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static String getCpuInfo() {
        String cpuInfo = "";
        try {
            if (new File("/proc/cpuinfo").exists()) {
                FileReader fr = new FileReader("/proc/cpuinfo");
                BufferedReader localBufferedReader = new BufferedReader(fr,
                        8192);
                cpuInfo = localBufferedReader.readLine();
                localBufferedReader.close();

                if (cpuInfo != null) {
                    cpuInfo = cpuInfo.split(":")[1].trim().split(" ")[0];
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return cpuInfo;
    }

    public static void startApkActivity(final Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(packageName, 0);
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(pi.packageName);

            List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String className = ri.activityInfo.name;
                intent.setComponent(new ComponentName(packageName, className));
                ctx.startActivity(intent);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 计算视频宽度
     *
     * @param ctx
     * @return
     */
    public static int getVideoWidth(Context ctx) {
        return Math.min(
                ctx.getResources().getDisplayMetrics().widthPixels,
                ctx.getResources().getDisplayMetrics().heightPixels);
    }

    /**
     * 计算屏幕高度
     *
     * @param ctx
     * @return
     */
    public static int getDeviceWidth(Context ctx) {
        return Math.max(
                ctx.getResources().getDisplayMetrics().widthPixels,
                ctx.getResources().getDisplayMetrics().heightPixels);
    }

    /**
     * 跳转定位服务界面
     *
     * @param context 全局信息接口
     */
    public static void gotoLocServiceSettings(Context context) {
        final Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 复制内容至剪贴板
     *
     * @param context
     * @param text
     */
    public static void copyText(Context context, String text) {
        if (TextUtils.isEmpty(text)) return;
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) cm.setText(text);
    }


    public static int dip2px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }
}

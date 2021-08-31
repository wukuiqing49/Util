package com.wu.base.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;


public class LanguageUtil {

    public static final String CN = "cn";//中文

    public static final String IN = "in";//印尼语

    public static final String EN = "en";//英语

    public static final String ES = "es";//西班牙语

    public static final String AR = "ar";//阿拉伯语

    public static final String TW = "zh-TW";


    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }


    /**
     * 获取系统的locale
     *
     * @return Locale对象
     */
    public static Locale getSystemLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }



    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = LanguageUtil.getLocaleByLanguage(language);

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context.createConfigurationContext(configuration);
    }

    public static void changeAppLanguage(Context context, String newLanguage) {
        SharedPreferencesHelper.getInstance(context).setValue(SELECTED_LANGUAGE_KEY, newLanguage);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();
            Locale locale = getLocaleByLanguage(newLanguage);
            configuration.setLocale(locale);
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, dm);
        }
    }

    public static String getSavedLanguage(Context mContext) {

        String savedLanguage = SharedPreferencesHelper.getInstance(mContext).getValue(SELECTED_LANGUAGE_KEY);
        if (savedLanguage != null) {
            return savedLanguage;
        } else {
            return Locale.getDefault().getLanguage().toLowerCase();
        }
    }


    private static Locale getLocaleByLanguage(String language) {
        Locale locale;
        if (language.equals(TW)) {
            locale = Locale.TAIWAN;
        } else {
            locale = Locale.forLanguageTag(language);
        }
        return locale;
    }

    public static Context getLanguageContext(Context context, String language) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getLocaleByLanguage(language);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }

    public static String getH5LanguageWithLanguage(Context context) {
        String curAppLanguage = getSavedLanguage(context);
        String h5Language = LanguageUtil.CN;
        if (curAppLanguage.contains(LanguageUtil.CN)) {
            h5Language = "language-zh";
        } else if (curAppLanguage.contains(LanguageUtil.IN)) {
            h5Language = "language-id";
        } else if (curAppLanguage.contains(LanguageUtil.EN)) {
            h5Language = "language-en";
        } else if (curAppLanguage.contains(LanguageUtil.TW)) {
            h5Language = "language-tw";
        } else if (curAppLanguage.contains(LanguageUtil.ES)) {
            h5Language = "language-es";
        } else if (curAppLanguage.contains(LanguageUtil.AR)) {
            h5Language = "language-ar";
        }
        return h5Language;
    }

    private static final String SELECTED_LANGUAGE_KEY = "yxy_selected_language";

}

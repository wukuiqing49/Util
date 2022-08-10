package com.wu.base.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;


import com.wu.base.R;

import java.text.DecimalFormat;

/**
 * @author wkq
 * @date 2022年07月25日 10:54
 * @des
 */

public class TextViewEffectUtil {

    /**
     * 中间划线的效果
     * @param textView
     */
    public  static void setStrikethrough(TextView textView){
        textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * Html 方式设置不同文字大小
     * @param textview
     * @param money
     */
    public static void setMoney(TextView textview, double money){
        String moneyss= "<font color='#FFFFFF'><small>¥ </small></font><font color='#FFFFFF'><big>%s</big></font>";
        String moneyContent= String.format(moneyss,getNumDiff(money));
        textview.setText(Html.fromHtml(moneyContent));

    }


    /**
     * span 方式设置 不同字体大小
     * @param textview
     * @param money
     * @param sizeSpan  字体大小  大于100 是比设置的字体大  小于100是比设置的小
     */
    public static void setMoney(TextView textview, double money, int sizeSpan){
        String moneyss= "¥ %s";
        String moneyContent= String.format(moneyss,getNumDiff(money));
        SpannableString spannableString = new SpannableString(moneyContent);
        spannableString.setSpan(new AbsoluteSizeSpan(sizeSpan), 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textview.setText(spannableString);
    }

    /**
     * 设置小数点以后文字大小
     * @param textview
     * @param money
     * @param sizeSpan  字体大小  大于100 是比设置的字体大  小于100是比设置的小
     */
    public static void setMoneyTextView(TextView textview, double money, int sizeSpan){
        String moneyss= "¥ %.2f";
        String moneyContent= String.format(moneyss,money);
        int index=moneyContent.indexOf(".");
        SpannableString spannableString = new SpannableString(moneyContent);
        spannableString.setSpan(new AbsoluteSizeSpan(sizeSpan), index, moneyContent.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textview.setText(spannableString);

    }

    /**
     * html方式
     * 设置字体的不同颜色
     * @param textview
     * @param content
     */
    public static void setColor(TextView textview, String content){
//        String str6 = "<font color=\"#00ff00\">我的</font><font color=\"#0000ff\">作业完成了</font>";
        String moneyss= "%s <font color=\"#FF0000\">%s</font>";
        String strigContent= String.format(moneyss,"我的","作业完成了");
        textview.setText(Html.fromHtml(strigContent));
    }

    /**
     * 设置文字颜色  span 方式
     * @param textview
     * @param content
     * @param endSize
     */
    public static void setColor(TextView textview, String content, int endSize){
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), endSize, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(spannableString);
    }

    /**
     * 设置图片
     * @param mContext
     * @param textview
     * @param content
     */
    public static void setDrawable(Context mContext, TextView textview, String content){
        SpannableString spannableString = new SpannableString(" "+content);
        //这只图片的宽高
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_notifi);
        drawable.setBounds(0, 0, 100, 100);

        ImageSpan imageSpan = new ImageSpan(drawable);
        spannableString.setSpan(imageSpan, 0, 1, ImageSpan.ALIGN_BASELINE);

        Drawable drawable2 = mContext.getResources().getDrawable(R.drawable.ic_notifi);
        drawable2.setBounds(0, 0, 100, 100);
        ImageSpan imageSpan2 = new ImageSpan(drawable2);
        spannableString.setSpan(imageSpan2, 2, 3, ImageSpan.ALIGN_BASELINE);
        textview.setText(spannableString);

    }



    /**
     * 数量转换
     */
    public  static String getNumDiff(double count) {
        String fansNum = "";
        if (count >= 0 && count < 10000) {
            fansNum = count + "";
        } else if (count >= 10000 && count < 10000000) {
            if (count / 1000 % 10 != 0) {
                float num = (float) count / 10000;
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                fansNum = decimalFormat.format(num).concat("万");
            } else {
                fansNum = (count / 10000) + "万";
            }
        } else if (count >= 10000000) {
            fansNum = "1000万";
        }
        return fansNum;
    }

}

package com.wu.base.util;

import android.annotation.TargetApi;
import android.nfc.NdefRecord;
import android.os.Build;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类
 *
 * @author tangjun
 */
public class StringFormatUtils {

    public static final String EMPTY = "";
    public static final SimpleDateFormat DATE_FORMAT_PART = new SimpleDateFormat(
            "HH:mm", Locale.getDefault());
    private static final String TAG = StringFormatUtils.class.getSimpleName();
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd hh:mm:ss";
    /**
     * 用于生成文件
     */
    private static final String DEFAULT_FILE_PATTERN = "yyyy-MM-dd-HH-mm-ss";
    private static final double KB = 1024.0;
    private static final double MB = 1048576.0;
    private static final double GB = 1073741824.0;

    public static String currentTimeString() {
        return DATE_FORMAT_PART.format(Calendar.getInstance().getTime());
    }

    public static char chatAt(String pinyin, int index) {
        if (pinyin != null && pinyin.length() > 0)
            return pinyin.charAt(index);
        return ' ';
    }

    /**
     * 获取字符串宽度
     */
    public static float GetTextWidth(String Sentence, float Size) {
        if (isEmpty(Sentence))
            return 0;
        TextPaint FontPaint = new TextPaint();
        FontPaint.setTextSize(Size);
        return FontPaint.measureText(Sentence.trim()) + (int) (Size * 0.1); // 留点余地
    }

    /**
     * 格式化日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 格式化日期字符串
     *
     * @param date
     * @return 例如2011-3-24
     */
    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_DATE_PATTERN);
    }

    public static String formatDate(long date) {
        return formatDate(new Date(date), DEFAULT_DATE_PATTERN);
    }

    /**
     * 获取当前时间 格式为yyyy-MM-dd 例如2011-07-08
     *
     * @return
     */
    public static String getDate() {
        return formatDate(new Date(), DEFAULT_DATE_PATTERN);
    }

    /**
     * 生成一个文件名，不含后缀
     */
    public static String createFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FILE_PATTERN, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getDateTime() {
        return formatDate(new Date(), DEFAULT_DATETIME_PATTERN);
    }

    /**
     * 格式化日期时间字符串
     *
     * @param date
     * @return 例如2011-11-30 16:06:54
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, DEFAULT_DATETIME_PATTERN);
    }

    public static String formatDateTime(long date) {
        return formatDate(new Date(date), DEFAULT_DATETIME_PATTERN);
    }

    /**
     * 格林威时间转换
     *
     * @param gmt
     * @return
     */
    public static String formatGMTDate(String gmt) {
        TimeZone timeZoneLondon = TimeZone.getTimeZone(gmt);
        return formatDate(Calendar.getInstance(timeZoneLondon)
                .getTimeInMillis());
    }

    /**
     * 拼接数组
     *
     * @param array
     * @param separator
     * @return
     */
    public static String join(final ArrayList<String> array,
                              final String separator) {
        StringBuffer result = new StringBuffer();
        if (array != null && array.size() > 0) {
            for (String str : array) {
                result.append(str);
                result.append(separator);
            }
            result.delete(result.length() - 1, result.length());
        }
        return result.toString();
    }

    public static String join(final Iterator<String> iter,
                              final String separator) {
        StringBuffer result = new StringBuffer();
        if (iter != null) {
            while (iter.hasNext()) {
                String key = iter.next();
                result.append(key);
                result.append(separator);
            }
            if (result.length() > 0)
                result.delete(result.length() - 1, result.length());
        }
        return result.toString();
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.equals("null");
    }

    /**
     * @param str
     * @return
     */
    public static String trim(String str) {
        return str == null ? EMPTY : str.trim();
    }

    /**
     * 转换时间显示
     *
     * @param time 毫秒
     * @return
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes,
                seconds) : String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    /**
     * 根据秒速获取时间格式
     */
    public static String gennerTime(int totalSeconds) {
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    /**
     * 转换文件大小
     *
     * @param size
     * @return
     */
    public static String generateFileSize(long size) {
        String fileSize;
        if (size < KB)
            fileSize = size + "B";
        else if (size < MB)
            fileSize = String.format("%.1f", size / KB) + "KB";
        else if (size < GB)
            fileSize = String.format("%.1f", size / MB) + "MB";
        else
            fileSize = String.format("%.1f", size / GB) + "GB";

        return fileSize;
    }

    public static String getTimeDiff(long time) {
        // Calendar cal = Calendar.getInstance();
        long diff = 0;
        // Date dnow = cal.getTime();
        String str = "";
        diff = System.currentTimeMillis() - time;

        if (diff > 2592000000L) {// 30 * 24 * 60 * 60 * 1000=2592000000 毫秒
            str = "1个月前";
        } else if (diff > 1814400000) {// 21 * 24 * 60 * 60 * 1000=1814400000 毫秒
            str = "3周前";
        } else if (diff > 1209600000) {// 14 * 24 * 60 * 60 * 1000=1209600000 毫秒
            str = "2周前";
        } else if (diff > 604800000) {// 7 * 24 * 60 * 60 * 1000=604800000 毫秒
            str = "1周前";
        } else if (diff > 86400000) { // 24 * 60 * 60 * 1000=86400000 毫秒
            // System.out.println("X天前");
            str = (int) Math.floor(diff / 86400000f) + "天前";
        } else if (diff > 18000000) {// 5 * 60 * 60 * 1000=18000000 毫秒
            // System.out.println("X小时前");
            str = (int) Math.floor(diff / 18000000f) + "小时前";
        } else if (diff > 60000) {// 1 * 60 * 1000=60000 毫秒
            // System.out.println("X分钟前");
            str = (int) Math.floor(diff / 60000) + "分钟前";
        } else {
            str = (int) Math.floor(diff / 1000) + "秒前";
        }
        return str;
    }

    /**
     * 截取字符串
     *
     * @param search       待搜索的字符串
     * @param start        起始字符串 例如：<title>
     * @param end          结束字符串 例如：</title>
     * @param defaultValue
     * @return
     */
    public static String substring(String search, String start, String end,
                                   String defaultValue) {
        int start_len = start.length();
        int start_pos = StringFormatUtils.isEmpty(start) ? 0 : search.indexOf(start);
        if (start_pos > -1) {
            int end_pos = StringFormatUtils.isEmpty(end) ? -1 : search.indexOf(end,
                    start_pos + start_len);
            if (end_pos > -1)
                return search.substring(start_pos + start.length(), end_pos);
            else
                return search.substring(start_pos + start.length());
        }
        return defaultValue;
    }

    /**
     * 截取字符串
     *
     * @param search 待搜索的字符串
     * @param start  起始字符串 例如：<title>
     * @param end    结束字符串 例如：</title>
     * @return
     */
    public static String substring(String search, String start, String end) {
        return substring(search, start, end, "");
    }

    /**
     * 拼接字符串
     *
     * @param strs
     * @return
     */
    public static String concat(String... strs) {
        StringBuffer result = new StringBuffer();
        if (strs != null) {
            for (String str : strs) {
                if (str != null)
                    result.append(str);
            }
        }
        return result.toString();
    }

    /**
     * 获取中文字符个数
     */
    public static int getChineseCharCount(String str) {
        String tempStr;
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            tempStr = String.valueOf(str.charAt(i));
            if (tempStr.getBytes().length == 3) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取英文字符个数
     */
    public static int getEnglishCount(String str) {
        String tempStr;
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            tempStr = String.valueOf(str.charAt(i));
            if (!(tempStr.getBytes().length == 3)) {
                count++;
            }
        }
        return count;
    }

    public static String encode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return url;
    }

    public static String base64encode(String value) {
        try {
            return new String(Base64.encode(value.getBytes(), Base64.DEFAULT));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return value;
    }

    public static String decode(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return url;
    }

    public static String base64decode(String value) {
        try {

            return new String(Base64.decode(value, Base64.DEFAULT));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return value;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = payload.getBytes(utfEncoding);
        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return record;
    }

    /**
     * 转换中国币文字显示
     *
     * @param gold
     * @return
     */
    public static String generateGoldValue(int gold) {
        String goldstr;
        if (gold < 10000) {
            goldstr = gold + "中国币";
        } else if (gold < 100000000) {
            goldstr = String.format(Locale.getDefault(), "%.1f万中国币", gold / 10000);
        } else
            goldstr = String.format(Locale.getDefault(), "%.1f亿中国币", gold / 100000000);
        return goldstr;
    }

    /**
     * 数量转换为万
     */
    public static String getNumDiff(int count, String sub) {
        String afterNum = "";
        if (count >= 0 && count < 10000) {
            afterNum = count + sub;
        } else if (count >= 10000 && count < 10000000) {
            if (count / 1000 % 10 != 0) {
                float num = (float) count / 10000;
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                afterNum = decimalFormat.format(num).concat("万" + sub);
            } else {
                afterNum = (count / 10000) + "万" + sub;
            }
        } else if (count >= 10000000) {
            afterNum = "1000万" + sub;
        }
        return afterNum;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().length() == 0) return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    //    private static final char[] cnNumbers = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
    private static final char[] cnNumbers = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};
    //    private static final char[] series = {' ', '拾', '百', '仟', '万', '拾', '百', '仟', '亿'};
    private static final char[] series = {' ', '十', '百', '千', '万', '十', '百', '千', '亿'};

    /**
     * 取得大写形式的字符串
     */
    public static String getCnString(Integer value) {
        // 整数部分
        String integerPart = "";
        // 小数部分
        String floatPart = "";

        String original = value.toString();

        if (original.contains(".")) {
            // 如果包含小数点
            int dotIndex = original.indexOf(".");
            integerPart = original.substring(0, dotIndex);
            floatPart = original.substring(dotIndex + 1);
        } else {
            // 不包含小数点
            integerPart = original;
        }
        // 因为是累加所以用StringBuffer
        StringBuffer sb = new StringBuffer();

        // 整数部分处理
        for (int i = 0; i < integerPart.length(); i++) {
            int number = Integer.parseInt(String.valueOf(integerPart.charAt(i)));

            sb.append(cnNumbers[number]);
            sb.append(series[integerPart.length() - 1 - i]);
        }
        // 小数部分处理
        if (floatPart.length() > 0) {
            sb.append("点");
            for (int i = 0; i < floatPart.length(); i++) {
                int number = Integer.parseInt(String.valueOf(floatPart.charAt(i)));

                sb.append(cnNumbers[number]);
            }
        }

        // 返回拼接好的字符串
        return sb.toString().trim();
    }


}

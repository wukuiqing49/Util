package com.wu.base.util;

public class DoublePressed {
    private static long time_pressed;
    private static long upload_time;

    /**
     * 连续点击处理
     *
     * @return
     */
    public static boolean onDoublePressed() {
        return onDoublePressed(false, 2000L);
    }

    public static boolean onLongDoublePressed() {
        return onDoublePressed(true, 2000L);
    }

    public static boolean onDoublePressed(boolean isLong, long time) {
        long timeMillis = System.currentTimeMillis();
        boolean doublePressed = time_pressed + time > timeMillis;

        if (isLong) {
            time_pressed = timeMillis;
        } else if (upload_time < timeMillis) {
            upload_time = timeMillis + time;
            time_pressed = timeMillis;
        }

        return doublePressed;
    }

    public static void onRestart() {
        time_pressed = 0;
        upload_time = 0;
    }
}

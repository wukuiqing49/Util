package com.wkq.lib_base.utlis

/**
 *
 * 作者:吴奎庆
 *
 * 时间:2020/5/15
 *
 * 用途:
 */


class BackTowPressed {
    private var back_pressed: Long = 0

    /**
     * 两次点击事件判断
     *
     * @return
     */
    fun onBackPressed(): Boolean {
        if (back_pressed + 2000 > System.currentTimeMillis()) return true
        back_pressed = System.currentTimeMillis()
        return false
    }

    fun onRestart() {
        back_pressed = 0
    }
}
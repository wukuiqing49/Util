package com.wu.util

import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.DiffUtil


/**
 * @author wkq
 *
 * @date 2021年09月06日 14:33
 *
 *@des  异步差分对比的监听
 *
 */

class RecyclerViewDiffItemCallBack() :
    DiffUtil.ItemCallback<UserInfo>() {
    //是否相同的id
    override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        return oldItem.id!!.equals(oldItem.id)

    }
    //是否相同的数据
    override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        var oldItem = oldItem.toString()
        var newItem = newItem.toString()
        return oldItem.equals(newItem)
    }


    //  局部刷新  返回null 整条数据刷新
    override fun getChangePayload(oldItem: UserInfo, newItem: UserInfo): Any? {
        var bundle = Bundle()
        // onBindViewHolder  实现三个参数  payloads   第一个数据为  封装的bundle
        if (!TextUtils.equals(oldItem.name, newItem.name)) {
            bundle.putString("name", newItem.name)
        }
        if (!TextUtils.equals(oldItem.icon, newItem.icon)) {
            bundle.putString("icon", newItem.name)
        }
        if (!TextUtils.equals(oldItem.phoneNum, newItem.phoneNum)) {
            bundle.putString("phoneNum", newItem.phoneNum)
        }
        return bundle
    }


}
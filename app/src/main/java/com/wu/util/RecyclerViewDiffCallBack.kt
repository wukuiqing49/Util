package com.wu.util

import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.DiffUtil


/**
 * @author wkq
 *
 * @date 2021年09月06日 14:33
 *
 *@des
 *
 */

class RecyclerViewDiffCallBack(oldList: List<UserInfo>, newList: List<UserInfo>) : DiffUtil.Callback() {
    var oldList: List<UserInfo>? = null
    var newList: List<UserInfo>? = null

    init {
        this.oldList = oldList
        this.newList = newList
    }

    //旧数据集的长度；
    override fun getOldListSize(): Int {
        if (oldList.isNullOrEmpty()) {
            return 0
        } else {
            return oldList!!.size
        }
    }

    //获取新数据长度
    override fun getNewListSize(): Int {
        if (newList.isNullOrEmpty()) {
            return 0
        } else {
            return newList!!.size
        }
    }

    //判断是否是同一个item  为true 进入areContentsTheSame
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList == null || oldList == null) return false
        var oldItemId = oldList!![oldItemPosition].id
        var newItemId = newList!![newItemPosition].id
        return oldItemId!!.equals(newItemId)

    }

    // 如果item相同，此方法用于判断是否同一个 Item 的内容也相同  为true  进入 getChangePayload
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList == null || oldList == null) return false
        var oldItem = oldList!![oldItemPosition].toString()
        var newItem = newList!![newItemPosition].toString()
        return oldItem.equals(newItem)
    }

    //  局部刷新  返回null 整条数据刷新
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        var oldItem = oldList!![oldItemPosition]
        var newItem = newList!![newItemPosition]

        var bundle=Bundle()
           // onBindViewHolder  实现三个参数  payloads   第一个数据为  封装的bundle
        if (!TextUtils.equals(oldItem.name,newItem.name)){
            bundle.putString("name",newItem.name)
        }
        if (!TextUtils.equals(oldItem.icon,newItem.icon)){
            bundle.putString("icon",newItem.name)
        }
        if (!TextUtils.equals(oldItem.phoneNum,newItem.phoneNum)){
            bundle.putString("phoneNum",newItem.phoneNum)
        }
        return bundle
    }


}
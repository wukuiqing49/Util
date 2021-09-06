package com.wu.util

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

    //判断是否是同一个item
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList == null || oldList == null) return false
        var oldItemId = oldList!![oldItemPosition].id
        var newItemId = newList!![newItemPosition].id
        return oldItemId!!.equals(newItemId)

    }

    // 如果item相同，此方法用于判断是否同一个 Item 的内容也相同
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList == null || oldList == null) return false
        var oldItem = oldList!![oldItemPosition]
        var newItem = newList!![newItemPosition]
        return oldItem.toString().equals(newItem)
    }


    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }


}
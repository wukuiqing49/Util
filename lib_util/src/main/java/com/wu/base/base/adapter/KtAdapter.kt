package com.wu.base.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * 作者: 吴奎庆
 *
 * 时间: 2020/5/9
 *
 * 简介:  kt  的databinding adapter
 */
abstract class KtAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var mContext: Context

    var itemList= ArrayList<T>()



    constructor(context: Context) {
        this.mContext = context
    }
    override fun getItemCount(): Int {
       return  itemList.size
    }

    fun getItems(): ArrayList<T>? {
        return itemList
    }

    fun getItem(position: Int): T? {
        if (itemList != null && position < itemList!!.size) {
            return itemList!!.get(position)
        }
        return null
    }

    fun addItem(t: T) {

        addItem(this.itemList!!.size, t)
    }

    open fun addItem(index: Int, item: T?) {
        if (item != null) {
            this.itemList?.add(index, item)
            try {
                notifyItemInserted(index)
            } catch (var4: Exception) {
            }
        }
    }

    fun addItems(items: ArrayList<T>?) {
        if (items != null) {
            this.itemList?.addAll(items.asIterable())
            notifyDataSetChanged()
//            notifyItemRangeInserted(0,itemList!!.size-1)
//            notifyItemRangeInserted(0,itemList!!.size-1)
//            notifyItemRangeInsert()
        }

    }

    fun updateItems(items: List<T>?) {
        if (items != null) {
            this.itemList.clear()
            this.itemList.addAll(items)
            notifyDataSetChanged()
        }
    }

    fun removeItem(item: T) {
        this.itemList?.remove(item)
    }

    fun removeItems(items: ArrayList<T>?) {
        if (items != null) {
            this.itemList?.removeAll(items.asIterable())
        }
    }

    fun removeItem(index: Int) {
        this.itemList.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemList.size - index);
    }

    open fun removeAllItems() {
        this.itemList?.clear()
        notifyDataSetChanged()
    }

    var viewClickListener: OnAdapterViewClickListener<T>? = null

    fun setOnViewClickListener(listener: OnAdapterViewClickListener<T>) {
        viewClickListener = listener
    }

    interface OnAdapterViewClickListener<T> {
        fun onViewClick(v: View?, program: T?)
    }
}
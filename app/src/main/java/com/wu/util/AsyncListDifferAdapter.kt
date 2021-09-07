package com.wu.util

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wu.base.adapter.KtAdapter
import com.wu.base.base.adapter.KtDataBindingViewHolder
import com.wu.util.databinding.ItemMainOneBinding


/**
 * @author wkq
 *
 * @date 2021年09月06日 15:38
 *
 *@des
 *
 */

class AsyncListDifferAdapter(mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mContext: Context? = null
    var diff: AsyncListDiffer<UserInfo>? = null
    init {
        this.mContext = mContext
        diff=AsyncListDiffer<UserInfo>(this,RecyclerViewDiffItemCallBack())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var binding = DataBindingUtil.inflate<ItemMainOneBinding>(
            LayoutInflater.from(mContext),
            R.layout.item_main_one,
            parent,
            false
        )
        var holder = KtDataBindingViewHolder(binding.root)
        holder.binding = binding
        return holder

    }

    fun setDatas(newList: List<UserInfo>) {
        if(diff==null)return
        diff!!.submitList(newList)
    }

    /**
     * @param payloads A non-null list of merged payloads. Can be empty list if requires full
     *                update.
     */

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        // payloads  为null 整条数据刷新
        if (payloads.isEmpty() || payloads.size <= 0) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            //局部更新   不会更新整个item 更新指定的控件
            var bundle = payloads.get(0) as Bundle
            if (bundle != null) {
                var name = bundle.getString("name")
                var phoneNum = bundle.getString("phoneNum")
                var holder = holder as KtDataBindingViewHolder
                var binding = holder.binding as ItemMainOneBinding
                if (!TextUtils.isEmpty(name)) {
                    binding.tvName.text = name
                }
                if (!TextUtils.isEmpty(phoneNum)) {
                    binding.tvPhone.text = phoneNum
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("绘制:", "" + position)
        var holder = holder as KtDataBindingViewHolder
        var binding = holder.binding as ItemMainOneBinding
        binding.tvName.setText(getItem(position)!!.name)
        binding.tvPhone.setText(getItem(position)!!.phoneNum)
    }

    fun getItem(position: Int): UserInfo {
        return diff!!.currentList.get(position)
    }

    fun getItems():List<UserInfo>{
       return diff!!.currentList
    }

    override fun getItemCount(): Int {
        return diff!!.currentList.size
    }

}
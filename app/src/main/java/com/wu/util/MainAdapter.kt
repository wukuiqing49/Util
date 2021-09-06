package com.wu.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

class MainAdapter(mContext: Context) : KtAdapter<UserInfo>(mContext) {

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


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var holder = holder as KtDataBindingViewHolder

        var binding = holder.binding as ItemMainOneBinding

        binding.tvName.setText(getItem(position)!!.name)
        binding.tvPhone.setText(getItem(position)!!.phoneNum)

    }


}
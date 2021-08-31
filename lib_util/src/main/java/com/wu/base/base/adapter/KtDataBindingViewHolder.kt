package com.wu.base.base.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * 作者: 吴奎庆
 *
 *
 * 时间: 2019/12/18
 *
 *
 * 简介:
 */
class KtDataBindingViewHolder(itemView: View?) : RecyclerView.ViewHolder(
    itemView!!
) {
    var binding: ViewDataBinding? = null
}
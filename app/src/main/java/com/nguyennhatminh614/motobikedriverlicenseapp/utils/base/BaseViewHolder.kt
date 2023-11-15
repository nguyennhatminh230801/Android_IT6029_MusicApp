package com.nguyennhatminh614.motobikedriverlicenseapp.utils.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T, VB : ViewBinding>(
    open val binding: VB,
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun onBindData(data: T)

    open fun onBindData(data: T, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindData(data)
        }
    }
}

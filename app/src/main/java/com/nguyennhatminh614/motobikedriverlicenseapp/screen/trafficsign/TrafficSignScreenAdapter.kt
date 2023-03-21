package com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.TrafficSignCategory
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.TrafficSigns
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentTrafficSignCategoryBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder

class TrafficSignScreenAdapter :
    BaseRecyclerViewAdapter<MutableList<TrafficSigns>, FragmentTrafficSignCategoryBinding, TrafficSignScreenAdapter.ViewHolder>(
        TrafficSignCategoryScreenDiffUtil
    ) {

    private var itemClickEvent: OnClickItem<TrafficSigns>? = null

    fun setItemClickEvent(itemClickEvent: OnClickItem<TrafficSigns>) {
        this.itemClickEvent = itemClickEvent
    }

    companion object {
        val TrafficSignCategoryScreenDiffUtil = object : DiffUtil.ItemCallback<MutableList<TrafficSigns>>() {
            override fun areItemsTheSame(
                oldItem: MutableList<TrafficSigns>,
                newItem: MutableList<TrafficSigns>,
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MutableList<TrafficSigns>,
                newItem: MutableList<TrafficSigns>,
            ) = oldItem == newItem
        }
    }

    inner class ViewHolder(
        override val binding: FragmentTrafficSignCategoryBinding) :
        BaseViewHolder<MutableList<TrafficSigns>, FragmentTrafficSignCategoryBinding>(binding) {
        override fun onBindData(data: MutableList<TrafficSigns>) {
            val trafficSignAdapter = TrafficSignAdapter()
            binding.recyclerViewTrafficSign.adapter = trafficSignAdapter
            trafficSignAdapter.submitList(data)
            trafficSignAdapter.registerOnClickItemEvent {
                itemClickEvent?.invoke(it)
            }
        }
    }

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<MutableList<TrafficSigns>>) {
        //Not op
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentTrafficSignCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
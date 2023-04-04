package com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.TrafficSigns
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ItemTrafficSignLayoutBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.loadGlideImageFromUrl

class TrafficSignAdapter :
    BaseRecyclerViewAdapter<TrafficSigns, ItemTrafficSignLayoutBinding, TrafficSignAdapter.ViewHolder>(
        TrafficSigns.getDiffUtilCallback()) {

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<TrafficSigns>) {
        this.clickItemInterface = onClickItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflateViewBinding(ItemTrafficSignLayoutBinding::inflate, parent))
    }

    inner class ViewHolder(
        override val binding: ItemTrafficSignLayoutBinding,
    ) : BaseViewHolder<TrafficSigns, ItemTrafficSignLayoutBinding>(binding) {
        override fun onBindData(data: TrafficSigns) {
            binding.apply {
                imageTrafficSign.loadGlideImageFromUrl(
                    root.context,
                    data.imageUrl,
                )
                textTrafficSignTitle.text = data.title
                textTrafficSignDescription.text = data.description.ifBlank { "Không có giải thích!!" }

                root.setOnClickListener {
                    clickItemInterface?.let { function -> function(data) }
                }
            }
        }
    }
}

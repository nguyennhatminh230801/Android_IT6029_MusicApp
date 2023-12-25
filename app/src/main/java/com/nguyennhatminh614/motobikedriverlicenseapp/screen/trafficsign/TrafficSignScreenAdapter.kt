package com.nguyennhatminh614.motobikedriverlicenseapp.screen.trafficsign

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign.TrafficSigns
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentTrafficSignCategoryBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder

class TrafficSignScreenAdapter :
    BaseRecyclerViewAdapter<TrafficSignViewModel.TabTrafficSignUiState, FragmentTrafficSignCategoryBinding, TrafficSignScreenAdapter.ViewHolder>(
        TrafficSignCategoryScreenDiffUtil
    ) {

    private var itemClickEvent: OnClickItem<TrafficSigns>? = null
    private var onUpdateTabUiState: (TrafficSignViewModel.TabTrafficSignUiState) -> Unit = {}

    fun setItemClickEvent(itemClickEvent: OnClickItem<TrafficSigns>) {
        this.itemClickEvent = itemClickEvent
    }

    fun setOnUpdateTabUiState(callback: (TrafficSignViewModel.TabTrafficSignUiState) -> Unit) = apply {
        this.onUpdateTabUiState = callback
    }

    companion object {
        val TrafficSignCategoryScreenDiffUtil =
            object : DiffUtil.ItemCallback<TrafficSignViewModel.TabTrafficSignUiState>() {
                override fun areItemsTheSame(
                    oldItem: TrafficSignViewModel.TabTrafficSignUiState,
                    newItem: TrafficSignViewModel.TabTrafficSignUiState,
                ) = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: TrafficSignViewModel.TabTrafficSignUiState,
                    newItem: TrafficSignViewModel.TabTrafficSignUiState,
                ) = oldItem == newItem

                override fun getChangePayload(
                    oldItem: TrafficSignViewModel.TabTrafficSignUiState,
                    newItem: TrafficSignViewModel.TabTrafficSignUiState
                ): Any? {
                    val bundle = bundleOf()
                    if (oldItem.scrolledPosition != newItem.scrolledPosition) {
                        bundle.putInt(PAYLOAD_SCROLLED, newItem.scrolledPosition)
                    }
                    return bundle
                }
            }

        private const val PAYLOAD_SCROLLED = "PAYLOAD_SCROLLED"
    }

    inner class ViewHolder(
        override val binding: FragmentTrafficSignCategoryBinding,
    ) : BaseViewHolder<TrafficSignViewModel.TabTrafficSignUiState, FragmentTrafficSignCategoryBinding>(binding) {

        private val trafficSignAdapter = TrafficSignAdapter()

        override fun onBindData(data: TrafficSignViewModel.TabTrafficSignUiState) {
            binding.recyclerViewTrafficSign.adapter = trafficSignAdapter
            trafficSignAdapter.submitList(data.trafficSigns)

            (binding.recyclerViewTrafficSign.layoutManager as? LinearLayoutManager)?.scrollToPosition(data.scrolledPosition)

            trafficSignAdapter.registerOnClickItemEvent {
                val scrolledPosition =
                    (binding.recyclerViewTrafficSign.layoutManager as? LinearLayoutManager)
                        ?.findFirstCompletelyVisibleItemPosition() ?: 0
                onUpdateTabUiState(data.copy(scrolledPosition = scrolledPosition))
                itemClickEvent?.invoke(it)
            }
        }
    }

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<TrafficSignViewModel.TabTrafficSignUiState>) {
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
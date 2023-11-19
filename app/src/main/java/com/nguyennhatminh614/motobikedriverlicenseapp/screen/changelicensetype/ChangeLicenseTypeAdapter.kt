package com.nguyennhatminh614.motobikedriverlicenseapp.screen.changelicensetype

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.LicenseType
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ItemLicenseTypeScreenBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getCurrentThemeCardColor
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getCurrentThemeTextColor
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getSelectedColor


class ChangeLicenseTypeAdapter
    : BaseRecyclerViewAdapter<ChangeLicenseTypeViewModel.LicenseTypeItemUiState, ItemLicenseTypeScreenBinding,
        ChangeLicenseTypeAdapter.ViewHolder>(getDiffUtilCallback()) {


    override fun registerOnClickItemEvent(onClickItem: OnClickItem<ChangeLicenseTypeViewModel.LicenseTypeItemUiState>) {
        this.clickItemInterface = onClickItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(inflateViewBinding(ItemLicenseTypeScreenBinding::inflate, parent))

    fun setCurrentLicenseType(licenseType: LicenseType, notifySelectedPosition: (Int) -> Unit) {
        val oldSelectedPosition = currentList.indexOfFirst { it.isSelected }
        val newSelectedPosition = currentList.indexOfFirst { it.licenseType == licenseType }

        val newStateList = currentList.toMutableList()

        if (oldSelectedPosition != AppConstant.NONE_POSITION
            && oldSelectedPosition != newSelectedPosition) {

            newStateList[oldSelectedPosition] =
                newStateList[oldSelectedPosition].copy(isSelected = false)
            submitList(newStateList)
            notifyItemChanged(oldSelectedPosition)
        }

        newStateList[newSelectedPosition] =
            newStateList[newSelectedPosition].copy(isSelected = true)
        submitList(newStateList)
        notifyItemChanged(newSelectedPosition)
        notifySelectedPosition(newSelectedPosition)
    }

    companion object {
        fun getDiffUtilCallback() = object : DiffUtil.ItemCallback<ChangeLicenseTypeViewModel.LicenseTypeItemUiState>() {
            override fun areItemsTheSame(oldItem: ChangeLicenseTypeViewModel.LicenseTypeItemUiState, newItem: ChangeLicenseTypeViewModel.LicenseTypeItemUiState) =
                oldItem.licenseType.type == newItem.licenseType.type

            override fun areContentsTheSame(oldItem: ChangeLicenseTypeViewModel.LicenseTypeItemUiState, newItem: ChangeLicenseTypeViewModel.LicenseTypeItemUiState) =
                oldItem == newItem

            override fun getChangePayload(
                oldItem: ChangeLicenseTypeViewModel.LicenseTypeItemUiState,
                newItem: ChangeLicenseTypeViewModel.LicenseTypeItemUiState
            ): Any? {
                if (oldItem.isSelected != newItem.isSelected) {
                    return PAYLOAD_SELECTED
                }

                return super.getChangePayload(oldItem, newItem)
            }
        }

        private const val PAYLOAD_SELECTED = "PAYLOAD_SELECTED"
    }

    inner class ViewHolder(
        override val binding: ItemLicenseTypeScreenBinding,
    ) : BaseViewHolder<ChangeLicenseTypeViewModel.LicenseTypeItemUiState, ItemLicenseTypeScreenBinding>(binding) {

        override fun onBindData(data: ChangeLicenseTypeViewModel.LicenseTypeItemUiState) = with(binding) {
            textLicenseType.text = "Hạng ${data.licenseType.type}"
            textDescriptionLicenseType.text = data.licenseType.description
            setSelectedState(data)
        }

        override fun onBindData(data: ChangeLicenseTypeViewModel.LicenseTypeItemUiState, payloads: MutableList<Any>) {
            if (payloads.contains(PAYLOAD_SELECTED)) {
                setSelectedState(data)
            } else {
                super.onBindData(data, payloads)
            }

            binding.root.setOnClickListener {
                clickItemInterface?.invoke(data)
            }
        }

        private fun setSelectedState(data: ChangeLicenseTypeViewModel.LicenseTypeItemUiState) = with(binding) {
            if (data.isSelected) {
                textLicenseType.setTextColor(getSelectedColor(R.color.white))
                textDescriptionLicenseType.setTextColor(getSelectedColor(R.color.white))
                root.setCardBackgroundColor(getSelectedColor(R.color.primary_color))
            } else {
                val currentTextColor = getCurrentThemeTextColor()
                val currentBackgroundColor = getCurrentThemeCardColor()
                textLicenseType.setTextColor(currentTextColor)
                textDescriptionLicenseType.setTextColor(currentTextColor)
                root.setCardBackgroundColor(currentBackgroundColor)
            }
        }
    }
}
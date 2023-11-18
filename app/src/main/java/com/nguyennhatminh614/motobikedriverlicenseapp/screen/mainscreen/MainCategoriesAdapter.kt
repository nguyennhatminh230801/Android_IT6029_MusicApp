package com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ItemMainScreenCategoryLayoutBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.main.MainCategoryFactory
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.main.MainScreenViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder

class MainCategoriesAdapter :
    BaseRecyclerViewAdapter<MainScreenViewModel.CategoryItemUiState, ItemMainScreenCategoryLayoutBinding, MainCategoriesAdapter.ViewHolder>(
        MainCategoryFactory.createCategoriesDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainScreenCategoryLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    inner class ViewHolder(
        override val binding: ItemMainScreenCategoryLayoutBinding,
    ) : BaseViewHolder<MainScreenViewModel.CategoryItemUiState, ItemMainScreenCategoryLayoutBinding>(
        binding
    ) {
        override fun onBindData(item: MainScreenViewModel.CategoryItemUiState) {
            binding.imageCategory.setImageResource(item.thumbnailDrawId)
            binding.textCategory.text = binding.root.context.getString(item.titleStrId)
            binding.itemCategory.setOnClickListener {
                clickItemInterface?.invoke(item)
            }
        }
    }

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<MainScreenViewModel.CategoryItemUiState>) {
        this.clickItemInterface = onClickItem
    }
}

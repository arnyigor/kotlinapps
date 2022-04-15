package com.arny.kotlinapps.presentation.shoplist

import androidx.recyclerview.widget.DiffUtil
import com.arny.kotlinapps.domain.models.ShopItem

class ShopItemDiffUtilItemCallback : DiffUtil.ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean = oldItem == newItem
}
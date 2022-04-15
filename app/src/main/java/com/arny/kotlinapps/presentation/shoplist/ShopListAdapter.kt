package com.arny.kotlinapps.presentation.shoplist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.arny.kotlinapps.R
import com.arny.kotlinapps.domain.models.ShopItem

class ShopListAdapter(
    private val onLongItemClick: (item: ShopItem) -> Unit = {},
    private val onItemClick: (item: ShopItem) -> Unit = {}
) : ListAdapter<ShopItem, ShopListItemViewHolder>(ShopItemDiffUtilItemCallback()) {

    companion object {
        const val VIEW_ENABLED = 1
        const val VIEW_DISABLED = 0
        const val MAX_POOL_SIZE = 15
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListItemViewHolder {
        val itemShopDisabled = when (viewType) {
            VIEW_DISABLED -> R.layout.item_shop_disabled
            VIEW_ENABLED -> R.layout.item_shop_enabled
            else -> error("Unknown view type $viewType")
        }
        return ShopListItemViewHolder(
            LayoutInflater.from(parent.context).inflate(itemShopDisabled, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ShopListItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        with(holder) {
            tvName.text = shopItem.name
            tvCount.text = shopItem.count.toString()
            view.setOnClickListener {
                onItemClick(shopItem)
            }
            view.setOnLongClickListener {
                onLongItemClick(shopItem)
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int = if (getItem(position).enabled) VIEW_ENABLED else VIEW_DISABLED
}
package com.arny.kotlinapps.domain.repositories

import androidx.lifecycle.LiveData
import com.arny.kotlinapps.domain.models.ShopItem

interface ShopItemsRepository {
    fun addShopItem(item: ShopItem)
    fun editShopItem(shopItem: ShopItem)
    fun getShopItem(shopItemId: Long): ShopItem
    fun getShopItems(): LiveData<List<ShopItem>>
    fun deleteShopItem(item: ShopItem)
}
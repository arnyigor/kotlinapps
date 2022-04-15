package com.arny.kotlinapps.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.domain.repositories.ShopItemsRepository

object ShopItemsRepositoryImpl : ShopItemsRepository {
    private val shopItems = mutableListOf<ShopItem>()
    private var autoIncrement: Long = 0
    private val shopListLD = MutableLiveData<List<ShopItem>>()

    init {
        for (i in 0 until 3) {
            addShopItem(
                ShopItem(
                    name = "Name $i",
                    count = i,
                    enabled = true,
                )
            )
        }
    }

    override fun addShopItem(item: ShopItem) {
        if (item.id == ShopItem.UNDEFINED_ID) {
            item.id = autoIncrement++
        }
        shopItems.add(item)
        updateLiveData()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        val oldIndex = shopItems.indexOf(oldItem)
        shopItems.remove(oldItem)
        shopItems.add(oldIndex, shopItem)
        updateLiveData()
    }

    override fun getShopItem(shopItemId: Long): ShopItem =
        shopItems.find { it.id == shopItemId } ?: error("Element with id $shopItemId not found")

    override fun getShopItems(): LiveData<List<ShopItem>> = shopListLD

    override fun deleteShopItem(item: ShopItem) {
        shopItems.remove(item)
        updateLiveData()
    }

    private fun updateLiveData() {
        shopListLD.value = shopItems.toList()
    }
}
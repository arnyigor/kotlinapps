package com.arny.kotlinapps.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.domain.repositories.ShopItemsRepository
import kotlin.random.Random

object ShopItemsRepositoryImpl : ShopItemsRepository {
    private val shopItems = sortedSetOf<ShopItem>({ item1, item2 -> item1.id.compareTo(item2.id) })
    private var autoIncrement: Long = 0
    private val shopListLD = MutableLiveData<List<ShopItem>>()

    init {
        for (i in 0 until 1000) {
            addShopItem(
                ShopItem(
                    name = "Name $i",
                    count = i,
                    enabled = Random.nextBoolean(),
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
        shopItems.remove(oldItem)
        shopItems.add(shopItem)
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
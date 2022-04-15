package com.arny.kotlinapps.domain.usecases

import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.domain.repositories.ShopItemsRepository

class AddShopItemUseCase(
    private val shopItemsRepository: ShopItemsRepository
) {
    fun addShopItem(item: ShopItem) {
        shopItemsRepository.addShopItem(item)
    }
}
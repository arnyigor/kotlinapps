package com.arny.kotlinapps.domain.usecases

import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.domain.repositories.ShopItemsRepository

class DeleteShopItemUseCase(
    private val shopItemsRepository: ShopItemsRepository
) {
    fun deleteShopItem(item: ShopItem) {
        shopItemsRepository.deleteShopItem(item)
    }
}
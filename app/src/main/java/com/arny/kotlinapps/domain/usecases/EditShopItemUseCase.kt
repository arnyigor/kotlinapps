package com.arny.kotlinapps.domain.usecases

import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.domain.repositories.ShopItemsRepository

class EditShopItemUseCase(
    private val shopItemsRepository: ShopItemsRepository
) {
    fun editShopItem(shopItem: ShopItem) {
        shopItemsRepository.editShopItem(shopItem)
    }
}
package com.arny.kotlinapps.domain.usecases

import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.domain.repositories.ShopItemsRepository

class GetShopItemByIdUseCase(
    private val shopItemsRepository: ShopItemsRepository
) {
    fun getShopItem(shopItemId: Long): ShopItem = shopItemsRepository.getShopItem(shopItemId)
}
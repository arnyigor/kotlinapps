package com.arny.kotlinapps.domain.usecases

import androidx.lifecycle.LiveData
import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.domain.repositories.ShopItemsRepository

class GetShopItemsUseCase(
    private val shopItemsRepository: ShopItemsRepository
) {
    fun getShopItems(): LiveData<List<ShopItem>> = shopItemsRepository.getShopItems()
}
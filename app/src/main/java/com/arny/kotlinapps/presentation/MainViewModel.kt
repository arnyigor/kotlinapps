package com.arny.kotlinapps.presentation

import androidx.lifecycle.ViewModel
import com.arny.kotlinapps.data.ShopItemsRepositoryImpl
import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.domain.usecases.DeleteShopItemUseCase
import com.arny.kotlinapps.domain.usecases.EditShopItemUseCase
import com.arny.kotlinapps.domain.usecases.GetShopItemsUseCase

class MainViewModel : ViewModel() {
    private val shopItemsRepository = ShopItemsRepositoryImpl

    private val getShopItemsUseCase = GetShopItemsUseCase(shopItemsRepository)
    private val deleteShopItemsUseCase = DeleteShopItemUseCase(shopItemsRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopItemsRepository)

    val shopList = getShopItemsUseCase.getShopItems()

    fun toggleShopItemEnabled(item: ShopItem) {
        editShopItemUseCase.editShopItem(item.copy(enabled = !item.enabled))
    }

    fun deleteShopItem(item: ShopItem) {
        deleteShopItemsUseCase.deleteShopItem(item)
    }

}
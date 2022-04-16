package com.arny.kotlinapps.presentation.shopitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arny.kotlinapps.data.ShopItemsRepositoryImpl
import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.domain.usecases.AddShopItemUseCase
import com.arny.kotlinapps.domain.usecases.EditShopItemUseCase
import com.arny.kotlinapps.domain.usecases.GetShopItemByIdUseCase

class ShopItemViewModel : ViewModel() {

    private companion object {
        const val COUNT_UNKNOWN = -1
    }

    private val shopItemsRepository = ShopItemsRepositoryImpl
    private val getShopItemUseCase = GetShopItemByIdUseCase(shopItemsRepository)
    private val addShopItemUseCase = AddShopItemUseCase(shopItemsRepository)
    private val edtShopItemUseCase = EditShopItemUseCase(shopItemsRepository)

    private val _closeScreen = MutableLiveData<Unit>()
    val closeScreen: LiveData<Unit> = _closeScreen

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean> = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean> = _errorInputCount

    private val _item = MutableLiveData<ShopItem>()
    val item: LiveData<ShopItem> = _item

    fun getShopItem(shoItemId: Long) {
        _item.value = getShopItemUseCase.getShopItem(shoItemId)
    }

    fun addShopItem(
        inputName: String?,
        inputCount: String?,
    ) {
        val parsedName = parseName(inputName)
        val parsedCount = parseCount(inputCount)
        val fieldsValid = validateInputs(parsedName, parsedCount)
        if (fieldsValid) {
            addShopItemUseCase.addShopItem(ShopItem(parsedName, parsedCount, true))
            closeEditScreen()
        }
    }

    private fun closeEditScreen() {
        _closeScreen.value = Unit
    }

    fun editShopItem(
        inputName: String?,
        inputCount: String?,
    ) {
        val parsedName = parseName(inputName)
        val parsedCount = parseCount(inputCount)
        val fieldsValid = validateInputs(parsedName, parsedCount)
        if (fieldsValid) {
            _item.value?.let { shopItem ->
                edtShopItemUseCase.editShopItem(
                    shopItem.copy(
                        name = parsedName,
                        count = parsedCount
                    )
                )
                closeEditScreen()
            }
        }
    }

    fun resetErrorInputName() {
        if (_errorInputName.value == true) {
            _errorInputName.value = false
        }
    }

    fun resetErrorInputCount() {
        if (_errorInputCount.value == true) {
            _errorInputCount.value = false
        }
    }

    private fun parseName(name: String?): String = name?.trim().orEmpty()

    private fun parseCount(inputCount: String?): Int = inputCount?.toIntOrNull() ?: COUNT_UNKNOWN

    private fun validateInputs(name: String, count: Int): Boolean {
        var result = name.isNotBlank() && count > COUNT_UNKNOWN
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= COUNT_UNKNOWN) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

}
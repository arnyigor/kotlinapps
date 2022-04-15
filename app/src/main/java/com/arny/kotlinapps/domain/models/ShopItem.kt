package com.arny.kotlinapps.domain.models

data class ShopItem(
    val name: String,
    val count: Int,
    val enabled: Boolean,
    var id: Long = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = -1L
    }
}

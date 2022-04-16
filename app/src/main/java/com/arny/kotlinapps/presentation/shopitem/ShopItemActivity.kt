package com.arny.kotlinapps.presentation.shopitem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arny.kotlinapps.R
import com.arny.kotlinapps.domain.models.ShopItem

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnShopItemEditFinish {

    companion object {
        private const val EXTRA_SCREEN_MODE = "EXTRA_SCREEN_MODE"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_EDIT = "MODE_EDIT"
        private const val MODE_UNKNOWN = ""
        private const val EXTRA_SHOPITEM_ID = "EXTRA_SHOPITEM_ID"
        fun createAddIntent(context: Context): Intent =
            Intent(context, ShopItemActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            }

        fun createEditIntent(context: Context, shopItemId: Long): Intent =
            Intent(context, ShopItemActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
                putExtra(EXTRA_SHOPITEM_ID, shopItemId)
            }
    }

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Long = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        if (savedInstanceState == null) {
            initScreenMode()
        }
    }

    private fun initScreenMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEdit(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAdd()
            else -> error("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            error("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (MODE_ADD != mode && MODE_EDIT != mode) {
            error("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT && !intent.hasExtra(EXTRA_SHOPITEM_ID)) {
            error("Param shopItemId is absent in edit mode")
        }
        if (screenMode == MODE_EDIT) {
            shopItemId = intent.getLongExtra(EXTRA_SHOPITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    override fun onEditFinish() {
         onBackPressed()
    }
}
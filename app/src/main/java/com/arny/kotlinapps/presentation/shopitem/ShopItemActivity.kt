package com.arny.kotlinapps.presentation.shopitem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.arny.kotlinapps.R
import com.arny.kotlinapps.domain.models.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {

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

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout

    private lateinit var tiedtName: TextInputEditText
    private lateinit var tilCount: TextInputLayout
    private lateinit var tiedtCount: TextInputEditText
    private lateinit var btnSave: Button

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Long = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        setupView()
        observeItem()
        observeCountError()
        observeNameError()
        observeCloseScreen()
        initScreenMode()
    }

    private fun initScreenMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchAddMode() {
        setTitle(R.string.shopitem_add_mode)
        btnSave.setOnClickListener {
            viewModel.addShopItem(tiedtName.text.toString(), tiedtCount.text.toString())
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        setTitle(R.string.shopitem_edit_mode)
        btnSave.setOnClickListener {
            viewModel.editShopItem(tiedtName.text.toString(), tiedtCount.text.toString())
        }
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

    private fun observeCloseScreen() {
        viewModel.closeScreen.observe(this) {
            onBackPressed()
        }
    }

    private fun observeCountError() {
        viewModel.errorInputCount.observe(this) { hasError ->
            tilCount.error = getString(R.string.error_input_count).takeIf { hasError }
        }
    }

    private fun observeNameError() {
        viewModel.errorInputName.observe(this) { hasError ->
            tilName.error = getString(R.string.error_input_name).takeIf { hasError }
        }
    }

    private fun observeItem() {
        viewModel.item.observe(this) { item ->
            tiedtName.setText(item.name)
            tiedtCount.setText(item.count.toString())
        }
    }

    private fun setupView() {
        tilName = findViewById(R.id.tilName)
        tilCount = findViewById(R.id.tilCount)
        tiedtName = findViewById(R.id.tiedtName)
        tiedtCount = findViewById(R.id.tiedtCount)
        btnSave = findViewById(R.id.btnSave)
        tiedtName.doAfterTextChanged {
            viewModel.resetErrorInputName()
        }
        tiedtCount.doAfterTextChanged {
            viewModel.resetErrorInputCount()
        }
    }
}
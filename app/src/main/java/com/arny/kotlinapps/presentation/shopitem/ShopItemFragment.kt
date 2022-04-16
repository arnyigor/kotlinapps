package com.arny.kotlinapps.presentation.shopitem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arny.kotlinapps.R
import com.arny.kotlinapps.domain.models.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {

    companion object {
        private const val EXTRA_SCREEN_MODE = "EXTRA_SCREEN_MODE"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_EDIT = "MODE_EDIT"
        private const val MODE_UNKNOWN = ""
        private const val EXTRA_SHOPITEM_ID = "EXTRA_SHOPITEM_ID"
        fun newInstanceAdd(): ShopItemFragment = ShopItemFragment().apply {
            arguments = bundleOf(EXTRA_SCREEN_MODE to MODE_ADD)
        }

        fun newInstanceEdit(shopItemId: Long): ShopItemFragment = ShopItemFragment().apply {
            arguments = bundleOf(
                EXTRA_SCREEN_MODE to MODE_EDIT,
                EXTRA_SHOPITEM_ID to shopItemId,
            )
        }
    }

    interface OnShopItemEditFinish {
        fun onEditFinish()
    }

    private lateinit var onShopItemEditFinish: OnShopItemEditFinish

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout

    private lateinit var tiedtName: TextInputEditText
    private lateinit var tilCount: TextInputLayout
    private lateinit var tiedtCount: TextInputEditText
    private lateinit var btnSave: Button

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Long = ShopItem.UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onShopItemEditFinish =
            (context as? OnShopItemEditFinish) ?: error("Activity mast implement OnShopItemEditFinish")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_shop_item, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        setupView(view)
        observeItem()
        observeCountError()
        observeNameError()
        observeCloseScreen()
        initScreenMode()
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (!args.containsKey(EXTRA_SCREEN_MODE)) {
            error("Param screen mode is absent")
        }
        val mode = args.getString(EXTRA_SCREEN_MODE)
        if (MODE_ADD != mode && MODE_EDIT != mode) {
            error("Unknown screen mode $mode")
        }
        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(EXTRA_SHOPITEM_ID)) {
                error("Param shopItemId is absent in edit mode")
            }
            shopItemId = args.getLong(EXTRA_SHOPITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initScreenMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchAddMode() {
        activity?.setTitle(R.string.shopitem_add_mode)
        btnSave.setOnClickListener {
            viewModel.addShopItem(tiedtName.text.toString(), tiedtCount.text.toString())
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        activity?.setTitle(R.string.shopitem_edit_mode)
        btnSave.setOnClickListener {
            viewModel.editShopItem(tiedtName.text.toString(), tiedtCount.text.toString())
        }
    }

    private fun observeCloseScreen() {
        viewModel.closeScreen.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(R.string.success), Toast.LENGTH_SHORT).show()
            onShopItemEditFinish.onEditFinish()
        }
    }

    private fun observeCountError() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) { hasError ->
            tilCount.error = getString(R.string.error_input_count).takeIf { hasError }
        }
    }

    private fun observeNameError() {
        viewModel.errorInputName.observe(viewLifecycleOwner) { hasError ->
            tilName.error = getString(R.string.error_input_name).takeIf { hasError }
        }
    }

    private fun observeItem() {
        viewModel.item.observe(viewLifecycleOwner) { item ->
            tiedtName.setText(item.name)
            tiedtCount.setText(item.count.toString())
        }
    }

    private fun setupView(view: View) {
        tilName = view.findViewById(R.id.tilName)
        tilCount = view.findViewById(R.id.tilCount)
        tiedtName = view.findViewById(R.id.tiedtName)
        tiedtCount = view.findViewById(R.id.tiedtCount)
        btnSave = view.findViewById(R.id.btnSave)
        tiedtName.doAfterTextChanged {
            viewModel.resetErrorInputName()
        }
        tiedtCount.doAfterTextChanged {
            viewModel.resetErrorInputCount()
        }
    }
}
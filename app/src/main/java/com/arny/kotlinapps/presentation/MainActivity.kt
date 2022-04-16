package com.arny.kotlinapps.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arny.kotlinapps.R
import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.presentation.shopitem.ShopItemActivity
import com.arny.kotlinapps.presentation.shopitem.ShopItemFragment
import com.arny.kotlinapps.presentation.shoplist.MainViewModel
import com.arny.kotlinapps.presentation.shoplist.ShopListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(),ShopItemFragment.OnShopItemEditFinish {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isLandMode()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupViews()
        observeItems()
        setupAdapter()
    }

    private fun isLandMode(): Boolean = findViewById<FragmentContainerView>(R.id.shop_item_container) != null

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onEditFinish() {
        supportFragmentManager.popBackStack()
    }

    private fun setupViews() {
        findViewById<FloatingActionButton>(R.id.fabAddShopItem).setOnClickListener {
            if (isLandMode()) {
                launchFragment(ShopItemFragment.newInstanceAdd())
            } else {
                startActivity(ShopItemActivity.createAddIntent(this))
            }
        }
    }

    private fun observeItems() {
        viewModel.shopList.observe(this) { items ->
            showList(items)
        }
    }

    private fun showList(list: List<ShopItem>) {
        shopListAdapter.submitList(list)
    }

    private fun setupAdapter() {
        createAdapter()
        val callBack: ItemTouchHelper.SimpleCallback = getSwipeCallback()
        with(findViewById<RecyclerView>(R.id.rvShopItems)) {
            adapter = shopListAdapter
            ItemTouchHelper(callBack).attachToRecyclerView(this)
            recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_ENABLED, ShopListAdapter.MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_DISABLED, ShopListAdapter.MAX_POOL_SIZE)
        }
    }

    private fun createAdapter() {
        shopListAdapter = ShopListAdapter(
            onItemClick = { onItemClick(it) },
            onLongItemClick = { item -> onLongItemClick(item) }
        )
    }

    private fun onLongItemClick(item: ShopItem) {
        viewModel.toggleShopItemEnabled(item)
    }

    private fun onItemClick(item: ShopItem) {
        if (isLandMode()) {
            launchFragment(ShopItemFragment.newInstanceEdit(item.id))
        } else {
            startActivity(ShopItemActivity.createEditIntent(this, item.id))
        }
    }

    private fun getSwipeCallback(): ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteShopItem(shopListAdapter.currentList[viewHolder.adapterPosition])
            }
        }
}
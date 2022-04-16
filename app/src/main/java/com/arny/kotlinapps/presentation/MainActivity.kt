package com.arny.kotlinapps.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arny.kotlinapps.R
import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.presentation.shopitem.ShopItemActivity
import com.arny.kotlinapps.presentation.shoplist.MainViewModel
import com.arny.kotlinapps.presentation.shoplist.ShopListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupViews()
        observeItems()
        setupAdapter()
    }

    private fun setupViews() {
        val fab = findViewById<FloatingActionButton>(R.id.fabAddShopItem)
        fab.setOnClickListener {
            startActivity(ShopItemActivity.createAddIntent(this))
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

    private fun onItemClick(it: ShopItem) {
        startActivity(ShopItemActivity.createEditIntent(this, it.id))
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
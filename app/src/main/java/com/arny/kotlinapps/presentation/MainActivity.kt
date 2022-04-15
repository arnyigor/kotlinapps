package com.arny.kotlinapps.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arny.kotlinapps.R
import com.arny.kotlinapps.domain.models.ShopItem
import com.arny.kotlinapps.presentation.shoplist.MainViewModel
import com.arny.kotlinapps.presentation.shoplist.ShopListAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) { items ->
            showList(items)
        }
        setupAdapter()
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
        Log.i(MainActivity::class.java.simpleName, "initAdapter: item clicked:$it")
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
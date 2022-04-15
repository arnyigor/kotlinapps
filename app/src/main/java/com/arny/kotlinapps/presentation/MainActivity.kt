package com.arny.kotlinapps.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.arny.kotlinapps.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    var count: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) { items ->
            Log.i(MainActivity::class.java.simpleName, "onCreate: shopList:$items")
            if (count++ == 0) {
                viewModel.deleteShopItem(items[0])
            }
        }
    }
}
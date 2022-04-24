package com.arny.kotlinapps.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.arny.kotlinapps.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
        observeState()
        binding.mainContainer.setOnClickListener {
            viewModel.reload()
        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenCreated {
            viewModel.loading.collectLatest {
                binding.pbProgress.isVisible = it
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.info.collectLatest {
                if (it.isNotBlank()) {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.result.collectLatest { result ->
                println("result:$result")
                with(binding.tvInfo) {
                    isVisible = !result.isNullOrBlank()
                    text = result
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.progress.collectLatest { progress ->
                binding.pbProgressLinear.progress = progress
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.error.collectLatest { err ->
                with(binding.tvInfo) {
                    isVisible = err.isNotBlank()
                    text = err
                }
            }
        }
    }
}
package com.arny.kotlinapps.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arny.kotlinapps.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    companion object {
        fun newInstance() = GameFragment()
    }

    private var binding: FragmentGameBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inflate = FragmentGameBinding.inflate(inflater, container, false)
        binding = inflate
        return inflate.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let {
            with(it) {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
package com.arny.kotlinapps.presentation.chooselevel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arny.kotlinapps.databinding.FragmentChooseLevelBinding
import com.arny.kotlinapps.domain.entity.Level

class ChooseLevelFragment : Fragment() {

    private var binding: FragmentChooseLevelBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inflate = FragmentChooseLevelBinding.inflate(inflater, container, false)
        binding = inflate
        return inflate.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let {
            with(it) {
                btnTestLevel.setOnClickListener {
                    launchGame(Level.TEST)
                }
                btnEasyLevel.setOnClickListener {
                    launchGame(Level.EASY)
                }
                btNormalLevel.setOnClickListener {
                    launchGame(Level.NORMAL)
                }
                btnHardLevel.setOnClickListener {
                    launchGame(Level.HARD)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun launchGame(level: Level) {
        findNavController().navigate(
            ChooseLevelFragmentDirections.actionChooseLevelFragmentToGameFragment(level)
        )
    }
}
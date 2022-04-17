package com.arny.kotlinapps.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arny.kotlinapps.R

class ChooseLevelFragment private constructor() : Fragment() {
    companion object {
        fun newInstance() = ChooseLevelFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_choose_level, container, false)
}
package com.arny.kotlinapps.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arny.kotlinapps.R

class GameFinishFragment private constructor() : Fragment() {
    companion object {
        fun newInstance() = GameFinishFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_finish, container, false)
    }

}
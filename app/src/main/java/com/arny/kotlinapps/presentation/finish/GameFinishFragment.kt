package com.arny.kotlinapps.presentation.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.arny.kotlinapps.R
import com.arny.kotlinapps.databinding.FragmentGameFinishBinding
import com.arny.kotlinapps.domain.entity.GameResult
import com.arny.kotlinapps.domain.entity.Level
import com.arny.kotlinapps.presentation.game.GameFragment
import java.util.*

class GameFinishFragment : Fragment() {
    companion object {
        private const val PARAM_GAME_RESULT = "PARAM_GAME_RESULT"
        fun newInstance(gameResult: GameResult) = GameFinishFragment().apply {
            arguments = bundleOf(
                PARAM_GAME_RESULT to gameResult
            )
        }
    }

    private lateinit var gameResult: GameResult
    private var binding: FragmentGameFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inflate = FragmentGameFinishBinding.inflate(inflater, container, false)
        binding = inflate
        return inflate.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        handleBackPress()
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                launchChooseLevelFragment()
            }
        })
    }

    private fun initUI() {
        binding?.let {
            with(it) {
                btnTryAgain.setOnClickListener {
                    launchChooseLevelFragment()
                }
                setGameResult()
            }
        }
    }

    private fun FragmentGameFinishBinding.setGameResult() {
        tvGameResult.setText(if (gameResult.winner) R.string.winner_result else R.string.won_result)
        tvGameResultDescription.text = String.format(
            Locale.getDefault(),
            "%s\n%s\n%s\n%s\n%s",
            getString(R.string.level, getString(getLevelRes(gameResult.level))),
            getString(R.string.require_right_answers, gameResult.gameSettings.minCountOfRightAnswers),
            getString(R.string.right_answers, gameResult.countOfRightAnswers),
            getString(R.string.need_right_answers_percent, gameResult.gameSettings.minPercentOrRightAnswers),
            getString(R.string.answers_percent, gameResult.percentOfRightAnswers),
        )
    }

    @StringRes
    private fun getLevelRes(level: Level): Int = when (level) {
        Level.TEST -> R.string.test_level
        Level.EASY -> R.string.easy_level
        Level.NORMAL -> R.string.normal_level
        Level.HARD -> R.string.hard_level
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun parseArgs() {
        gameResult = requireArguments().getParcelable<GameResult>(PARAM_GAME_RESULT) as GameResult
    }

    private fun launchChooseLevelFragment() {
        requireActivity().supportFragmentManager.popBackStack(GameFragment.BACK_STACK_KEY, POP_BACK_STACK_INCLUSIVE)
    }

}
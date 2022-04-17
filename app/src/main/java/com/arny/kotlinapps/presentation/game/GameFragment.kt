package com.arny.kotlinapps.presentation.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arny.kotlinapps.R
import com.arny.kotlinapps.databinding.FragmentGameBinding
import com.arny.kotlinapps.domain.entity.GameResult
import com.arny.kotlinapps.domain.entity.Level
import com.arny.kotlinapps.presentation.finish.GameFinishFragment

class GameFragment : Fragment() {
    companion object {
        const val BACK_STACK_KEY = "GameFragment"
        private const val PARAM_LEVEL = "PARAM_LEVEL"
        fun newInstance(level: Level) = GameFragment().apply {
            arguments = bundleOf(
                PARAM_LEVEL to level
            )
        }
    }

    private lateinit var viewModel: GameViewModel

    private var binding: FragmentGameBinding? = null

    private lateinit var level: Level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

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
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        initUI()
        viewModel.loadGame(level = level)
    }

    private fun initUI() {
        binding?.let {
            with(it) {
                setListeners()
                observeData()
            }
        }
    }

    private fun FragmentGameBinding.observeData() {
        viewModel.question.observe(viewLifecycleOwner) { question ->
            tvSum.text = question.sum.toString()
            tvLeftNum.text = question.visibleNumber.toString()
            listOf(tvOption1, tvOption2, tvOption3, tvOption4, tvOption5, tvOption6)
                .onEachIndexed { index, textView ->
                    textView.text = question.options[index].toString()
                }
        }
        viewModel.formattedTime.observe(viewLifecycleOwner) { time ->
            tvTimer.text = time
        }
        viewModel.answersData.observe(viewLifecycleOwner) { (right, min) ->
            tvAnswersProgress.text = getString(R.string.right_answers, right, min)
        }
    }

    private fun FragmentGameBinding.setListeners() {
        listOf(tvOption1, tvOption2, tvOption3, tvOption4, tvOption5, tvOption6).onEach { textView ->
            textView.setOnClickListener {
                val text = textView.text.toString()
                viewModel.chooseAnswer(text)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun launchGameResult(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    private fun parseArgs() {
        level = requireArguments().getParcelable<Level>(PARAM_LEVEL) as Level
    }
}

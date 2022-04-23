package com.arny.kotlinapps.presentation.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arny.kotlinapps.databinding.FragmentGameBinding
import com.arny.kotlinapps.domain.entity.GameResult

class GameFragment : Fragment() {
    private val args by navArgs<GameFragmentArgs>()

    private val viewModelFactory by lazy {
        GameViewModelFactory(args.level)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
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
        initUI()
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
        viewModel.progressAnswersText.observe(viewLifecycleOwner) { wrapped ->
            tvAnswersProgress.text = wrapped.toString(requireContext())
        }
        viewModel.rightAnswersPercent.observe(viewLifecycleOwner) { progress ->
            progressAnswers.progress = progress
        }
        viewModel.minPercent.observe(viewLifecycleOwner) { progress ->
            progressAnswers.secondaryProgress = progress
        }
        viewModel.enoughCount.observe(viewLifecycleOwner) { enough ->
            tvAnswersProgress.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    getProgressColor(enough)
                )
            )
        }
        viewModel.enoughPercent.observe(viewLifecycleOwner) { enough ->
            DrawableCompat.setTint(
                progressAnswers.indeterminateDrawable,
                ContextCompat.getColor(
                    requireContext(),
                    getProgressColor(enough)
                )
            )
        }
        viewModel.gameResult.observe(viewLifecycleOwner) { result ->
            launchGameResult(result)
        }
    }

    @ColorRes
    private fun getProgressColor(enough: Boolean) =
        if (enough) android.R.color.holo_green_light else android.R.color.holo_red_dark

    private fun FragmentGameBinding.setListeners() {
        listOf(tvOption1, tvOption2, tvOption3, tvOption4, tvOption5, tvOption6).onEach { textView ->
            textView.setOnClickListener {
                textView.text.toString().toIntOrNull()?.let {
                    viewModel.chooseAnswer(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun launchGameResult(gameResult: GameResult) {
        findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameFinishFragment(gameResult))
    }
}

package com.arny.kotlinapps.presentation.game

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arny.kotlinapps.R
import com.arny.kotlinapps.domain.entity.GameResult
import com.arny.kotlinapps.domain.entity.GameSettings
import com.arny.kotlinapps.domain.entity.Level
import com.arny.kotlinapps.domain.entity.Question
import com.arny.kotlinapps.domain.usecases.GenerateQuestionUseCase
import com.arny.kotlinapps.domain.usecases.GetGameSettingsUseCase
import com.arny.kotlinapps.presentation.utils.IWrappedString
import com.arny.kotlinapps.presentation.utils.ResourceString
import java.util.*

class GameViewModel : ViewModel() {

    companion object {
        const val ZERO_TIME = 0L
        const val MILLIS_IN_SECONDS = 1000L
        const val SEC_IN_MIN = 60
    }

    private val generateQuestionUseCase: GenerateQuestionUseCase = GenerateQuestionUseCase()
    private val getGameSettingsUseCase: GetGameSettingsUseCase = GetGameSettingsUseCase()

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level

    private var timer: CountDownTimer? = null
    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String> = _formattedTime

    private val _progressAnswersText = MutableLiveData<IWrappedString>()
    val progressAnswersText: LiveData<IWrappedString> = _progressAnswersText

    private val _rightAnswersPercent = MutableLiveData<Int>()
    val rightAnswersPercent: LiveData<Int> = _rightAnswersPercent

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean> = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean> = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int> = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult> = _gameResult

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question> = _question

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    fun loadGame(level: Level) {
        getGameSettings(level)
        generateQuestion()
        startTimer()
    }

    fun chooseAnswer(num: Int) {
        checkAnswer(num)
        updateProgress()
        generateQuestion()
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSum)
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOrRightAnswers
    }

    private fun startTimer() {
        _formattedTime.value = formatTime(ZERO_TIME)
        val timeLong = gameSettings.timeInSec * MILLIS_IN_SECONDS
        timer = object : CountDownTimer(timeLong, MILLIS_IN_SECONDS) {
            override fun onTick(time: Long) {
                _formattedTime.value = formatTime(time)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun formatTime(timeInMs: Long): String {
        val time = timeInMs / MILLIS_IN_SECONDS
        val min = time / SEC_IN_MIN
        val sec = time % SEC_IN_MIN
        return String.format(Locale.getDefault(), "%02d:%02d", min, sec)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            winner = _enoughCount.value == true && _enoughPercent.value == true,
            countOfRightAnswers = countOfRightAnswers,
            countOfQuestions = countOfQuestions,
            percentOfRightAnswers = _rightAnswersPercent.value ?: 0,
            gameSettings = gameSettings,
            level = level
        )
    }

    private fun checkAnswer(num: Int) {
        if (num == _question.value?.rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun updateProgress() {
        val percent = calcPercentOfRightAnswers()
        val minRightAnswers = gameSettings.minCountOfRightAnswers
        val minPercentOrRightAnswers = gameSettings.minPercentOrRightAnswers
        _enoughCount.value = countOfRightAnswers >= minRightAnswers
        _enoughPercent.value = percent >= minPercentOrRightAnswers
        _rightAnswersPercent.value = percent
        _progressAnswersText.value =
            ResourceString(R.string.right_answers_with_min, countOfRightAnswers, minRightAnswers)
    }

    private fun calcPercentOfRightAnswers(): Int = ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()

}
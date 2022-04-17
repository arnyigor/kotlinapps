package com.arny.kotlinapps.presentation.game

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arny.kotlinapps.domain.entity.GameResult
import com.arny.kotlinapps.domain.entity.GameSettings
import com.arny.kotlinapps.domain.entity.Level
import com.arny.kotlinapps.domain.entity.Question
import com.arny.kotlinapps.domain.usecases.GenerateQuestionUseCase
import com.arny.kotlinapps.domain.usecases.GetGameSettingsUseCase
import java.util.*

class GameViewModel : ViewModel() {

    companion object {
        const val ZERO_ANSWERS = 0
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

    private val _progressAnswers = MutableLiveData<String>()
    val  progressAnswers: LiveData<String> = _progressAnswers

    private val _rightAnswersPercent = MutableLiveData<Int>()
    val rightAnswersPercent: LiveData<Int> = _rightAnswersPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult> = _gameResult

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question> = _question

    fun loadGame(level: Level) {
        getGameSettings(level)
        loadGameData()
        generateQuestion()
        startTimer()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun loadGameData() {
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSum)
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
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
        TODO("Not yet implemented")
    }

    fun chooseAnswer(num: Int) {
        checkAnswer(num)
        generateQuestion()
    }

    private fun checkAnswer(num: Int) {
        if (num == _question.value?.rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun initGameResult() {
        if (_gameResult.value == null) {
            _gameResult.value = GameResult(
                winner = false,
                countOfRightAnswers = 0,
                countOfQuestions = 0,
                percentOfRightAnswers = 0,
                gameSettings = gameSettings
            )
        }
    }
}
package com.arny.kotlinapps.domain.usecases

import com.arny.kotlinapps.domain.entity.GameSettings
import com.arny.kotlinapps.domain.entity.Level
import com.arny.kotlinapps.domain.entity.Level.*

class GetGameSettingsUseCase {
    operator fun invoke(level: Level): GameSettings = when (level) {
        TEST -> GameSettings(
            timeInSec = 30,
            maxSum = 10,
            minCountOfRightAnswers = 10,
            minPercentOrRightAnswers = 30,
        )
        EASY -> GameSettings(
            timeInSec = 50,
            maxSum = 15,
            minCountOfRightAnswers = 18,
            minPercentOrRightAnswers = 50,
        )
        NORMAL -> GameSettings(
            timeInSec = 45,
            maxSum = 30,
            minCountOfRightAnswers = 20,
            minPercentOrRightAnswers = 75,
        )
        HARD -> GameSettings(
            timeInSec = 40,
            maxSum = 30,
            minCountOfRightAnswers = 20,
            minPercentOrRightAnswers = 90,
        )
    }
}
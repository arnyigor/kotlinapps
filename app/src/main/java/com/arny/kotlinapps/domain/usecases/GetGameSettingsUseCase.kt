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
            minPercentOrRightAnswers = 50,
        )
        EASY -> GameSettings(
            timeInSec = 30,
            maxSum = 10,
            minCountOfRightAnswers = 10,
            minPercentOrRightAnswers = 70,
        )
        NORMAL -> GameSettings(
            timeInSec = 40,
            maxSum = 20,
            minCountOfRightAnswers = 20,
            minPercentOrRightAnswers = 80,
        )
        HARD -> GameSettings(
            timeInSec = 40,
            maxSum = 30,
            minCountOfRightAnswers = 30,
            minPercentOrRightAnswers = 90,
        )
    }
}
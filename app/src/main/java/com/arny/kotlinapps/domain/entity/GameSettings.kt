package com.arny.kotlinapps.domain.entity

data class GameSettings(
    val timeInSec: Int,
    val maxSum: Int,
    val minCountOfRightAnswers: Int,
    val minPercentOrRightAnswers: Int
)

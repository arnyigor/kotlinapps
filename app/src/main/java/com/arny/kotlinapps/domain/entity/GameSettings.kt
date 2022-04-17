package com.arny.kotlinapps.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val timeInSec: Int,
    val maxSum: Int,
    val minCountOfRightAnswers: Int,
    val minPercentOrRightAnswers: Int
) : Parcelable

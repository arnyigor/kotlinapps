package com.arny.kotlinapps.presentation.utils

import android.content.Context

interface IWrappedString {
    fun toString(context: Context): String?
}
package com.arny.kotlinapps.presentation.utils

import android.content.Context
import androidx.annotation.StringRes

class ResourceString(
    @StringRes val res: Int,
    private vararg val values: Any
) : IWrappedString {
    override fun toString(context: Context): String = context.getString(res, *values)
}
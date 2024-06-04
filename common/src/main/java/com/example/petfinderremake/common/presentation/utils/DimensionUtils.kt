package com.example.petfinderremake.common.presentation.utils

import android.content.Context

fun convertDpToPixels(context: Context, dp: Int) =
    (dp * context.resources.displayMetrics.density).toInt()

fun convertPixelsToDp(context: Context, pixels: Int) =
    (pixels / context.resources.displayMetrics.density).toInt()
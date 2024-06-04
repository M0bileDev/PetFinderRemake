package com.example.petfinderremake.common.presentation.utils

import android.content.Context

sealed interface PresentationData {
    data class StringValue(val value: String) : PresentationData
    data class StringResource(val stringRes: Int) : PresentationData
}

fun PresentationData.fromPresentationDataToString(context: Context): String {
    return when (this) {
        is PresentationData.StringResource -> context.getString(stringRes)
        is PresentationData.StringValue -> value
    }
}

fun List<Pair<Int, PresentationData>>.fromPresentationDataListToString(context: Context): String {
    val stringBuilder = StringBuilder()
    forEach {
        val key = context.getString(it.first)
        val value = it.second.fromPresentationDataToString(context)
        stringBuilder.append(key).append(":").append(" ").append(value)
            .apply { repeat(2) { appendLine() } }
    }
    return stringBuilder.toString()
}
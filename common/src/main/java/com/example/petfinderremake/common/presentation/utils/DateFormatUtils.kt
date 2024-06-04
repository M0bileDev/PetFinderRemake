package com.example.petfinderremake.common.presentation.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateFormatUtils {
    companion object {
        const val PATTERN = "dd/MM/yyyy HH:mm:ss"

        fun format(date: LocalDateTime): String {
            val formatter = DateTimeFormatter.ofPattern(PATTERN)
            return date.format(formatter)
        }
    }
}
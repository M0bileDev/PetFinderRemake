package com.example.petfinderremake.common.ext

fun Long?.orNotDefined() = this ?: -1L
fun Long.isNegative() = this < 0
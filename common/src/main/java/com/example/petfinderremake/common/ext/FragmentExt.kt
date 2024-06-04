package com.example.petfinderremake.common.ext

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key) ?: throw Exception("getNavigationResult error")

fun Fragment.setNavigationResult(result: String, key: String = "result") =
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result) ?: throw Exception("setNavigationResult error")
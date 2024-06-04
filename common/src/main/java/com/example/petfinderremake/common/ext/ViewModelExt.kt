package com.example.petfinderremake.common.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderremake.logging.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration

fun ViewModel.repeatableLaunch(duration: Duration, suspendBlock: suspend () -> Unit): Job {
    return viewModelScope.launch {
        while (isActive) {
            suspendBlock()
            delay(duration)
        }
    }
}
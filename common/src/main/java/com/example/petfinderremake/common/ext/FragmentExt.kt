package com.example.petfinderremake.common.ext

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

fun Fragment.getNavigationResult(key: String = "result"): Observable<String> {
    val subject = BehaviorSubject.create<String>()
    val saveStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
    val liveData =
        saveStateHandle?.getLiveData<String>(key) ?: throw Exception("getNavigationResult error")
    liveData.observe(this) { result ->
        result?.let {
            subject.onNext(it)
        }
    }
    return subject.hide()
}

fun Fragment.setNavigationResult(result: String, key: String = "result") =
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
        ?: throw Exception("setNavigationResult error")
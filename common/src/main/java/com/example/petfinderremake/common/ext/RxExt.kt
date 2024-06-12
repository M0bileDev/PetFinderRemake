package com.example.petfinderremake.common.ext

import io.reactivex.rxjava3.core.Notification
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

fun <T : Any> BehaviorSubject<T>.getValueOrThrow(exception: Exception = Exception("Value is null")): T {
    return this.value ?: throw exception
}

fun <T : Any> Notification<T>.getValueOrThrow(exception: Exception = Exception("Value is null")): T {
    return this.value ?: throw exception
}

fun Disposable?.getOrThrow(exception: Exception = Exception("Disposable is null")): Disposable {
    return this ?: throw exception
}

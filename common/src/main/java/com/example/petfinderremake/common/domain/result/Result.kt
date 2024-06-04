package com.example.petfinderremake.common.domain.result


sealed interface Result<out T, out E : RootError> {
    data class Success<out T>(val success: T) : Result<T, Nothing>
    data class Error<out E : RootError>(val error: E) : Result<Nothing, E>
}

suspend fun <T, E : RootError> Result<T, E>.onSuccess(block: suspend (Result.Success<T>) -> Unit): Result<T, E> {
    if (this is Result.Success) {
        block(this)
    }

    return this
}

suspend fun <T, E : RootError> Result<T, E>.onError(block: suspend (Result.Error<E>) -> Unit): Result<T, E> {
    if (this is Result.Error) {
        block(this)
    }

    return this
}


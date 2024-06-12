package com.example.petfinderremake.common.domain.result.error

import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError

enum class ArgumentError : Error {
    ARGUMENT_IS_EMPTY,
    ARGUMENT_IS_NEGATIVE

}

fun <T, E : RootError> Result<T, E>.onArgumentError(
    onArgumentIsEmpty: () -> Unit = {},
    onArgumentIsNegative: () -> Unit = {},
    onArgumentError: () -> Unit = {},
): Result<T, E> {

    if (this !is Result.Error)
        return this

    if (this.error !is ArgumentError)
        return this

    when (this.error) {
        ArgumentError.ARGUMENT_IS_EMPTY -> onArgumentIsEmpty()
        ArgumentError.ARGUMENT_IS_NEGATIVE -> onArgumentIsNegative()
    }

    onArgumentError()
    return this
}
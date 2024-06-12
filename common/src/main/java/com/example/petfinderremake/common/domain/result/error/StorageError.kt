package com.example.petfinderremake.common.domain.result.error

import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError

enum class StorageError : Error {
    NO_DATA_TO_STORE
}

fun <T, E : RootError> Result<T, E>.onStorageError(
    onNoDataToStore: (StorageError) -> Unit = {},
    onStorageError: (StorageError) -> Unit = {},
): Result<T, E> {

    if (this !is Result.Error)
        return this

    if (this.error !is StorageError)
        return this

    when (this.error) {
        StorageError.NO_DATA_TO_STORE -> onNoDataToStore(this.error)
    }

    onStorageError(this.error)
    return this
}
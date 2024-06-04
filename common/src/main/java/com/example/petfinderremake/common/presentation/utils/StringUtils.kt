package com.example.petfinderremake.common.presentation.utils

import com.example.petfinderremake.common.R
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError

fun networkErrorStringResource(networkError: NetworkError): Int {
    return when (networkError) {
        NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS -> R.string.network_error_access_denied_invalid_credentials
        NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS -> R.string.network_error_access_denied_insufficient_access
        NetworkError.NOT_FOUND -> R.string.network_error_not_found
        NetworkError.UNEXPECTED_ERROR -> R.string.network_error_unexpected_error
        NetworkError.MISSING_PARAMETERS -> R.string.network_error_missing_parameters
        NetworkError.INVALID_PARAMETERS -> R.string.network_error_invalid_parameters
    }
}

fun storageErrorStringResource(storageError: StorageError): Int {
    return when (storageError) {
        StorageError.NO_DATA_TO_STORE -> R.string.store_error_no_data_to_store
    }
}
package com.example.petfinderremake.common.domain.result.error

import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError

enum class NetworkError(val code: Int) : Error {
    ACCESS_DENIED_INVALID_CREDENTIALS(401),
    ACCESS_DENIED_INSUFFICIENT_ACCESS(403),
    NOT_FOUND(404),
    UNEXPECTED_ERROR(500),
    MISSING_PARAMETERS(0x00001),
    INVALID_PARAMETERS(0x00002);

    class NetworkErrorTypeException(
        message: String = "Unsupported NetworkError. NetworkError should be one of" +
                "ACCESS_DENIED_INVALID_CREDENTIALS, " +
                "ACCESS_DENIED_INSUFFICIENT_ACCESS, " +
                "NOT_FOUND, " +
                "UNEXPECTED_ERROR, " +
                "MISSING_PARAMETERS, " +
                "INVALID_PARAMETERS"
    ) : IllegalStateException(message)
}

suspend fun <T, E : RootError> Result<T, E>.onNetworkError(
    onAccessDeniedInvalidCredentials: (NetworkError) -> Unit = {},
    onAccessDeniedInsufficientAccess: (NetworkError) -> Unit = {},
    onNotFound: (NetworkError) -> Unit = {},
    onUnexpectedError: (NetworkError) -> Unit = {},
    onMissingParameters: (NetworkError) -> Unit = {},
    onInvalidParameters: (NetworkError) -> Unit = {},
    onNetworkError: suspend (NetworkError) -> Unit = {}
): Result<T, E> {

    if (this !is Result.Error)
        return this

    if (this.error !is NetworkError)
        return this

    when (this.error) {
        NetworkError.ACCESS_DENIED_INVALID_CREDENTIALS -> onAccessDeniedInvalidCredentials(this.error)
        NetworkError.ACCESS_DENIED_INSUFFICIENT_ACCESS -> onAccessDeniedInsufficientAccess(this.error)
        NetworkError.NOT_FOUND -> onNotFound(this.error)
        NetworkError.UNEXPECTED_ERROR -> onUnexpectedError(this.error)
        NetworkError.MISSING_PARAMETERS -> onMissingParameters(this.error)
        NetworkError.INVALID_PARAMETERS -> onInvalidParameters(this.error)
    }

    onNetworkError(this.error)
    return this
}




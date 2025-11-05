package com.om.smartpost.core.domain.utils

sealed class ApiError: Error {
    data class Transport(val networkError: NetworkError): ApiError()
    data class ServerMessage(val serverRes: ServerResponse): ApiError()

    data object Conflict : ApiError()
    data object ServerError : ApiError()           // 500
}

data class ServerResponse(
    val code: String?,
    val message: String?
)


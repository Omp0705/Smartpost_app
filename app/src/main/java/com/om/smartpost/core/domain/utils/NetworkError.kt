package com.om.smartpost.core.domain.utils


// Error Types Enum for Network
enum class NetworkError: Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUEST,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION_ERROR,
    UNKNOWN,
    UNAUTHORIZED,
    BAD_REQ
}

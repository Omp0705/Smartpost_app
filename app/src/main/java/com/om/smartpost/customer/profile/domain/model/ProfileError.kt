package com.om.smartpost.customer.profile.domain.model

import com.om.smartpost.core.domain.utils.Error
import com.om.smartpost.core.domain.utils.NetworkError

sealed interface ProfileError : Error {
    enum class Network(val type: NetworkError) : ProfileError {
        REQUEST_TIMEOUT(NetworkError.REQUEST_TIMEOUT),
        TOO_MANY_REQUEST(NetworkError.TOO_MANY_REQUEST),
        NO_INTERNET(NetworkError.NO_INTERNET),
        SERVER_ERROR(NetworkError.SERVER_ERROR),
        SERIALIZATION_ERROR(NetworkError.SERIALIZATION_ERROR),
        UNKNOWN(NetworkError.UNKNOWN),
        UNAUTHORIZED(NetworkError.UNAUTHORIZED),
        BAD_REQ(NetworkError.BAD_REQ)
    }
    object Validation : ProfileError // Example generic validation error
}

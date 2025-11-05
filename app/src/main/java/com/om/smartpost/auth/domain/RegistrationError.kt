package com.om.smartpost.auth.domain

import com.om.smartpost.core.domain.utils.NetworkError
import com.om.smartpost.core.domain.utils.Error

sealed interface RegistrationError: Error {
    data object UsernameExists: RegistrationError
    data object EmailExists: RegistrationError
    data object MobileExists: RegistrationError
    data object WeakPassword: RegistrationError
    data object InvalidEmail: RegistrationError
    data object InvalidMobile: RegistrationError

    // Transport wrapper
    data class Transport(val cause: NetworkError): RegistrationError
}
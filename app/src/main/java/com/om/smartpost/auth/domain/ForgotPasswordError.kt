package com.om.smartpost.auth.domain

import com.om.smartpost.core.domain.utils.Error

sealed interface ForgotPasswordError : Error {
    data object InvalidOtp : ForgotPasswordError
    data object OtpExpired : ForgotPasswordError
    data object SessionExpired : ForgotPasswordError
    data object NoInternet : ForgotPasswordError
    data object ServerError : ForgotPasswordError
    data object Unknown : ForgotPasswordError
}

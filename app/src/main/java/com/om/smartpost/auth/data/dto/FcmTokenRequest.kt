package com.om.smartpost.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequest(
    val token: String
)

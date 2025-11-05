package com.om.smartpost.core.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val code: String? = null,
    val message: String? = null,
    val errors: String? = null,
)

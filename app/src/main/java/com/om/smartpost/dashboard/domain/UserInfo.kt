package com.om.smartpost.dashboard.domain

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val username: String,
    val role: String
)
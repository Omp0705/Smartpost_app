package com.om.smartpost.auth.presentation

sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val role: String) : AuthState()
    object Unauthenticated : AuthState()
}
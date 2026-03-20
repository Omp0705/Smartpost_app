package com.om.smartpost.auth.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.auth.domain.AuthRepository
import com.om.smartpost.auth.presentation.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.messaging.FirebaseMessaging

class SplashViewModel (
    private val authRepository: AuthRepository
) : ViewModel() {

    // The state that the Splash Screen and Navigation will observe
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            // Delay slightly to match the minimum splash screen display time
            // (optional, but can make the transition smoother)
            // delay(100)
            val isAuthenticated = authRepository.validateSession()
            if (isAuthenticated) {
                val role = authRepository.getUserRole()
                if (role != null) {
                    _authState.value = AuthState.Authenticated(role)
                    syncFcmToken()
                } else {
                    // Token valid but no role found? Force re-login
                    _authState.value = AuthState.Unauthenticated
                }
            } else {
                _authState.value = AuthState.Unauthenticated

            }

        }
    }

    private fun syncFcmToken() {
        viewModelScope.launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                authRepository.updateFcmToken(token)
            } catch (e: Exception) {
                // Ignore gracefully if FCM fails during startup
            }
        }
    }

}

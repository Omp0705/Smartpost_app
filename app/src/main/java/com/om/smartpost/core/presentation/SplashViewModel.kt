package com.om.smartpost.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.auth.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
            _authState.value = if (isAuthenticated) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated

            }

        }
    }

}
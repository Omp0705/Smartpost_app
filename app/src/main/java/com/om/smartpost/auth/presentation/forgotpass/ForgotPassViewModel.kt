package com.om.smartpost.auth.presentation.forgotpass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.auth.domain.ValidationError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    private val _state = MutableStateFlow(ForgotUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<ForgotEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: ForgotAction) {
        when (action) {
            is ForgotAction.updateEmail -> {
                _state.update {
                    it.copy(
                        email = action.email,
                        canSubmit = action.email.isNotBlank()
                    )
                }
            }

            ForgotAction.ResetPassword -> {
                resetPassword()
            }
            ForgotAction.onNavToSignIn -> {

            }
        }
    }

    private fun resetPassword() {
        val current = state.value
        val email = current.email.trim()

        viewModelScope.launch {
            when {
                email.isEmpty() -> _events.send(ForgotEvent.ValidationErrors(ValidationError.EMAIL_EMPTY))

                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    _events.send(ForgotEvent.ValidationErrors(ValidationError.EMAIL_INVALID))

                else -> {
                    // Proceed with the fake reset logic
                    _state.update { it.copy(isLoading = true) }

                    // Simulate a network call
                    kotlinx.coroutines.delay(1500)

                    _state.update { it.copy(isLoading = false) }
                    _events.send(ForgotEvent.ShowMessage("Password reset link sent to your email"))
                }
            }
        }
    }
}

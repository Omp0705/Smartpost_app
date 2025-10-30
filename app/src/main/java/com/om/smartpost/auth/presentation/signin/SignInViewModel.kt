package com.om.smartpost.auth.presentation.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.auth.domain.ValidationError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow(SignInUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<SignInEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SignInAction) {
        when (action) {
            SignInAction.FacebookSignIn -> {}
            SignInAction.ForgotPassword -> {}
            SignInAction.GoogleSignIn -> {}
            SignInAction.NavigateToSignUp -> {}
            SignInAction.SignIn -> {
                signIn()
            }
            SignInAction.TogglePasswordVisibility -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }
            is SignInAction.UpdateIdentifier -> {
                _state.update {
                    it.copy(
                        identifier = action.identifier
                    )
                }
            }
            is SignInAction.UpdatePassword -> {
                _state.update {
                    it.copy(
                        password = action.password
                    )
                }
            }
        }
    }

    fun signIn(){
        val currentState = state.value

        viewModelScope.launch {
            val identifier = currentState.identifier.trim()
            val password = currentState.password
            when{
                identifier.isEmpty() -> {
                    _events.send(SignInEvent.ValidationErrors(ValidationError.IDENTIFIER_EMPTY))
                }

                identifier.contains("@") -> {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(identifier).matches()) {
                        _events.send(SignInEvent.ValidationErrors(ValidationError.EMAIL_INVALID))
                    } else if (password.isEmpty()) {
                        _events.send(SignInEvent.ValidationErrors(ValidationError.PASSWORD_EMPTY))
                    } else if (password.length < 6) {
                        _events.send(SignInEvent.ValidationErrors(ValidationError.PASSWORD_TO_SHORT))
                    } else {
                        _events.send(SignInEvent.ShowMessage("Signing in..."))
                        _state.update { it.copy(isLoading = true) }
                    }
                }

                else -> {
                    val usernameRegex = "^[a-zA-Z0-9._-]{3,15}$".toRegex()
                    if (!usernameRegex.matches(identifier)) {
                        _events.send(SignInEvent.ValidationErrors(ValidationError.USERNAME_INVALID))
                    } else if (password.isEmpty()) {
                        _events.send(SignInEvent.ValidationErrors(ValidationError.PASSWORD_EMPTY))
                    } else if (password.length < 6) {
                        _events.send(SignInEvent.ValidationErrors(ValidationError.PASSWORD_TO_SHORT))
                    } else {
                        _events.send(SignInEvent.ShowMessage("Signing in..."))
                        _state.update { it.copy(isLoading = true) }
                    }
                }

            }
        }
    }


}
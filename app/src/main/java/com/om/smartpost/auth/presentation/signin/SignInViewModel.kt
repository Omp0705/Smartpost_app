package com.om.smartpost.auth.presentation.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.auth.domain.AuthRepository
import com.om.smartpost.auth.domain.LoginUser
import com.om.smartpost.auth.domain.ValidationError
import com.om.smartpost.core.domain.utils.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel (
    private val authRepository: AuthRepository
): ViewModel() {

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

    private fun signIn() {
        val currentState = state.value
        val identifier = currentState.identifier.trim()
        val password = currentState.password

        viewModelScope.launch {
            val validationError = validateInput(identifier, password)
            if (validationError != null) {
                _events.send(SignInEvent.ValidationErrors(validationError))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }
            val result =  authRepository.loginUser(LoginUser(identifier,password))
            when (result) {
                is Result.Success -> {
                    _events.send(SignInEvent.ShowMessage("Login Successful"))
                    _events.send(SignInEvent.NavigateToHome)
                }
                is Result.Error -> {
                    _events.send(SignInEvent.ShowMessage(result.error.message))
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }
    private fun validateInput(identifier: String, password: String): ValidationError? {
        return when {
            identifier.isEmpty() -> ValidationError.IDENTIFIER_EMPTY
            identifier.contains("@") && !android.util.Patterns.EMAIL_ADDRESS.matcher(identifier).matches() ->
                ValidationError.EMAIL_INVALID
            !identifier.contains("@") && !Regex("^[a-zA-Z0-9._-]{3,15}$").matches(identifier) ->
                ValidationError.USERNAME_INVALID
            password.isEmpty() -> ValidationError.PASSWORD_EMPTY
            password.length < 6 -> ValidationError.PASSWORD_TO_SHORT
            else -> null
        }
    }


}
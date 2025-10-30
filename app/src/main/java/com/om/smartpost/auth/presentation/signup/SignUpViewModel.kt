package com.om.smartpost.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.auth.domain.ValidationError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignUpUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<SignUpEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.UpdateFullName -> _state.update { it.copy(fullName = action.value) }
            is SignUpAction.UpdateUsername -> _state.update { it.copy(username = action.value) }
            is SignUpAction.UpdateEmail -> _state.update { it.copy(email = action.value) }
            is SignUpAction.UpdateMobile -> _state.update { it.copy(mobileNo = action.value) }
            is SignUpAction.UpdatePassword -> _state.update { it.copy(password = action.value) }
            is SignUpAction.UpdateConfirmPassword -> _state.update { it.copy(confirmPassword = action.value) }
            is SignUpAction.SelectRole -> _state.update { it.copy(selectedRole = action.role) }
            is SignUpAction.ToggleAgreement -> _state.update { it.copy(isAgreementChecked = !it.isAgreementChecked) }
            is SignUpAction.TogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            is SignUpAction.ToggleConfirmPasswordVisibility -> _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            SignUpAction.Submit -> validateAndSubmit()
            SignUpAction.NavigateBack -> {}
        }
    }

    private fun validateAndSubmit() {
        val current = state.value
        val errors = mutableMapOf<String, String>()

        if (current.fullName.isBlank())
            errors["fullName"] = "Please enter your full name"

        if (current.username.isBlank())
            errors["username"] = "Username cannot be empty"
        else if (current.username.length < 4)
            errors["username"] = "Username must be at least 4 characters"

        if (current.email.isBlank())
            errors["email"] = "Please enter your email"
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(current.email).matches())
            errors["email"] = "Invalid email format"

        if (current.mobileNo.isBlank())
            errors["mobileNo"] = "Please enter your mobile number"
        else if (!current.mobileNo.matches(Regex("^[0-9]{10}$")))
            errors["mobileNo"] = "Invalid mobile number"

        if (current.password.isBlank())
            errors["password"] = "Please enter your password"
        else if (current.password.length < 6)
            errors["password"] = "Password too short (min 6 chars)"

        if (current.confirmPassword.isBlank())
            errors["confirmPassword"] = "Please confirm your password"
        else if (current.password != current.confirmPassword)
            errors["confirmPassword"] = "Passwords do not match"

        if (current.selectedRole.isBlank())
            errors["selectedRole"] = "Please select your role"

        if (!current.isAgreementChecked)
            errors["agreement"] = "You must agree to the Terms & Privacy Policy"

        if (errors.isNotEmpty()) {
            _state.update { it.copy(validationErrors = errors) }
            viewModelScope.launch {
                _events.send(SignUpEvent.ValidationFailed(errors))
            }
            return
        }

        // If all good
        _state.update { it.copy(isLoading = true, validationErrors = emptyMap()) }

        viewModelScope.launch {
            _events.send(SignUpEvent.ShowMessage("Creating account..."))
            // TODO: call repository
            _state.update { it.copy(isLoading = false) }
        }
    }
}

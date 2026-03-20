package com.om.smartpost.auth.presentation.forgotpass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.auth.domain.AuthRepository
import com.om.smartpost.auth.domain.ForgotPasswordError
import com.om.smartpost.auth.domain.ValidationError
import com.om.smartpost.auth.presentation.mappers.toUiText
import com.om.smartpost.core.domain.mappers.toAuthError
import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.om.smartpost.core.presentation.UiText
import com.om.smartpost.auth.presentation.mappers.toUiText

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

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
                        canSubmit = action.email.isNotBlank() // Only check for non-empty here
                    )
                }
            }

            is ForgotAction.updateOtpCode -> {
                _state.update {
                    it.copy(
                        otpCode = action.otp,
                        // Assuming 6-digit OTP is required for submission
                        canSubmit = action.otp.length == 4
                    )
                }
            }

            is ForgotAction.updateNewPassword -> {
                _state.update {
                    val newPass = action.newPassword
                    it.copy(
                        newPassword = newPass,
                        // Check if new password is valid and matches confirm password
                        canSubmit = newPass.length >= 8 && newPass == it.confirmPassword
                    )
                }
            }

            is ForgotAction.updateConfirmPassword -> {
                _state.update {
                    val cnfPass = action.cnfPassword
                    it.copy(
                        confirmPassword = cnfPass,
                        // Check if confirm password is valid and matches new password
                        canSubmit = cnfPass.length >= 8 && cnfPass == it.newPassword
                    )
                }
            }

            ForgotAction.SendResetLink -> sendResetLink()
            ForgotAction.ValidateOtp -> validateOtp()
            ForgotAction.ResetPassword -> resetPassword()

            ForgotAction.GoBackToEmailEntry -> {
                // When going back, reset current step state and validation errors
                _state.update {
                    it.copy(
                        currentStep = ForgotStep.EMAIL_ENTRY,
                        canSubmit = it.email.isNotBlank(),
                        validationErrors = null
                    )
                }
            }

            ForgotAction.onNavToSignIn -> {
                // Trigger navigation event back to the sign in screen
                viewModelScope.launch {
                    _events.send(ForgotEvent.NavigateToSignIn)
                }
            }

            ForgotAction.ToggleCnfPasswordVisibility -> {
                _state.update {
                    it.copy(
                        isConfirmPasswordVisible = !it.isConfirmPasswordVisible
                    )
                }
            }

            ForgotAction.ToggleNewPasswordVisibility -> {
                _state.update {
                    it.copy(
                        isNewPasswordVisible = !it.isNewPasswordVisible
                    )
                }
            }
        }
    }

    private fun sendResetLink() = viewModelScope.launch {
        val email = state.value.email
        if (!state.value.canSubmit) return@launch // Prevent execution if not submittable

        _state.update { it.copy(isLoading = true, validationErrors = null) }
        val result = authRepository.sendResetLink(email)
        when (result) {
            is Result.Success -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        currentStep = ForgotStep.OTP_VALIDATION,
                        canSubmit = false // for next step (OTP input)
                    )
                }
                _events.send(ForgotEvent.ShowMessage(UiText.DynamicString("OTP sent to your email!")))
            }

            is Result.Error -> {
                _state.update { it.copy(isLoading = false) }


                _events.send(ForgotEvent.ShowMessage(result.error.toUiText()))
            }
        }

    }

    private fun validateOtp() = viewModelScope.launch {
        val email = state.value.email
        val otp = state.value.otpCode
        if (!state.value.canSubmit) {
            _events.send(ForgotEvent.ShowMessage(UiText.DynamicString("OTP must be 6 digits.")))
            return@launch
        }

        _state.update { it.copy(isLoading = true) }
        val result = authRepository.validateOtp(email, otp)
        when (result) {
            is Result.Success -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        currentStep = ForgotStep.PASSWORD_RESET,
                        canSubmit = false // for next step (OTP input)
                    )
                }
                _events.send(ForgotEvent.ShowMessage(UiText.DynamicString("Otp validated")))
            }

            is Result.Error -> {
                _state.update { it.copy(isLoading = false) }


                _events.send(ForgotEvent.ShowMessage(result.error.toUiText()))
            }
        }


    }

    private fun resetPassword() = viewModelScope.launch {
        val email = state.value.email
        val newPassword = state.value.newPassword
        if (!state.value.canSubmit) {
            _events.send(ForgotEvent.ShowMessage(UiText.DynamicString("Passwords must match and be at least 8 characters.")))
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        // 💡 Placeholder Logic: Assume successful password reset
        val result = authRepository.resetPassword(email, newPassword)
        when (result) {
            is Result.Success -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        currentStep = ForgotStep.PASSWORD_RESET,
                        canSubmit = false // for next step (OTP input)
                    )
                }
                _events.send(ForgotEvent.ShowMessage(UiText.DynamicString("Password Reset Successfully")))
            }
            is Result.Error -> {
                _state.update { it.copy(isLoading = false) }


                _events.send(ForgotEvent.ShowMessage(result.error.toUiText()))
            }
        }
    }
}

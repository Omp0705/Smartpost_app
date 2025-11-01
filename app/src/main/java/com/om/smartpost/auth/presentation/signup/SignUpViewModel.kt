package com.om.smartpost.auth.presentation.signup

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.auth.domain.ValidationError
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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

    // Simulated "taken" usernames for local debounce demo
    private val takenUsernames = setOf("admin", "test", "smartpost")
    private var usernameJob: Job? = null

    fun onAction(action: SignUpAction) {
        when(action){
            is SignUpAction.SelectRole -> setState { copy(selectedRole = action.role) }
            is SignUpAction.ToggleAgreement -> setState { copy(isAgreementChecked = action.checked) }
            is SignUpAction.TogglePasswordVisibility -> setState { copy(isPasswordVisible = action.show) }
            is SignUpAction.UpdateEmail -> { updateEmail(action.value) }
            is SignUpAction.UpdateFullName -> { updateFullName(action.value)}
            is SignUpAction.UpdateMobile -> { updateMobile(action.value)}
            is SignUpAction.UpdatePassword -> { updatePassword(action.value)}
            is SignUpAction.UpdateUsername -> { updateUsername(action.value)}
            SignUpAction.Continue -> onContinue()
            SignUpAction.CreateAccount -> onCreateAccount()
            SignUpAction.NavigateBack -> {
                if (_state.value.stage == SignUpStage.Profile) {
                    setState { copy(stage = SignUpStage.Credentials) }
                }
                else if (_state.value.stage == SignUpStage.Credentials){

                }
            }
        }

    }
    private fun updateEmail(email: String){
        val trimmed = email.trim()
        val err = validateEmail(trimmed)
        setState { copy(email = trimmed, emailError = err) }
    }
    private fun updateMobile(mobile: String){
        val trimmed = mobile.trim()
        val err = validateMobileNo(trimmed)
        setState { copy(mobileNo = trimmed, mobileError = err) }
    }
    private fun updateFullName(fullName: String){
        val err = if (fullName.length < 2) "Enter your full name" else null
        setState { copy(fullName = fullName, fullNameError = err) }
    }
    private fun updateUsername(username: String){
        val trimmed = username.trim()
        val err = validateUsernameFormat(trimmed)
        setState { copy(username = trimmed, usernameError = err) }

        usernameJob?.cancel()
        usernameJob = viewModelScope.launch {
            delay(400)
            val availabilityErr = if (trimmed.isNotEmpty() && trimmed.lowercase() in takenUsernames)
                "Username already taken" else null
            // Only set availability error if no format error
            if (_state.value.usernameError == null || _state.value.usernameError == "Username already taken") {
                setState { copy(usernameError = err ?: availabilityErr) }
            }
        }
    }
    private fun updatePassword(password: String){
        val trimmed = password.trim()
        val passwordStrength = passwordStrength(password)
        val err = validatePassword(trimmed)
        setState { copy(
            password = trimmed,
            passwordStrength = passwordStrength,
            passwordError = err
        ) }
    }

//    private fun onContinue() {
//        val state = _state.value
//        val emailErr = validateEmail(state.email)
//        val mobileErr = validateMobileNo(state.mobileNo)
//        val passErr = validatePassword(state.password)
//        val agreeErr = if (!state.isAgreementChecked) "Please accept the terms" else null
//        val anyErr = listOfNotNull(emailErr, passErr, agreeErr).isNotEmpty()
//        setState {
//            copy(
//                emailError = emailErr,
//                mobileError = mobileErr,
//                passwordError = passErr
//            )
//        }
//        if (!anyErr) {
//            setState { copy(stage = SignUpStage.Profile) }
//        }
//    }

    private fun onContinue() {
        val s = _state.value

        // Trim inputs
        val emailInput = s.email.trim()
        val mobileInput = s.mobileNo.trim()

        // Validate each field independently (both required)
        val emailErr = when {
            emailInput.isBlank() -> "Required"
            else -> validateEmail(emailInput) // returns "Invalid Email" or null
        }

        val mobileErr = when {
            mobileInput.isBlank() -> "Required"
            else -> validateMobileNo(mobileInput) // returns "Invalid Mobile No." or null
        }

        // Password + agreement
        val passErr = validatePassword(s.password) // your existing rules
        val agreeErr = if (!s.isAgreementChecked) "Please accept the terms" else null

        // Show inline errors (CTA stays enabled)
        setState {
            copy(
                emailError = emailErr,
                mobileError = mobileErr,
                passwordError = passErr
            )
        }

        // Advance only when all mandatory fields are valid and terms accepted
        val hasErrors = listOfNotNull(emailErr, mobileErr, passErr, agreeErr).isNotEmpty()
        if (!hasErrors) {
            setState { copy(stage = SignUpStage.Profile) }
        }
    }

    private fun onCreateAccount() {
        val state = _state.value
        val nameErr = if (state.fullName.trim().length < 2) "Enter your full name" else null
        val usernameErr = validateUsernameFormat(state.username) ?: state.usernameError
        val roleErr = if (state.selectedRole == null) "Select a role" else null
        val anyErr = listOfNotNull(nameErr, usernameErr, roleErr).isNotEmpty()
        setState {
            copy(
                fullNameError = nameErr,
                usernameError = usernameErr,
                roleError = roleErr

            )
        }
        if (!anyErr) {
            setState { copy(isLoading = true) }
            // proceed to repository (Network Call)
        }

    }

    private fun validateEmail(input: String): String? {
        if (input.isBlank()) return "Required"
        val ok = android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
        val res = if (!ok) "Invalid Email" else null
        return res
    }

    private fun validateMobileNo(input: String): String?{
        if (input.isBlank()) return "Required"
        val digits = input.filter { it.isDigit() }
        if (digits.length > 10 || digits.length < 10){
            return "Invalid Mobile No."
        }
        else{
            return null
        }
    }

    private fun validatePassword(input: String): String?{
        if (input.length < 6){
            return "At least 6 characters"
        }
        val hasLetter = input.any { it.isLetter() }
        val hasDigit = input.any { it.isDigit() }
        if (!hasLetter || !hasDigit) return "Include a letter and a digit"
        else return null
    }

    private fun passwordStrength(pw: String): PasswordStrength {
        var score = 0
        if (pw.length >= 8) score++
        if (pw.any { it.isLowerCase() }) score++
        if (pw.any { it.isUpperCase() }) score++
        if (pw.any { it.isDigit() }) score++
        if (pw.any { !it.isLetterOrDigit() }) score++
        return when (score) {
            0,1,2 -> PasswordStrength.POOR
            3 -> PasswordStrength.MEDIUM
            4 -> PasswordStrength.GOOD
            else -> PasswordStrength.STRONG
        }
    }

    private fun validateUsernameFormat(u: String): String? {
        if (u.isBlank()) return "Required"
        val ok = u.all { it.isLetterOrDigit() || it == '_' } && u.length in 3..20
        return if (!ok) "3–20 chars, letters/digits/_ only" else null
    }

//    helper to update the state
    private inline fun setState(update: SignUpUiState.() -> SignUpUiState) {
        _state.update(update)
    }



}

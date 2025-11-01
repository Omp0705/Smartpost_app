package com.om.smartpost.auth.presentation.signup

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.om.smartpost.R
import com.om.smartpost.auth.presentation.components.AgreementRow
import com.om.smartpost.auth.presentation.components.AuthTextField
import com.om.smartpost.auth.presentation.components.PasswordStrengthIndicator
import com.om.smartpost.core.presentation.utils.AuthLabel

@Composable
fun SignUpStep1(
    state: SignUpUiState,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier) {

    val focusManager = LocalFocusManager.current
    val (emailRef,mobileRef,passwordRef)  = remember { FocusRequester.createRefs() }

    AuthLabel(text = "Email", modifier = Modifier)
    AuthTextField(
        value = state.email,
        onValueChange = { onAction(SignUpAction.UpdateEmail(it)) },
        label = "Enter email",
        isPassword = false,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        onImeAction = { passwordRef.requestFocus() },
        enabled = !state.isLoading,
        supportingText = state.emailError, // show inline error or hint
        modifier = Modifier
            .focusRequester(emailRef)
            .padding(top = 8.dp)
    )
    Spacer(Modifier.height(12.dp))
    AuthLabel(text = "Mobile No.", modifier = Modifier)
    AuthTextField(
        value = state.mobileNo,
        onValueChange = { onAction(SignUpAction.UpdateMobile(it)) },
        label = "Enter Mobile No",
        isPassword = false,
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next,
        onImeAction = { passwordRef.requestFocus() },
        enabled = !state.isLoading,
        supportingText = state.mobileError, // show inline error or hint
        modifier = Modifier
            .focusRequester(emailRef)
            .padding(top = 8.dp)
    )

    Spacer(Modifier.height(12.dp))

    AuthLabel(text = "Password", modifier = Modifier)
    AuthTextField(
        value = state.password,
        onValueChange = { onAction(SignUpAction.UpdatePassword(it)) },
        label = "Create password",
        isPassword = true,
        isPasswordVisible = state.isPasswordVisible,
        onPasswordToggle = { onAction(SignUpAction.TogglePasswordVisibility(!state.isPasswordVisible)) },
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
        onImeAction = { focusManager.clearFocus() },
        enabled = !state.isLoading,
        modifier = Modifier
            .focusRequester(passwordRef)
            .padding(top = 8.dp)
    )

//    Password Strength Indicator
    PasswordStrengthIndicator(
        strength = state.passwordStrength,
        modifier = Modifier.padding(top = 8.dp)
    )

    Spacer(Modifier.height(12.dp))

    AgreementRow(
        checked = state.isAgreementChecked,
        onCheckChanged = { onAction(SignUpAction.ToggleAgreement(it)) },
        onOpen = { /* open T&C */ }
    )


    
}



@Preview
@Composable
private fun SignUpStep1Preview() {
    SignUpStep1(
        state = SignUpUiState(),
        onAction = {}
    )
}
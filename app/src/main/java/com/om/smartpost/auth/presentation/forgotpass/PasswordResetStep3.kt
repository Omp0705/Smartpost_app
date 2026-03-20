package com.om.smartpost.auth.presentation.forgotpass

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.om.smartpost.R
import com.om.smartpost.auth.presentation.components.AuthButton
import com.om.smartpost.auth.presentation.components.AuthTextField

@Composable
public fun PasswordResetStep(
    state: ForgotUiState,
    onAction: (ForgotAction) -> Unit
) {

    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Set a new password",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Create a new password. Ensure it differs from previous ones for security",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_regular)),
                fontSize = 14.sp,
                color = Color(0xFF757575)
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "Password",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        )
        AuthTextField(
            value = state.newPassword,
            onValueChange = { onAction(ForgotAction.updateNewPassword(it)) },
            label = "Password",
            isPassword = true,
            isPasswordVisible = state.isNewPasswordVisible,
            onPasswordToggle = { onAction(ForgotAction.ToggleNewPasswordVisibility) },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            onImeAction = { /* Focus next field logic */ } ,
            enabled = !state.isLoading,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "Confirm Password",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        )
        AuthTextField(
            value = state.confirmPassword,
            onValueChange = { onAction(ForgotAction.updateConfirmPassword(it)) },
            label = "Confirm Password",
            isPassword = true,
            isPasswordVisible = state.isConfirmPasswordVisible,
            onPasswordToggle = { onAction(ForgotAction.ToggleCnfPasswordVisibility) },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            onImeAction = { onAction(ForgotAction.ResetPassword) } ,
            enabled = !state.isLoading,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))
        AuthButton(
            text = "Update Password",
            onClick = { onAction(ForgotAction.ResetPassword) },
            enabled = state.canSubmit && !state.isLoading,
            isLoading = state.isLoading
        )
    }
}
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.SmartPostTheme
import com.om.smartpost.R
import com.om.smartpost.auth.presentation.components.AuthButton
import com.om.smartpost.auth.presentation.components.AuthTextField

@Composable
fun EmailEntryStep(
    state: ForgotUiState,
    onAction: (ForgotAction) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Forgot password",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Please enter your email to reset the password",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_regular)),
                fontSize = 14.sp,
                color = Color(0xFF757575)
            )
        )

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "Your Email",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        )
        AuthTextField(
            value = state.email,
            onValueChange = { onAction(ForgotAction.updateEmail(it)) },
            label = "Enter your email",
            isPassword = false,
            keyboardType = KeyboardType.Email,
            onImeAction = { onAction(ForgotAction.SendResetLink) } ,
            enabled = !state.isLoading,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        AuthButton(
            text = "Reset Password",
            onClick = { onAction(ForgotAction.SendResetLink) },
            enabled = state.canSubmit && !state.isLoading,
            isLoading = state.isLoading
        )
    }
}

@Preview
@Composable
private fun EmailEntryStep1Preview() {
    SmartPostTheme {
        EmailEntryStep(
            state = ForgotUiState(),
            onAction = {}
        )
    }
}
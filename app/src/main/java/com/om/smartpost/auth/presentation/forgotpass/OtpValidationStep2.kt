package com.om.smartpost.auth.presentation.forgotpass

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.SmartPostTheme
import com.om.smartpost.R
import com.om.smartpost.auth.presentation.components.AuthButton
import com.om.smartpost.auth.presentation.components.AuthTextField
import com.om.smartpost.auth.presentation.components.OtpInputField
import com.om.smartpost.auth.presentation.components.OtpTextField


@Composable
public fun OtpValidationStep(
    state: ForgotUiState,
    onAction: (ForgotAction) -> Unit
) {
    val annotatedString = buildAnnotatedString {
        append("Haven't got the email yet? ")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary, // Resend link color
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(Font(R.font.manrope_semibold))
            )
        ) {
            append("Resend email")
        }
    }

    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Check your email",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = buildAnnotatedString {
                append("We sent a reset link to ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                ) {

                    append(state.email.take(6) + "...com")
                }
                append("\nEnter 5 digit code that mentioned in the email")
            },
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_regular)),
                fontSize = 14.sp,
                color = Color(0xFF757575)
            )
        )

        Spacer(modifier = Modifier.height(30.dp))
        // 🔑 OTP Input Field using the dedicated component

        OtpTextField(
            otpText = state.otpCode,
            onOtpTextChange = { otp,isComplete ->
                onAction(ForgotAction.updateOtpCode(otp))
                if (isComplete) {
                    println("Can submit now: $otp")
                }
            },
            otpCount = 4
        )
//        OtpInputField(
//            otpCode = state.otpCode,
//            onCodeChanged = { onAction(ForgotAction.updateOtpCode(it)) },
//            isEnabled = !state.isLoading,
//            onImeAction = { onAction(ForgotAction.ValidateOtp) },
//            modifier = Modifier.fillMaxWidth()
//        )

        Spacer(modifier = Modifier.height(30.dp))
        AuthButton(
            text = "Verify Code",
            onClick = { onAction(ForgotAction.ValidateOtp) },
            enabled = state.canSubmit && !state.isLoading,
            isLoading = state.isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = annotatedString,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(enabled = !state.isLoading) { onAction(ForgotAction.SendResetLink) },
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_regular)),
                fontSize = 14.sp,
                color = Color(0xFF757575)
            )
        )
    }
}

@Preview
@Composable
private fun OtpValidationScreenPreview() {
    SmartPostTheme {
        OtpValidationStep(
            state = ForgotUiState(),
            onAction = {}
        )
    }
}
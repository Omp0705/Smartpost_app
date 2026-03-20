package com.om.smartpost.auth.presentation.forgotpass

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.om.smartpost.auth.presentation.mappers.toUiText
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ForgotPasswordScreen(
    state: ForgotUiState,
    events: Flow<ForgotEvent>,
    onAction: (ForgotAction) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ForgotEvent.ValidationErrors -> {
                    snackbarHostState.showSnackbar(event.error.toUiText().asString(context))
                }

                is ForgotEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.msg.asString(context))
                }

                ForgotEvent.NavigateToSignIn -> {

                }
            }

        }
    }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 0.dp,
                modifier = Modifier.size(36.dp)
            ) {
                // Determine the correct back action based on the step
                val currentBackAction = when (state.currentStep) {
                    ForgotStep.EMAIL_ENTRY -> onBackPressed // Exit Forgot Password flow
                    else -> {
                        // Go back one step within the flow
                        { onAction(ForgotAction.GoBackToEmailEntry) }
                    }
                }
                IconButton(onClick = { onBackPressed() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

            }
            when (state.currentStep) {
                ForgotStep.EMAIL_ENTRY -> EmailEntryStep(state, onAction)
                ForgotStep.OTP_VALIDATION -> {
                    OtpValidationStep(
                        state = state,
                        onAction = onAction,
                    )
                }
                ForgotStep.PASSWORD_RESET -> {
                    PasswordResetStep(
                        state = state,
                        onAction = onAction
                    )
                }
            }


        }
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenPreview() {
    SmartPostTheme {
        ForgotPasswordScreen(
            state = ForgotUiState(currentStep = ForgotStep.EMAIL_ENTRY),
            events = emptyFlow(),
            onAction = {},
            onBackPressed = {}
        )

    }
}

@Preview
@Composable
private fun ForgotPasswordStep2Preview() {
    SmartPostTheme {
        ForgotPasswordScreen(
            state = ForgotUiState(currentStep = ForgotStep.OTP_VALIDATION),
            events = emptyFlow(),
            onAction = {},
            onBackPressed = {}
        )

    }
}
@Preview
@Composable
private fun ForgotPasswordStep3Preview() {
    SmartPostTheme {
        ForgotPasswordScreen(
            state = ForgotUiState(currentStep = ForgotStep.PASSWORD_RESET),
            events = emptyFlow(),
            onAction = {},
            onBackPressed = {}
        )

    }
}
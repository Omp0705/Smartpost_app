package com.om.smartpost.auth.presentation.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.SmartPostTheme
import com.example.compose.textFieldColor
import com.om.smartpost.R
import com.om.smartpost.auth.presentation.components.AuthButton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun SignUpScreen(
    state: SignUpUiState,
    events: Flow<SignUpEvent> = emptyFlow(),
    onAction: (SignUpAction) -> Unit,
    onBackPressed: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {

        events.collect { event ->
            when (event) {
                is SignUpEvent.Success -> snackbarHostState.showSnackbar(event.message)
                is SignUpEvent.Error -> {
                    snackbarHostState.showSnackbar(event.error.message)
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onAction(SignUpAction.NavigateBack)
                        onBackPressed()
                    },
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Create account",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.weight(1f))

                // placeholder to keep logo centered (same size as back button)
                Spacer(modifier = Modifier.size(36.dp))
            }

        },
        bottomBar = {
            val isStep1 = state.stage == SignUpStage.Credentials
            println("isStep1: $isStep1")
            AuthButton(
                text = if (isStep1) "Continue" else "Create Account",
                onClick = { onAction(if (isStep1) SignUpAction.Continue else SignUpAction.CreateAccount) },
                enabled = !state.isLoading,
                isLoading = state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            // Simple progress
            LinearProgressIndicator(
                progress = { if (state.stage == SignUpStage.Credentials) 0.5f else 1f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                ,
                color = MaterialTheme.colorScheme.primary,
                trackColor = textFieldColor,
                drawStopIndicator = {}
            )
            Spacer(Modifier.height(12.dp))
            when (state.stage) {
                SignUpStage.Credentials -> {
                    SignUpStep1(
                        state = state,
                        onAction = onAction
                    )
                }
                SignUpStage.Profile -> {
                    SignUpStep2(
                        state = state,
                        onAction = onAction
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}




@Preview(showBackground = true)
@Composable
private fun SignUpScreenStep1Preview() {
    SmartPostTheme {
        SignUpScreen(
            state = SignUpUiState(stage = SignUpStage.Credentials),
            events = emptyFlow(),
            onAction = {},
            onBackPressed = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenStep2Preview() {
    SmartPostTheme {
        SignUpScreen(
            state = SignUpUiState(stage = SignUpStage.Profile),
            events = emptyFlow(),
            onAction = {},
            onBackPressed = {}
        )
    }
}
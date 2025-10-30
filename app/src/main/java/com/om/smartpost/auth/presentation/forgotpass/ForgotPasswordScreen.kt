package com.om.smartpost.auth.presentation.forgotpass

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.om.smartpost.auth.presentation.signin.SignInAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ForgotPasswordScreen(
    state: ForgotUiState,
    events: Flow<ForgotEvent>,
    onAction: (ForgotAction) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier) {

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        events.collect{ event ->
            when(event){
                is ForgotEvent.ValidationErrors -> {
                    snackbarHostState.showSnackbar(event.error.message)
                }
                is ForgotEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.msg)
                }
            }

        }
    }
    Scaffold( snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ){
            Spacer(Modifier.height(16.dp))
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 0.dp,
                modifier = Modifier.size(36.dp)
            ) {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp).align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Forgot Password",
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
                onValueChange = { onAction(ForgotAction.updateEmail(it))},
                label = "Enter your Email",
                isPassword = false,
                keyboardType = KeyboardType.Email ,
                onImeAction = {  } ,
                enabled = !state.isLoading,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            AuthButton(
                text = "Reset Password",
                onClick = { onAction(ForgotAction.ResetPassword) },
                enabled = if (!state.email.isEmpty()) true else false,
                isLoading = state.isLoading
            )

        }

    }

}

@Preview
@Composable
private fun ForgotPasswordScreenPreview() {
    SmartPostTheme {
        ForgotPasswordScreen(
            state = ForgotUiState(),
            events = emptyFlow(),
            onAction = {},
            onBackPressed = {}
        )

    }

}
package com.om.smartpost.auth.presentation.signin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.SmartPostTheme
import com.om.smartpost.R
import com.om.smartpost.auth.presentation.components.AgreementRow
import com.om.smartpost.auth.presentation.components.AuthButton
import com.om.smartpost.auth.presentation.components.AuthTextField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun SignInScreen(
    state: SignInUiState,
    events: Flow<SignInEvent>,
    onAction: (SignInAction) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPass: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier

) {
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val (usernameFocus,passwordFocus) = remember {
        FocusRequester.createRefs()
    }

    val linkColor = MaterialTheme.colorScheme.primary

    val annotated = remember(linkColor) {
        buildAnnotatedString {
            append("Don’t have an account? ")

            val link = LinkAnnotation.Url(
                url = "create_account", // custom tag; not opening a web URL
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = linkColor,
                        textDecoration = TextDecoration.Underline,
                        fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                        fontSize = 12.sp
                    )
                ),
                // Handle the click
                linkInteractionListener = { onNavigateToSignUp() }
            )

            withLink(link) {
                append("Create Account")
            }
        }
    }

    LaunchedEffect(Unit){
        events.collect{ event ->
            println("Event received: $event")
            when (event) {
                is SignInEvent.ValidationErrors -> {
                    snackbarHostState.showSnackbar(event.error.message)
                }
                is SignInEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is SignInEvent.NavigateToHome -> {
                    onNavigateToHome()
                }
            }
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.onPrimaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_app_logo),
                contentDescription = "logo",
                modifier = Modifier.size(120.dp)
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Sign in to your Account",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                modifier = Modifier.align(Alignment.Start).padding(start = 24.dp, end = 24.dp),
                text = "Email or Username",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            )

            AuthTextField(
                value = state.identifier,
                onValueChange = { onAction(SignInAction.UpdateIdentifier(it))},
                label = "Email or username",
                isPassword = false,
                keyboardType = KeyboardType.Text ,
                onImeAction = { passwordFocus.requestFocus() } ,
                enabled = !state.isLoading,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                modifier = Modifier.align(Alignment.Start).padding(start = 24.dp, end = 24.dp),
                text = "Password",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            )

            AuthTextField(
                value = state.password,
                onValueChange = { onAction(SignInAction.UpdatePassword(it))},
                label = "Password",
                isPassword = true,
                isPasswordVisible = state.isPasswordVisible,
                onPasswordToggle = { onAction(SignInAction.TogglePasswordVisibility) },
                enabled = !state.isLoading,
                imeAction = ImeAction.Done,
                onImeAction = { focusManager.clearFocus() },
                modifier = Modifier.focusRequester(usernameFocus)
                    .padding(start = 24.dp, end = 24.dp, top = 8.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                modifier = Modifier.align(Alignment.End).padding(start = 24.dp, end = 24.dp)
                    .clickable {
                         onNavigateToForgotPass()
                    },
                text = "Forgot Password?",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.manrope_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
            )
            Spacer(modifier = Modifier.height(18.dp))

            AuthButton(
                text = "Sign In",
                onClick = { onAction(SignInAction.SignIn) },
                isLoading = state.isLoading,
                enabled = state.identifier.isNotBlank() && state.password.isNotBlank(),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "or sign in with",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.manrope_regular)),
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Social Login buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedIconButton(
                    onClick = { onAction(SignInAction.GoogleSignIn) },
                    modifier = Modifier.size(56.dp),
                    border = BorderStroke(1.dp,Color(0xFF757575))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_google),
                        contentDescription = "Sign in with Google",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified  // keeps original icon colors
                    )
                }

                OutlinedIconButton(
                    onClick = { onAction(SignInAction.FacebookSignIn) },
                    modifier = Modifier.size(56.dp),
                    border = BorderStroke(1.dp,Color(0xFF757575))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_github),
                        contentDescription = "Sign in with Facebook",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified  // keeps original icon colors
                    )
                }

            }

            Spacer(modifier = Modifier.height(14.dp))
            //create account text
            Text(
                text = annotated,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.manrope_regular)),
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                ),
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )





        }
    }

}

val dummyState = SignInUiState(
    identifier = "omp12",
    password = "123456"
)

@Preview
@Composable
private fun SignInScreenPreview() {
    SmartPostTheme {
        SignInScreen(
            dummyState,
            events = emptyFlow(),
            onAction = {},
            onNavigateToSignUp = {},
            onNavigateToForgotPass = {},
            onNavigateToHome = {}
        )
    }

}
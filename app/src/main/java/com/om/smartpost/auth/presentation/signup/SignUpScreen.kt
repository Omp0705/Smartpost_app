package com.om.smartpost.auth.presentation.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.SmartPostTheme
import com.om.smartpost.R
import com.om.smartpost.auth.presentation.components.AgreementRow
import com.om.smartpost.auth.presentation.components.AuthButton
import com.om.smartpost.auth.presentation.components.AuthTextField
import com.om.smartpost.auth.presentation.components.RoleSelector
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
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // FocusRequesters
    val (fullNameRef, usernameRef, emailRef, mobileRef, passwordRef, confirmRef) =
        remember { FocusRequester.createRefs() }

    // UI state for role dropdown (local)
    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("User", "Courier", "Admin") // replace/extend as needed
    val selectedRole = state.selectedRole

    // Snackbar (optional)
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        // collect events if needed (not mandatory)
        events.collect { event ->
            when (event) {
                is SignUpEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
                is SignUpEvent.ValidationFailed -> {}
            }
        }
    }

    Scaffold(
        topBar = {},
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                .padding(start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            // Header (logo + title + back)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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

//                Image(
//                    imageVector = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(R.drawable.ic_app_logo),
//                    contentDescription = "logo",
//                    modifier = Modifier.size(72.dp)
//                )

                Spacer(modifier = Modifier.weight(1f))

                // placeholder to keep logo centered (same size as back button)
                Spacer(modifier = Modifier.size(36.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter details to create your SmartPost account",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.manrope_regular)),
                    fontSize = 14.sp,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
                        alpha = 0.8f
                    )
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Full Name
            AuthLabel(text = "Full name", modifier = Modifier.align(Alignment.Start))
            AuthTextField(
                value = state.fullName,
                onValueChange = { onAction(SignUpAction.UpdateFullName(it)) },
                label = "Enter full name",
                isPassword = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                onImeAction = { usernameRef.requestFocus() },
                enabled = !state.isLoading,
                modifier = Modifier
                    .focusRequester(fullNameRef)
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Username
            AuthLabel(text = "Username", modifier = Modifier.align(Alignment.Start))
            AuthTextField(
                value = state.username,
                onValueChange = { onAction(SignUpAction.UpdateUsername(it)) },
                label = "Choose username",
                isPassword = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                onImeAction = { emailRef.requestFocus() },
                enabled = !state.isLoading,
                modifier = Modifier
                    .focusRequester(usernameRef)
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email
            AuthLabel(text = "Email", modifier = Modifier.align(Alignment.Start))
            AuthTextField(
                value = state.email,
                onValueChange = { onAction(SignUpAction.UpdateEmail(it)) },
                label = "Enter email",
                isPassword = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = { mobileRef.requestFocus() },
                enabled = !state.isLoading,
                modifier = Modifier
                    .focusRequester(emailRef)
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mobile
            AuthLabel(text = "Mobile no", modifier = Modifier.align(Alignment.Start))
            AuthTextField(
                value = state.mobileNo,
                onValueChange = { onAction(SignUpAction.UpdateMobile(it)) },
                label = "Enter mobile number",
                isPassword = false,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                onImeAction = { passwordRef.requestFocus() },
                enabled = !state.isLoading,
                modifier = Modifier
                    .focusRequester(mobileRef)
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            AuthLabel(text = "Password", modifier = Modifier.align(Alignment.Start))
            AuthTextField(
                value = state.password,
                onValueChange = { onAction(SignUpAction.UpdatePassword(it)) },
                label = "Create password",
                isPassword = true,
                isPasswordVisible = state.isPasswordVisible,
                onPasswordToggle = { onAction(SignUpAction.TogglePasswordVisibility(!state.isPasswordVisible)) },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                onImeAction = { confirmRef.requestFocus() },
                enabled = !state.isLoading,
                modifier = Modifier
                    .focusRequester(passwordRef)
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm Password
            AuthLabel(text = "Confirm password", modifier = Modifier.align(Alignment.Start))
            AuthTextField(
                value = state.confirmPassword,
                onValueChange = { onAction(SignUpAction.UpdateConfirmPassword(it)) },
                label = "Confirm password",
                isPassword = true,
                isPasswordVisible = state.isConfirmPasswordVisible,
                onPasswordToggle = { onAction(SignUpAction.ToggleConfirmPasswordVisibility(!state.isConfirmPasswordVisible)) },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onImeAction = {
                    // hide keyboard and attempt submit
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(SignUpAction.Submit)
                },
                enabled = !state.isLoading,
                modifier = Modifier
                    .focusRequester(confirmRef)
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Role selector (simple dropdown)
            AuthLabel(text = "Role", modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(4.dp))
            RoleSelector(
                selectedRole = state.selectedRole,
                roles = listOf("Receiver", "Postman", "Admin"),
                onSelect = { onAction(SignUpAction.SelectRole(it)) },
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Agreement Row (custom component you will provide)
            Spacer(modifier = Modifier.height(6.dp))
            AgreementRow(
                checked = state.isAgreementChecked,
                onCheckChanged = { onAction(SignUpAction.ToggleAgreement(it)) },
                onOpen = {  }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Submit button
            AuthButton(
                text = "Create Account",
                onClick = { onAction(SignUpAction.Submit) },
                enabled = !state.isLoading &&
                        state.fullName.isNotBlank() &&
                        state.username.isNotBlank() &&
                        state.email.isNotBlank() &&
                        state.password.isNotBlank() &&
                        state.confirmPassword.isNotBlank() &&
                        state.isAgreementChecked &&
                        state.password == state.confirmPassword,
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

// small helper for labels
@Composable
private fun AuthLabel(text: String, modifier: Modifier) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.manrope_semibold)),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.9f
            )
        ),
        modifier = modifier

    )
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    SmartPostTheme {
        SignUpScreen(
            state = SignUpUiState(),
            events = emptyFlow(),
            onAction = {},
            onBackPressed = {}
        )
    }
}
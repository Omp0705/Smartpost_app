package com.om.smartpost.auth.presentation.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.om.smartpost.auth.presentation.components.AuthTextField
import com.om.smartpost.core.presentation.utils.AuthLabel

@Composable
fun SignUpStep2(
    state: SignUpUiState,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier) {
    val (nameRef, usernameRef,) = remember { FocusRequester.createRefs() }

    AuthLabel(text = "Full name", modifier = Modifier)
    AuthTextField(
        value = state.fullName,
        onValueChange = { onAction(SignUpAction.UpdateFullName(it)) },
        label = "Enter full name",
        isPassword = false,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next,
        onImeAction = { usernameRef.requestFocus() },
        enabled = !state.isLoading,
        supportingText = state.fullNameError,
        modifier = Modifier
            .focusRequester(nameRef)
            .padding(top = 8.dp)
    )
    Spacer(Modifier.height(12.dp))

    AuthLabel(text = "Username", modifier = Modifier)
    AuthTextField(
        value = state.username,
        onValueChange = { onAction(SignUpAction.UpdateUsername(it)) },
        label = "Choose username",
        isPassword = false,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done,
        onImeAction = { /* handled by CTA */ },
        enabled = !state.isLoading,
        supportingText = state.usernameError, // set via debounce/availability check
        modifier = Modifier
            .focusRequester(usernameRef)
            .padding(top = 8.dp)
    )

    Spacer(Modifier.height(12.dp))
    AuthLabel(text = "Role", modifier = Modifier)
    Spacer(Modifier.height(4.dp))
    Column {
        listOf("Receiver","Postman","Admin").forEach{ role ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !state.isLoading) { onAction(SignUpAction.SelectRole(role)) }
                    .padding(vertical = 6.dp)
            ){
                RadioButton(
                    selected = state.selectedRole == role,
                    onClick = { onAction(SignUpAction.SelectRole(role)) },
                    enabled = !state.isLoading
                )
                Text(text = role, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }

}

@Preview
@Composable
private fun SignUpStep2Preview() {
    SignUpStep2(
        state = SignUpUiState(),
        onAction = {}
    )
}
package com.om.smartpost.auth.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.SmartPostTheme

private const val OTP_LENGTH = 5

@Composable
fun OtpInputField(
    otpCode: String,
    onCodeChanged: (String) -> Unit,
    isEnabled: Boolean,
    onImeAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequesters = remember { List(OTP_LENGTH) { FocusRequester() } }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Iterate through the required number of digits (5 in this case)
        repeat(OTP_LENGTH) { index ->
            val char = otpCode.getOrNull(index)?.toString() ?: ""

            // 💡 NEW LOGIC: Check if the field is filled OR if it's the currently focused (active) field.
            val isFilled = char.isNotEmpty()
            val isCurrentFocus = index == otpCode.length.coerceAtMost(OTP_LENGTH)

            val borderColor = when {
                !isEnabled -> MaterialTheme.colorScheme.surfaceVariant // Disabled state
                isFilled || isCurrentFocus -> MaterialTheme.colorScheme.primary // Filled or Focused (Primary color)
                else -> MaterialTheme.colorScheme.surfaceVariant // Empty and unfocused
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .weight(1f)
                    .width(0.dp) // Ensure even weighting
                    .border(
                        width = 1.dp,
                        color = borderColor, // 💡 Use calculated border color
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                TextField(
                    value = TextFieldValue(char, selection = TextRange(char.length)),
                    onValueChange = { textFieldValue ->
                        val newChar = textFieldValue.text
                        if (newChar.length <= 1) {
                            val newOtp = otpCode.toMutableList()

                            if (newChar.isNotEmpty()) {
                                // Add or replace the digit
                                if (index < newOtp.size) {
                                    newOtp[index] = newChar[0]
                                } else {
                                    newOtp.add(newChar[0])
                                }
                                onCodeChanged(newOtp.joinToString(""))

                                // Move focus to the next field
                                if (index < OTP_LENGTH - 1) {
                                    focusManager.moveFocus(FocusDirection.Next)
                                } else {
                                    // If last field is filled, hide keyboard and run IME action
                                    focusManager.clearFocus()
                                    onImeAction()
                                }
                            } else if (newChar.isEmpty() && index >= 0) {
                                // Handle backspace on an empty field: go back and clear the previous field
                                if (char.isEmpty() && index > 0) {
                                    focusManager.moveFocus(FocusDirection.Previous)
                                    // Remove character at previous index
                                    val newCode = otpCode.substring(0, index - 1) + otpCode.substring(index)
                                    onCodeChanged(newCode.filter { it.isDigit() })
                                } else {
                                    // Clear current field (this handles backspace on filled field)
                                    val newCode = otpCode.removeRange(index, index + 1)
                                    onCodeChanged(newCode)
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .focusRequester(focusRequesters[index]),
                    textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                    singleLine = true,
                    enabled = isEnabled,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = if (index == OTP_LENGTH - 1) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) },
                        onDone = {
                            focusManager.clearFocus()
                            onImeAction()
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
                )
            }
            // Add a small spacer between fields, except after the last one
            if (index < OTP_LENGTH - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }

    // Ensure the first field is focused when the composable first loads
    LaunchedEffect(Unit) {
        focusRequesters.firstOrNull()?.requestFocus()
    }
}


@Preview
@Composable
private fun OtpFieldPreview() {
    SmartPostTheme {
        OtpInputField(
            otpCode = "12",
            onCodeChanged = {},
            isEnabled = true,
            onImeAction = {},
        )
    }

}
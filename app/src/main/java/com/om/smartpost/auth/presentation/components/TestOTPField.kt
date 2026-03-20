package com.om.smartpost.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.SmartPostTheme

@Composable
fun OtpTextField(
    otpText: String,
    onOtpTextChange: (String, Boolean) -> Unit,
    otpCount: Int = 4
) {
    val focusRequesters = remember { List(otpCount) { FocusRequester() } }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(otpCount) { index ->
            // Get the character at this index (or empty string if not typed yet)
            val char = if (index < otpText.length) otpText[index].toString() else ""

            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            ) {
                OtpCharBox(
                    text = char,
                    focusRequester = focusRequesters[index],
                    onValueChange = { newValue ->
                        // LOGIC: Handle typing a character
                        if (newValue.length <= 1) {
                            // Case 1: User types a single number
                            handleOtpChange(
                                otpText = otpText,
                                index = index,
                                newChar = newValue,
                                otpCount = otpCount,
                                onOtpTextChange = onOtpTextChange
                            )
                            if (newValue.isNotEmpty() && index < otpCount - 1) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        } else {
                            // Case 2: User types while box is full (Overwrite logic)
                            // newValue might be "45" (old '4', new '5'). We want '5'.
                            val lastChar = newValue.last().toString()
                            handleOtpChange(
                                otpText = otpText,
                                index = index,
                                newChar = lastChar,
                                otpCount = otpCount,
                                onOtpTextChange = onOtpTextChange
                            )
                            if (index < otpCount - 1) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        }
                    },
                    onBackspace = {
                        // LOGIC: Handle Backspace
                        if (char.isEmpty() && index > 0) {
                            // Move back and delete previous
                            focusRequesters[index - 1].requestFocus()
                            handleOtpChange(otpText, index - 1, "", otpCount, onOtpTextChange)
                        } else {
                            // Delete current
                            handleOtpChange(otpText, index, "", otpCount, onOtpTextChange)
                        }
                    }
                )
            }
        }
    }
}

/**
 * Helper function to manipulate the string at a specific index
 */
private fun handleOtpChange(
    otpText: String,
    index: Int,
    newChar: String,
    otpCount: Int,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    // Convert current string to mutable char list, padded with spaces if needed
    val currentChars = otpText.toCharArray().toMutableList()

    // Ensure list is large enough to reach 'index'
    while (currentChars.size <= index) {
        currentChars.add(' ') // Add placeholder for gaps
    }

    if (newChar.isNotEmpty()) {
        currentChars[index] = newChar[0]
    } else {
        // If deleting, we can just replace with space or remove.
        // Ideally in OTP, if we delete index 2 in "1234", it becomes "12 4" (gap)
        // But usually, apps expect sequential strings.
        // Simple approach: Remove the char at this index.
        if (index < currentChars.size) {
            currentChars.removeAt(index)
        }
    }

    // Convert back to string, removing any placeholder spaces used for logic
    // This ensures "1 2" becomes "12"
    val newString = currentChars.joinToString("").replace(" ", "")

    // Notify parent
    onOtpTextChange(newString, newString.length == otpCount)
}

@Composable
private fun OtpCharBox(
    text: String,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit
) {
    // FIX: We need to track focus state locally to change the border color
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = if (isFocused) MaterialTheme.colorScheme.primary else Color.LightGray

    BasicTextField(
        value = TextFieldValue(
            text = text,
            selection = TextRange(text.length) // Cursor at end
        ),
        onValueChange = { onValueChange(it.text) },
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused } // FIX: Update focus state here
            .onKeyEvent { event ->
                if (event.key == Key.Backspace) {
                    onBackspace()
                    false
                } else {
                    false
                }
            },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(Color.White, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun OtpTextFiledPreview() {
    SmartPostTheme {
        OtpTextField(
            otpText = "12",
            onOtpTextChange = { otp,isComplete ->

            },
            otpCount = 4
        )
    }
}
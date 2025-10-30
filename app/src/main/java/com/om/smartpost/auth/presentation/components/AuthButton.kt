package com.om.smartpost.auth.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.SmartPostTheme
import com.example.ui.theme.bodyFontFamily


@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = text,
                    style = TextStyle(
                        fontFamily = bodyFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AuthButtonPreview() {
    SmartPostTheme  {
        Column(modifier = Modifier.padding(16.dp)) {
            AuthButton(
                text = "SignIn",
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthButtonLoadingPreview() {
    SmartPostTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            AuthButton(
                text = "SignIn",
                onClick = {},
                isLoading = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthButtonDisabledPreview() {
    SmartPostTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            AuthButton(
                text = "SignIn",
                onClick = {},
                enabled = false
            )
        }
    }
}

package com.om.smartpost.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.om.smartpost.auth.presentation.signup.PasswordStrength

@Composable
fun PasswordStrengthIndicator(
    strength: PasswordStrength,
    modifier: Modifier = Modifier,
    segmentCount: Int = 4
) {
    // Map strength to active segments
    val activeSegments = when (strength) {
        PasswordStrength.NONE -> 0
        PasswordStrength.POOR -> 1
        PasswordStrength.MEDIUM -> 2
        PasswordStrength.GOOD -> 3
        PasswordStrength.STRONG -> 4
    }
    // Colors for each segment
    val colors = listOf(
        Color(0xFFD32F2F), // Red
        Color(0xFFFF9800), // Orange
        Color(0xFFFFC107), // Amber
        Color(0xFF4CAF50)  // Green
    )

    Column(modifier = modifier) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            repeat(segmentCount) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (index < activeSegments) colors[index] else Color(0xFFE0E0E0)
                        )
                )
            }
        }
        if (strength != PasswordStrength.NONE) {
            Text(
                text = strength.label,
                style = MaterialTheme.typography.bodySmall,
                color = colors[activeSegments.coerceAtLeast(1) - 1],
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun NonePasswordStrengthPreview() {
    PasswordStrengthIndicator(
        strength = PasswordStrength.NONE
    )
}

@Preview(showBackground = true)
@Composable
private fun PoorPasswordStrengthPreview() {
    PasswordStrengthIndicator(
        strength = PasswordStrength.POOR
    )
}
@Preview(showBackground = true)
@Composable
private fun MediumPasswordStrengthPreview() {
    PasswordStrengthIndicator(
        strength = PasswordStrength.MEDIUM
    )
}
@Preview(showBackground = true)
@Composable
private fun StrongPasswordStrengthPreview() {
    PasswordStrengthIndicator(
        strength = PasswordStrength.STRONG
    )
}
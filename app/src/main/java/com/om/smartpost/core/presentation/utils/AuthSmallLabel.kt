package com.om.smartpost.core.presentation.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.om.smartpost.R

@Composable
public fun AuthLabel(text: String, modifier: Modifier) {
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
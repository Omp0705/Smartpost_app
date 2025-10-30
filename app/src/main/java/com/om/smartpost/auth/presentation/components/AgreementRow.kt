package com.om.smartpost.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.SmartPostTheme
import com.om.smartpost.R

@Composable
fun AgreementRow(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit,
    onOpen: (String) -> Unit = {}
) {
    val linkColor = MaterialTheme.colorScheme.primary
    val linkTextStyle = SpanStyle(
        color = linkColor,
        fontFamily = FontFamily(Font(R.font.manrope_semibold)),
        textDecoration = TextDecoration.Underline,
        fontWeight = FontWeight.SemiBold
    )
    val uriHandler = LocalUriHandler.current

    val annotated = remember(linkColor) {
        buildAnnotatedString {
            append("I've read and agreed to ")
            val userAgreement = LinkAnnotation.Url(
                url = "user_agreement"
            ){ ann ->
                val url = (ann as LinkAnnotation.Url).url
                if(url.startsWith("http")) uriHandler.openUri(url) else onOpen(url)
            }
            withLink(userAgreement) { withStyle(linkTextStyle) {append("User Agreement")} }
            append(" and ")

            // "Privacy Policy" link
            val privacyPolicy = LinkAnnotation.Url(
                url = "privacy_policy"
            ) { ann ->
                val url = (ann as LinkAnnotation.Url).url
                if (url.startsWith("http")) uriHandler.openUri(url) else onOpen(url)
            }
            withLink(privacyPolicy) { withStyle(linkTextStyle) { append("Privacy Policy") } }
        }

    }
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckChanged,
            modifier = Modifier.padding(end = 8.dp).clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.width(8.dp))

        Text(
            text = annotated,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_medium)),
                fontSize = 14.sp
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
private fun AgreementRowPreview() {
    SmartPostTheme {
        AgreementRow(
            checked = true,
            onCheckChanged = {},
            onOpen = {}
        )
    }
}
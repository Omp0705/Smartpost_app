package com.om.smartpost.customer.home.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.om.smartpost.R
import com.om.smartpost.customer.home.domain.model.PackageItem

@Composable
fun OutForDeliveryCard(
    item: PackageItem,
    modifier: Modifier = Modifier
) {
    val (bgColor, mainColor) = StatusUtils.getStatusColors(item.status)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.height(130.dp) // Fixed height for consistency or use IntrinsicSize
        ) {
            // Colored Strip at the start
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(mainColor)
            )

            // Content
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Top Row: Tracking ID and Status Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.trackingNumber,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        ),
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    StatusBadge(status = item.status)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // From Section
                Text(
                    text = buildAnnotatedString {
                        append("From: ")
                        withStyle(style = SpanStyle(color = Color.DarkGray)) {
                            append(item.senderOrSource)
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.weight(1f)) // Push next row to bottom

                // Bottom Row: Time and Postman
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Time with Icon
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_clock),
                        contentDescription = null,
                        tint = mainColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.timeOrDate,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = mainColor,
                         maxLines = 1,
                         overflow = TextOverflow.Ellipsis,
                         modifier = Modifier.weight(1f)
                    )

                    // Postman Name
                    if (item.postmanName != null) {
                        Text(
                            text = buildAnnotatedString {
                                append("Postman: ")
                                withStyle(style = SpanStyle(color = Color.DarkGray)) {
                                    append(item.postmanName)
                                }
                            },
                             style = MaterialTheme.typography.bodySmall,
                             color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

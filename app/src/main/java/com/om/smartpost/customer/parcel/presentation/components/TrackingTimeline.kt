package com.om.smartpost.customer.parcel.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.om.smartpost.R
import com.om.smartpost.customer.parcel.domain.model.ShipmentStatus
import com.om.smartpost.customer.parcel.domain.model.TrackingEvent
import java.time.format.DateTimeFormatter

@Composable
fun TrackingTimeline(events: List<TrackingEvent>, currentStatus: ShipmentStatus, modifier: Modifier = Modifier) {

    // Check if the parcel has reached a final state so we stop pulsing
    val isTerminalState = currentStatus in listOf(
        ShipmentStatus.DELIVERED,
        ShipmentStatus.CANCELLED,
        ShipmentStatus.DELIVERY_FAILED,
        ShipmentStatus.RETURN_TO_SENDER
    )

    // Find the latest occurrence of the current status
    // If we can't find it, we default to the last item index
    val currentEventIndex = events.indexOfLast { it.status == currentStatus }.takeIf { it >= 0 } ?: events.lastIndex

    Column(modifier = modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.location_ic),
                contentDescription = null,
                tint = Color(0xFF6A1B31),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tracking Timeline",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        events.forEachIndexed { index, event ->
            // Logic to determine dot states dynamically
            val isLastInList = index == events.lastIndex

            val isCurrent = (index == currentEventIndex) && !isTerminalState
            val isCompleted = index < currentEventIndex || (isTerminalState && index == currentEventIndex)

            TrackingTimelineItem(
                event = event,
                isLastItem = isLastInList,
                isCurrent = isCurrent,
                isCompleted = isCompleted
            )
        }
    }
}

@Composable
fun TrackingTimelineItem(
    event: TrackingEvent,
    isLastItem: Boolean,
    isCurrent: Boolean,   // Passed in dynamically now
    isCompleted: Boolean  // Passed in dynamically now
) {
    val maroonText = Color(0xFF6A1B31)

    val dotColor = if (isCompleted || isCurrent) maroonText else Color.Gray
    val isOutlineDot = !isCompleted && !isCurrent

    Row(modifier = Modifier.padding(bottom = 0.dp)) {

        // Timeline graphic column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            // Status Dot
            if (isOutlineDot) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color.Transparent, CircleShape)
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.White, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.Transparent, CircleShape)
                            .padding(2.dp)
                    )
                }
                Canvas(modifier = Modifier.size(12.dp)) {
                    drawCircle(
                        color = Color.LightGray,
                        radius = size.minDimension / 2f,
                        style = Stroke(width = 4f)
                    )
                }
            } else if (isCurrent) {
                val infiniteTransition = rememberInfiniteTransition(label = "pulsing_dot")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.8f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale"
                )
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.7f,
                    targetValue = 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "alpha"
                )

                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp * scale)
                            .clip(CircleShape)
                            .background(maroonText.copy(alpha = alpha))
                    )
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(maroonText)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(maroonText)
                )
            }

            // Connecting Line
            if (!isLastItem) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(if (isCompleted) maroonText else Color(0xFFE0E0E0))
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Content Column on the right
        Column(modifier = Modifier.padding(bottom = if (isLastItem) 0.dp else 24.dp).weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    // Replaced event.statusTitle with the Enum's displayName
                    text = event.status.displayName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = if (isCompleted || isCurrent) Color.Black else Color.DarkGray,
                    modifier = Modifier.weight(1f)
                )

                // Added the timestamp formatting so the user knows WHEN it happened!
                Text(
                    text = event.timestamp.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = event.description,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = Color.Gray
            )

            // Added the location string we get from the backend
            if (event.location.isNotEmpty()) {
                Text(
                    text = event.location,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color.LightGray
                )
            }
        }
    }
}
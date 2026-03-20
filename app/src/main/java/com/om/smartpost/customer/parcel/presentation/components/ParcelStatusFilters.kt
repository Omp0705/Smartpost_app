package com.om.smartpost.customer.parcel.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class ParcelFilter(val displayName: String) {
    ALL("All"),
    INCOMING("Incoming"),
    SENT("Sent"),
    DELIVERED("Delivered")
}

private fun getFilterColors(isSelected: Boolean): Pair<Color, Color> {
    return if (isSelected) {
        Color(0xFF8D1C1C) to Color.White // Maroon brand color
    } else {
        Color(0xFFF6F0ED) to Color(0xFF8D1C1C) // Unselected tan
    }
}

@Composable
fun ParcelStatusFilters(
    selectedStatus: ParcelFilter,
    onStatusSelected: (ParcelFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(ParcelFilter.values()) { filter ->
            val isSelected = selectedStatus == filter
            val (bgColor, textColor) = getFilterColors(isSelected)
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(bgColor)
                    .clickable { onStatusSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = filter.displayName,
                    color = textColor,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

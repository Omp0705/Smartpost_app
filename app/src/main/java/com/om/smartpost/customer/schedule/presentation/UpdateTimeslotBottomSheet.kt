package com.om.smartpost.customer.schedule.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.om.smartpost.customer.parcel.domain.model.Shipment
import com.om.smartpost.customer.profile.data.dto.TimeSlot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTimeslotBottomSheet(
    shipment: Shipment,
    isUpdating: Boolean,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var selectedTimeslot by remember { mutableStateOf(shipment.predictedSlot ?: "10:00 AM - 12:00 PM") }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Update Timeslot",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (isUpdating) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Button(onClick = { onSave(selectedTimeslot) }) {
                        Text("Save")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Tracking ID: ${shipment.trackingNumber}", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))

            Text("Select New Timeslot", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            var isExpanded by remember { mutableStateOf(false) }
            Box {
                OutlinedTextField(
                    value = selectedTimeslot,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                    modifier = Modifier.fillMaxWidth().clickable { isExpanded = true }
                )
                Box(modifier = Modifier.matchParentSize().clickable { isExpanded = true })

                DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                    TimeSlot.values().forEach { timeSlot ->
                        val displayLabel = when(timeSlot) {
                            TimeSlot.SLOT_10_12 -> "10:00 AM - 12:00 PM"
                            TimeSlot.SLOT_12_02 -> "12:00 PM - 02:00 PM"
                            TimeSlot.SLOT_02_04 -> "02:00 PM - 04:00 PM"
                            TimeSlot.SLOT_04_06 -> "04:00 PM - 06:00 PM"
                        }
                        DropdownMenuItem(
                            text = { Text(displayLabel) },
                            onClick = {
                                selectedTimeslot = displayLabel
                                isExpanded = false
                            },
                            trailingIcon = if(selectedTimeslot == displayLabel) {
                                { Icon(Icons.Default.Check, null) }
                            } else null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

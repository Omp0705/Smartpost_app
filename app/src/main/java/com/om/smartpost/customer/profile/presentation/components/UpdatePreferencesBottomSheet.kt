package com.om.smartpost.customer.profile.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.om.smartpost.customer.profile.data.dto.TimeSlot
import com.om.smartpost.customer.profile.domain.model.DeliveryPreferences
import com.om.smartpost.customer.profile.domain.model.DeliverySlot
import com.om.smartpost.customer.profile.presentation.ProfileAction
import com.om.smartpost.customer.profile.presentation.ProfileState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePreferencesBottomSheet(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    // Reference the nested state and the draft preferences
    val preferencesState = state.preferencesSheetState
    val draft = preferencesState.draft ?: return // Don't render if draft is null

    ModalBottomSheet(
        onDismissRequest = { onAction(ProfileAction.Preference.Close) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // --- HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Update Preferences",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (preferencesState.isUpdating) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Button(onClick = { onAction(ProfileAction.Preference.Save) }) {
                        Text("Save")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- TIME SLOT SELECTOR ---
            Text("Preferred Delivery Slot", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            var isExpanded by remember { mutableStateOf(false) }
            Box {
                OutlinedTextField(
                    value = draft.preferredDeliverySlot.label,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                    modifier = Modifier.fillMaxWidth().clickable { isExpanded = true }
                )
                // Overlay for click detection on readOnly field
                Box(modifier = Modifier.matchParentSize().clickable { isExpanded = true })

                DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                    // Logic to iterate through Slot constants
                    TimeSlot.values().forEach { timeSlot ->
                        // Create the label based on the slot (or use a helper)
                        val displayLabel = when(timeSlot) {
                            TimeSlot.SLOT_10_12 -> "10:00 AM - 12:00 PM"
                            TimeSlot.SLOT_12_02 -> "12:00 PM - 02:00 PM"
                            TimeSlot.SLOT_02_04 -> "02:00 PM - 04:00 PM"
                            TimeSlot.SLOT_04_06 -> "04:00 PM - 06:00 PM"
                        }
                        DropdownMenuItem(
                            text = { Text(displayLabel) },
                            onClick = {
                                // Update draft with a NEW DeliverySlot object
                                val newSlot = DeliverySlot(code = timeSlot, label = displayLabel)
                                onAction(ProfileAction.Preference.UpdateDraft(draft.copy(preferredDeliverySlot = newSlot)))
                                isExpanded = false
                            },
                            trailingIcon = if(timeSlot == draft.preferredDeliverySlot.code) {
                                { Icon(Icons.Default.Check, null) }
                            } else null
                        )
                    }

                }
            }

            // --- TOGGLES SECTION ---
            SectionHeader("Delivery Options")

            PreferenceSwitchItem("Leave at Door", draft.leaveAtDoor) {
                onAction(ProfileAction.Preference.UpdateDraft(draft.copy(leaveAtDoor = it)))
            }
            PreferenceSwitchItem("Leave with Guard", draft.leaveWithGuard) {
                onAction(ProfileAction.Preference.UpdateDraft(draft.copy(leaveWithGuard = it)))
            }
            PreferenceSwitchItem("Deliver to Neighbor", draft.deliverToNeighbor) {
                onAction(ProfileAction.Preference.UpdateDraft(draft.copy(deliverToNeighbor = it)))
            }
            PreferenceSwitchItem("OTP Required", draft.otpRequired) {
                onAction(ProfileAction.Preference.UpdateDraft(draft.copy(otpRequired = it)))
            }

            SectionHeader("Timing Constraints")

            PreferenceSwitchItem("Avoid Morning", draft.avoidMorning) {
                onAction(ProfileAction.Preference.UpdateDraft(draft.copy(avoidMorning = it)))
            }
            PreferenceSwitchItem("Weekend Only", draft.weekendOnly) {
                onAction(ProfileAction.Preference.UpdateDraft(draft.copy(weekendOnly = it)))
            }

            // --- NOTE FIELD ---
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = draft.deliveryNote ?: "",
                onValueChange = { onAction(ProfileAction.Preference.UpdateDraft(draft.copy(deliveryNote = it))) },
                label = { Text("Delivery Note / Instructions") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = title, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
    }
}
@Composable
fun PreferenceSwitchItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

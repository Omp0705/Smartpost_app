package com.om.smartpost.customer.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.om.smartpost.customer.profile.presentation.AddressField
import com.om.smartpost.customer.profile.presentation.ProfileAction
import com.om.smartpost.customer.profile.presentation.ProfileState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressBottomSheet(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    // Reference the nested state to keep the code clean
    val formState = state.addAddressFromState

    ModalBottomSheet(
        onDismissRequest = { onAction(ProfileAction.Address.Close) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (formState.editingAddressId == null) "Add New Address" else "Edit Address",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                if (formState.isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Button(onClick = { onAction(ProfileAction.Address.Save) }) {
                        Text("Save")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Address Type (Now maps to Enum/String logic we fixed)
            Text("Address Type", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                com.om.smartpost.customer.profile.domain.model.AddressType.entries.forEach { type ->
                    androidx.compose.material3.FilterChip(
                        selected = formState.addressType == type,
                        onClick = { onAction(ProfileAction.Address.EditField(AddressField.TYPE, type.name)) },
                        label = { Text(type.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Address Line 1
            OutlinedTextField(
                value = formState.addressLine1,
                onValueChange = { onAction(ProfileAction.Address.EditField(AddressField.LINE1, it)) },
                label = { Text("Address Line 1") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Address Line 2
            OutlinedTextField(
                value = formState.addressLine2,
                onValueChange = { onAction(ProfileAction.Address.EditField(AddressField.LINE2, it)) },
                label = { Text("Address Line 2 (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // City and Pincode Row
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = formState.city,
                    onValueChange = { onAction(ProfileAction.Address.EditField(AddressField.CITY, it)) },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedTextField(
                    value = formState.pincode,
                    onValueChange = { onAction(ProfileAction.Address.EditField(AddressField.PINCODE, it)) },
                    label = { Text("Pincode") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
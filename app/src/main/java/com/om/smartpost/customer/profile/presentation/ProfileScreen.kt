package com.om.smartpost.customer.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.om.smartpost.R
import com.om.smartpost.customer.profile.domain.model.DeliverySlot
import com.om.smartpost.customer.profile.presentation.components.AddAddressBottomSheet
import com.om.smartpost.customer.profile.presentation.components.AddressSection
import com.om.smartpost.customer.profile.presentation.components.ExpandableProfileOptionItem
import com.om.smartpost.customer.profile.presentation.components.LogoutDialog
import com.om.smartpost.customer.profile.presentation.components.ProfileHeader
import com.om.smartpost.customer.profile.presentation.components.ProfileOptionItem
import com.om.smartpost.customer.profile.presentation.components.ShimmerProfile
import com.om.smartpost.customer.profile.presentation.components.UpdatePreferencesBottomSheet
import kotlinx.coroutines.flow.Flow

@Composable
fun ProfileScreen(
    state: ProfileState,
    events: Flow<ProfileEvent>,
    onAction: (ProfileAction) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToAddAddress: () -> Unit
) {


    LaunchedEffect(key1 = true) {
        events.collect { event ->
            when(event) {
                is ProfileEvent.NavigateToLogin -> onNavigateToLogin()
                is ProfileEvent.ShowSnackbar -> {

                }
                is ProfileEvent.NavigateToAddAddress -> onNavigateToAddAddress()
            }
        }
    }
    if (state.isLoading) {
        ShimmerProfile()
    } else if (state.userProfile != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Profile Card
            ProfileHeader(user = state.userProfile)

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Addresses
            AddressSection(
                addresses = state.userProfile.addresses,
                onAddClick = { onAction(ProfileAction.Address.OpenForNew) },
                onAction = onAction
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Preferences Section
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            ProfileOptionItem(
                icon = Icons.Default.Settings,
                title = "Delivery Preferences",
                subtitle = state.userProfile.deliveryPreferences?.let { prefs ->
                     listOfNotNull(
                        prefs.preferredDeliverySlot.label,
                        if (prefs.leaveAtDoor) "Leave at door" else null,
                        if (prefs.callBeforeDelivery) "Call before delivery" else null,
                        prefs.deliveryNote
                    ).joinToString(", ").ifEmpty { "None" }
                } ?: "Configure preferences",
                onClick = { onAction(ProfileAction.Preference.Open) }
            )

             Spacer(modifier = Modifier.height(24.dp))
             
             // ... Security ...
 
             Spacer(modifier = Modifier.height(24.dp))
 
             // 4. Security Section
             Text(
                 text = "Privacy & Security",
                 style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                 modifier = Modifier.padding(horizontal = 16.dp)
             )
             Spacer(modifier = Modifier.height(8.dp))
             ProfileOptionItem(
                 icon = Icons.Default.Lock,
                 title = "Change Password",
                 onClick = { /* Handle change password */ },
                 showDivider = false
             )
             
             Spacer(modifier = Modifier.height(32.dp))
 
             // 5. Logout
             Button(
                 onClick = { onAction(ProfileAction.Logout) },
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(horizontal = 16.dp)
                     .height(50.dp),
                 colors = ButtonDefaults.buttonColors(
                     containerColor = Color(0xFFFFEBEE), // Light Red
                     contentColor = Color(0xFFD32F2F) // Dark Red for text/icon
                 ),
                 shape = RoundedCornerShape(12.dp),
                 elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
             ) {
                 Icon(
                     imageVector = ImageVector.vectorResource(R.drawable.ic_logout),
                     contentDescription = null,
                     modifier = Modifier.size(20.dp)
                 )
                 Spacer(modifier = Modifier.width(8.dp))
                 Text(
                     text = "Logout",
                     fontWeight = FontWeight.Bold,
                     style = MaterialTheme.typography.titleMedium
                 )
             }
         }
        if (state.preferencesSheetState.isVisible) {
            UpdatePreferencesBottomSheet(
                state = state,
                onAction = onAction
            )
        }
     }
 
     if (state.isLogoutDialogVisible) {
         LogoutDialog(
             onDismiss = { onAction(ProfileAction.DismissLogout) },
             onConfirm = { onAction(ProfileAction.ConfirmLogout) }
         )
     }
     
     if (state.addAddressFromState.isVisible) {
         AddAddressBottomSheet(
             state = state,
             onAction = onAction
         )
     }
     


}

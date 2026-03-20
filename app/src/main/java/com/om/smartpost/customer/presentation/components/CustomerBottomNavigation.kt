package com.om.smartpost.customer.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.om.smartpost.R
import com.om.smartpost.navigation.NavRoutes

sealed class CustomerBottomNavItem(
    val route: String,
    val title: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
) {
    object Home : CustomerBottomNavItem(
        route = NavRoutes.CUSTOMER_HOME,
        title = "Home",
        selectedIcon = R.drawable.house_chimney_24,
        unselectedIcon = R.drawable.house_chimney_24
    )
    object Parcel : CustomerBottomNavItem(
        route = NavRoutes.CUSTOMER_PARCEL,
        title = "Parcel",
        selectedIcon = R.drawable.box_24,
        unselectedIcon = R.drawable.box_24
    )
    // Send is special, handled separately
    object Send : CustomerBottomNavItem(
        route = NavRoutes.CUSTOMER_SEND,
        title = "Send",
        selectedIcon = R.drawable.add_24,
        unselectedIcon = R.drawable.add_24
    )
    object Schedule : CustomerBottomNavItem(
        route = NavRoutes.CUSTOMER_SCHEDULE,
        title = "Schedule",
        selectedIcon = R.drawable.calendar_24,
        unselectedIcon = R.drawable.calendar_24
    )
    object Profile : CustomerBottomNavItem(
        route = NavRoutes.CUSTOMER_PROFILE,
        title = "Profile",
        selectedIcon = R.drawable.circle_user_24,
        unselectedIcon = R.drawable.circle_user_24
    )
}

@Composable
fun CustomerBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        CustomerBottomNavItem.Home,
        CustomerBottomNavItem.Parcel,
        CustomerBottomNavItem.Send,
        CustomerBottomNavItem.Schedule,
        CustomerBottomNavItem.Profile
    )

    val primaryMaroon = Color(0xFF8D1C1C)
    val inactiveGray = Color(0xFF757575)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            color = androidx.compose.ui.graphics.Color.White,
            tonalElevation = 8.dp,
            shadowElevation = 16.dp, // Adds depth below the bar
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .navigationBarsPadding() // Ensures it doesn't overlap system nav
                    .padding(top = 8.dp, bottom = 8.dp) // Proper padding at the top
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    items.forEach { item ->
                        val isSelected = currentRoute == item.route

                        Column(
                            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    // Removes the grayish click ripple effect
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    onNavigate(item.route)
                                }
                        ) {
                            if (item == CustomerBottomNavItem.Send) {
                                // Empty space for the FAB to sit in
                                Spacer(modifier = Modifier.size(48.dp))
                            } else {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(width = 56.dp, height = 36.dp)
                                        .background(
                                            color = if (isSelected) primaryMaroon.copy(alpha = 0.1f) else Color.Transparent,
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                                12.dp
                                            )
                                        )
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) ImageVector.vectorResource(item.selectedIcon) else ImageVector.vectorResource(item.unselectedIcon),
                                        contentDescription = item.title,
                                        tint = if (isSelected) primaryMaroon else inactiveGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(4.dp))

                            // Text aligned across all items
                            Text(
                                text = item.title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected || item == CustomerBottomNavItem.Send)
                                    androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium,
                                color = if (isSelected || item == CustomerBottomNavItem.Send) primaryMaroon else inactiveGray
                            )
                        }
                    }
                }


            }
        }
        Box(
            modifier = Modifier
                .offset(y = (-40).dp) // Lifts it above the white bar
                .size(60.dp)
                .shadow(8.dp, androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                .background(
                    color = primaryMaroon,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                )
                .clickable { onNavigate(CustomerBottomNavItem.Send.route) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }

}

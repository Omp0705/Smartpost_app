package com.om.smartpost.postman.presentation.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.om.smartpost.R
import com.om.smartpost.navigation.NavRoutes

sealed class PostmanBottomNavItem(
    val route: String,
    val title: String,
    @DrawableRes val iconRes: Int
) {
    object Home : PostmanBottomNavItem(
        route = NavRoutes.POSTMAN_HOME,
        title = "Home",
        iconRes = R.drawable.house_chimney_24
    )
    object Parcels : PostmanBottomNavItem(
        route = NavRoutes.POSTMAN_PARCELS,
        title = "Parcels",
        iconRes = R.drawable.box_24
    )
    object Route : PostmanBottomNavItem(
        route = NavRoutes.POSTMAN_ROUTE_TAB,
        title = "Route",
        iconRes = R.drawable.location_ic
    )
    object Profile : PostmanBottomNavItem(
        route = NavRoutes.POSTMAN_PROFILE,
        title = "Profile",
        iconRes = R.drawable.circle_user_24
    )
}

@Composable
fun PostmanBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        PostmanBottomNavItem.Home,
        PostmanBottomNavItem.Parcels,
        PostmanBottomNavItem.Route,
        PostmanBottomNavItem.Profile
    )

    val primaryBlue = Color(0xFF0D47A1)
    val inactiveGray = Color(0xFF757575)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            color = Color.White,
            tonalElevation = 8.dp,
            shadowElevation = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onNavigate(item.route)
                            }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(width = 56.dp, height = 36.dp)
                                .background(
                                    color = if (isSelected) primaryBlue.copy(alpha = 0.1f) else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(item.iconRes),
                                contentDescription = item.title,
                                tint = if (isSelected) primaryBlue else inactiveGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) primaryBlue else inactiveGray
                        )
                    }
                }
            }
        }
    }
}

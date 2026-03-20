package com.om.smartpost.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun UserInfoScreen(
    state: InfoUiState,
    events: Flow<InfoEvent>,
    onAction: (InfoAction) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        events.collect { event ->
            when(event) {
                InfoEvent.NavigateToAuth -> {
                    onNavigateToLogin()
                }
                is InfoEvent.ShowMessage -> { /* Handle Snackbar message */ }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Username: ${state.username}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Role: ${state.role}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(
            onClick = { onAction(InfoAction.onProtectedAccess) },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text(text = "Authorized access")
        }
        OutlinedButton(
            onClick = { onAction(InfoAction.OnLogout) },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(top = 16.dp),
            enabled = true
        ) {
            Text(text = "Logout")
        }

    }
}

@Preview
@Composable
private fun UserInfoPreview() {
    UserInfoScreen(
        state = InfoUiState(),
        onAction = {},
        events = emptyFlow(),
        onNavigateToLogin = {}

    )
}
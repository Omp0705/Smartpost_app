package com.om.smartpost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.compose.SmartPostTheme
import com.om.smartpost.auth.presentation.signin.SignInScreen
import com.om.smartpost.auth.presentation.signin.SignInUiState
import com.om.smartpost.auth.presentation.signin.dummyState
import com.om.smartpost.core.presentation.AuthState
import com.om.smartpost.core.presentation.SplashViewModel
import com.om.smartpost.ui.navigation.AppNavGraph
import com.om.smartpost.ui.navigation.NavRoutes
import org.koin.android.ext.android.inject


class MainActivity : ComponentActivity() {
    private val mainViewModel by inject<SplashViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition {
            // The screen stays visible as long as the ViewModel is in the Loading state.
            mainViewModel.authState.value is AuthState.Loading
        }
        setContent {
            val authState by mainViewModel.authState.collectAsState()
            SmartPostTheme {
                val navController = rememberNavController()
                val startDestination = when (authState) {
                    AuthState.Authenticated -> NavRoutes.MAIN // Go straight to Home
                    AuthState.Unauthenticated -> NavRoutes.AUTH // Go to Login Flow
                    AuthState.Loading -> NavRoutes.AUTH // Default/Fallback if splash failed to hold
                }
                if (authState != AuthState.Loading) {
                    AppNavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}




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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.compose.SmartPostTheme
import com.om.smartpost.auth.presentation.signin.SignInScreen
import com.om.smartpost.auth.presentation.signin.SignInUiState
import com.om.smartpost.auth.presentation.signin.dummyState
import com.om.smartpost.ui.navigation.AppNavGraph
import com.om.smartpost.ui.navigation.NavRoutes


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dummyState = SignInUiState(
            identifier = "",
            password = ""
        )
        setContent {
            SmartPostTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    startDestination = NavRoutes.AUTH
                )

            }
        }
    }
}




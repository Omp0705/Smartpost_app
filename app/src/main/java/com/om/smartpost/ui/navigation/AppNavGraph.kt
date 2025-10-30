package com.om.smartpost.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.om.smartpost.auth.presentation.forgotpass.ForgotPasswordScreen
import com.om.smartpost.auth.presentation.forgotpass.ForgotPasswordViewModel
import com.om.smartpost.auth.presentation.forgotpass.ForgotUiState
import com.om.smartpost.auth.presentation.signin.SignInScreen
import com.om.smartpost.auth.presentation.signin.SignInUiState
import com.om.smartpost.auth.presentation.signin.SignInViewModel
import com.om.smartpost.auth.presentation.signup.SignUpScreen
import com.om.smartpost.auth.presentation.signup.SignUpUiState
import com.om.smartpost.auth.presentation.signup.SignUpViewModel
import kotlinx.coroutines.flow.emptyFlow
import org.koin.androidx.compose.koinViewModel


@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navigation(
            startDestination = NavRoutes.SIGN_IN,
            route = NavRoutes.AUTH
        ){
            composable(NavRoutes.SIGN_IN) {
                val viewModel: SignInViewModel = koinViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                SignInScreen(
                    state = state,
                    events = viewModel.events,
                    onAction = viewModel::onAction,
                    onNavigateToSignUp = {
                        navController.navigate(NavRoutes.SIGN_UP)
                    },
                    onNavigateToForgotPass = {
                        navController.navigate(NavRoutes.FORGOT_PASSWORD)
                    }
                )

            }

            composable(NavRoutes.SIGN_UP) {
                val viewModel: SignUpViewModel = koinViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                SignUpScreen(
                    state = state,
                    events = viewModel.events,
                    onAction = viewModel::onAction,
                    onBackPressed = {}
                )

            }
            composable(NavRoutes.FORGOT_PASSWORD) {
                val viewModel: ForgotPasswordViewModel = koinViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                ForgotPasswordScreen(
                    state = state,
                    events = viewModel.events,
                    onAction = viewModel::onAction,
                    onBackPressed = { navController.navigate(NavRoutes.SIGN_IN)}
                )

            }

        }

        composable(NavRoutes.HOME_SCREEN) {

        }
    }

}
package com.om.smartpost.di

import com.om.smartpost.auth.presentation.forgotpass.ForgotPasswordViewModel
import com.om.smartpost.auth.presentation.signin.SignInViewModel
import com.om.smartpost.auth.presentation.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<SignInViewModel>{
        SignInViewModel()
    }
    viewModel<SignUpViewModel>{
        SignUpViewModel()
    }

    viewModel<ForgotPasswordViewModel>{
        ForgotPasswordViewModel()
    }
}
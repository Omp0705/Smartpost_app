package com.om.smartpost.di

import com.om.smartpost.auth.data.AuthRepositoryImpl
import com.om.smartpost.auth.data.RemoteAuthDataSource
import com.om.smartpost.auth.domain.AuthRepository
import com.om.smartpost.auth.presentation.forgotpass.ForgotPasswordViewModel
import com.om.smartpost.auth.presentation.signin.SignInViewModel
import com.om.smartpost.auth.presentation.signup.SignUpViewModel
import com.om.smartpost.core.data.local.TokenManager
import com.om.smartpost.core.data.networking.HttpClientFactory
import com.om.smartpost.core.presentation.SplashViewModel
import com.om.smartpost.dashboard.data.InfoRepositoryImpl
import com.om.smartpost.dashboard.data.UserInfoDataSource
import com.om.smartpost.dashboard.domain.InfoRepository
import com.om.smartpost.dashboard.presentation.UserInfoViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { TokenManager(androidContext()) }
    single { HttpClientFactory.create(CIO.create(),get()) }

    single<RemoteAuthDataSource> {
        RemoteAuthDataSource(
            get()
        )
    }

    single<UserInfoDataSource> {
        UserInfoDataSource(
            get()
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            remoteAuthDataSource = get(),
            tokenManager = get()
        )
    }
    single<InfoRepository> {
        InfoRepositoryImpl(
            infoDataSource = get()
        )
    }


    viewModel<SplashViewModel> {
        SplashViewModel(
            authRepository = get()
        )
    }

    viewModel<SignInViewModel>{
        SignInViewModel(get())
    }
    viewModel<SignUpViewModel>{
        SignUpViewModel(get())
    }
    viewModel<UserInfoViewModel>{
        UserInfoViewModel(get())
    }

    viewModel<ForgotPasswordViewModel>{
        ForgotPasswordViewModel()
    }
}
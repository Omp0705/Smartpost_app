package com.om.smartpost.di

import com.om.smartpost.auth.data.AuthRepositoryImpl
import com.om.smartpost.auth.data.RemoteAuthDataSource
import com.om.smartpost.auth.domain.AuthRepository
import com.om.smartpost.auth.presentation.forgotpass.ForgotPasswordViewModel
import com.om.smartpost.auth.presentation.signin.SignInViewModel
import com.om.smartpost.auth.presentation.signup.SignUpViewModel
import com.om.smartpost.core.data.local.TokenManager
import com.om.smartpost.core.data.networking.HttpClientFactory
import com.om.smartpost.auth.presentation.splash.SplashViewModel
import com.om.smartpost.customer.notifications.data.NotificationRepositoryImpl
import com.om.smartpost.customer.notifications.data.RemoteNotificationDataSource
import com.om.smartpost.customer.notifications.domain.repository.NotificationRepository
import com.om.smartpost.customer.notifications.presentation.NotificationsViewModel
import com.om.smartpost.dashboard.data.InfoRepositoryImpl
import com.om.smartpost.dashboard.data.UserInfoDataSource
import com.om.smartpost.dashboard.domain.InfoRepository
import com.om.smartpost.customer.parcel.domain.repository.ShipmentRepository
import com.om.smartpost.customer.parcel.presentation.ParcelViewModel
import com.om.smartpost.customer.parcel.presentation.details.ParcelDetailsViewModel
import com.om.smartpost.customer.schedule.presentation.ScheduleViewModel
import com.om.smartpost.dashboard.presentation.UserInfoViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
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
        UserInfoViewModel(
            infoRepository = get(),
            authRepository = get()
        )
    }

    viewModel<ForgotPasswordViewModel>{
        ForgotPasswordViewModel(
            authRepository = get()
        )
    }

    viewModel {
        com.om.smartpost.customer.home.presentation.dashboard.DashboardViewModel(
            shipmentRepository = get()
        )
    }
    
    single<com.om.smartpost.customer.parcel.data.repository.RemoteShipmentDataSource> {
        com.om.smartpost.customer.parcel.data.repository.RemoteShipmentDataSource(get())
    }
    
    single<ShipmentRepository> {
        com.om.smartpost.customer.parcel.data.repository.ShipmentRepositoryImpl(get())
    }
    
    viewModel<ParcelViewModel> {
        ParcelViewModel(
            shipmentRepository = get()
        )
    }

    viewModel<ScheduleViewModel> {
        ScheduleViewModel(
            shipmentRepository = get()
        )
    }
    
    viewModel<ParcelDetailsViewModel> { params ->
        ParcelDetailsViewModel(
            parcelId = params.get(),
            shipmentRepository = get()
        )
    }

    single { RemoteNotificationDataSource(get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get()) }
    viewModel { NotificationsViewModel(get()) }
}
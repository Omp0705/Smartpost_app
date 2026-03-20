package com.om.smartpost.customer.profile.di

import com.om.smartpost.auth.data.RemoteAuthDataSource
import com.om.smartpost.customer.profile.data.repository.ProfileRepositoryImpl
import com.om.smartpost.customer.profile.data.repository.RemoteProfileDataSource
import com.om.smartpost.customer.profile.domain.repository.ProfileRepository
import com.om.smartpost.customer.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    single<RemoteProfileDataSource>{
        RemoteProfileDataSource(
            httpClient = get()
        )
    }
    single<ProfileRepository> {
        ProfileRepositoryImpl(
            remoteProfileDataSource = get()
        )
    }


    viewModel {
        ProfileViewModel(
            repository = get()
        )
    }
}

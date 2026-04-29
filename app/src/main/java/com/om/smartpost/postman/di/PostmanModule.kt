package com.om.smartpost.postman.di

import com.om.smartpost.postman.data.repository.PostmanRepositoryImpl
import com.om.smartpost.postman.data.repository.RemotePostmanDataSource
import com.om.smartpost.postman.domain.repository.PostmanRepository
import com.om.smartpost.postman.presentation.PostmanViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val postmanModule = module {
    singleOf(::RemotePostmanDataSource)
    singleOf(::PostmanRepositoryImpl) bind PostmanRepository::class
    viewModelOf(::PostmanViewModel)
}

package com.om.smartpost

import android.app.Application
import androidx.compose.runtime.saveable.listSaver
import com.om.smartpost.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class SmartPostApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@SmartPostApp)
            androidLogger()

            modules(
                listOf(
                    appModule
                )
            )
        }

    }
}
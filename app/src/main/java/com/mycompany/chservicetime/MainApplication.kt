package com.mycompany.chservicetime

import android.app.Application
import com.mycompany.chservicetime.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            // androidLogger(Level.DEBUG)
            androidContext(this@MainApplication)
            modules(appModules)
        }
    }
}
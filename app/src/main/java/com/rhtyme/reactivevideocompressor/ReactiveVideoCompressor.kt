package com.rhtyme.reactivevideocompressor

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.rhtyme.reactivevideocompressor.di.appModule
import com.rhtyme.reactivevideocompressor.di.repoModule
import com.rhtyme.reactivevideocompressor.di.viewModelModule
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class ReactiveVideoCompressor: Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@ReactiveVideoCompressor)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }
}
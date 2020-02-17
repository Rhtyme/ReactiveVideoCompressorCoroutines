package com.rhtyme.coroutinevideocompressor

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.rhtyme.coroutinevideocompressor.di.appModule
import com.rhtyme.coroutinevideocompressor.di.repoModule
import com.rhtyme.coroutinevideocompressor.di.viewModelModule
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class CoroutineVideoCompressor: Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@CoroutineVideoCompressor)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }
}
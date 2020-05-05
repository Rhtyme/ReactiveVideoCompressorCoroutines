package com.rhtyme.coroutinevideocompressor.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class AppCoroutineContextProvider {

    private var Main: CoroutineContext = Dispatchers.Main
    private var IO: CoroutineContext = Dispatchers.IO

    fun ui(): CoroutineContext {
        return Main
    }

    fun io(): CoroutineContext {
        return IO
    }

    fun setupTestMode() {
        Main = Dispatchers.Unconfined
        IO = Dispatchers.Unconfined
    }

}
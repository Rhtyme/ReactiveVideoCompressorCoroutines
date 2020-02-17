package com.rhtyme.coroutinevideocompressor.viewmodel

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(val weakContext: WeakReference<Context>) : ViewModel(),
    CoroutineScope {

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    fun getSafeString(@StringRes res: Int): String {
        return weakContext.get()?.getString(res) ?: ""
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(TAG).d("onCleared called!!!")
    }

    companion object {
        const val TAG = "VIEW_MODEL_"
    }

}
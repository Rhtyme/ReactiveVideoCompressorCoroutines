package com.rhtyme.coroutinevideocompressor.viewmodel

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import timber.log.Timber
import java.lang.ref.WeakReference

abstract class BaseViewModel(val weakContext: WeakReference<Context>) : ViewModel() {

    val coroutineContextProvider = AppCoroutineContextProvider()

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
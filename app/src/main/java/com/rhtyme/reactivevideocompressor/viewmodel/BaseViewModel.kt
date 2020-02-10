package com.rhtyme.reactivevideocompressor.viewmodel

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.kwork.app.utils.rx.RxObservableSchedulers
import ru.kwork.app.utils.rx.RxSingleSchedulers
import timber.log.Timber
import java.lang.ref.WeakReference

abstract class BaseViewModel(val weakContext: WeakReference<Context>) : ViewModel() {

    private var compositeDisposable = CompositeDisposable()

    var rxSingleSchedulers: RxSingleSchedulers = RxSingleSchedulers.DEFAULT

    var rxObservableSchedulers: RxObservableSchedulers = RxObservableSchedulers.DEFAULT

    fun addDisposable(disposable: Disposable) {
        if (compositeDisposable.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable.add(disposable)
    }

    fun getSafeString(@StringRes res: Int): String {
        return weakContext.get()?.getString(res) ?: ""
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(TAG).d("onCleared called!!!")
        compositeDisposable.clear()
    }

    companion object {
        const val TAG = "VIEW_MODEL_"
    }

}
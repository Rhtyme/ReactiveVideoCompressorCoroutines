package ru.kwork.app.utils.rx

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class RxObservableSchedulers {

    abstract fun <T> applySchedulers(): ObservableTransformer<T, T>

    companion object {
        var DEFAULT: RxObservableSchedulers = object : RxObservableSchedulers() {
            override fun <T> applySchedulers(): ObservableTransformer<T, T> {
                return ObservableTransformer<T, T> { upstream ->
                    upstream
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
            }
        }

        var TEST_SCHEDULER: RxObservableSchedulers = object : RxObservableSchedulers() {
            override fun <T> applySchedulers(): ObservableTransformer<T, T> {
                return ObservableTransformer<T, T> { upstream ->
                    upstream
                            .subscribeOn(Schedulers.trampoline())
                            .observeOn(Schedulers.trampoline())
                }
            }
        }
    }
}
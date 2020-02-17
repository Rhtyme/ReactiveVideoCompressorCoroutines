package ru.kwork.app.utils.rx

import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class RxSingleSchedulers {

    abstract fun <T> applySchedulers(): SingleTransformer<T, T>

    companion object {

        var DEFAULT: RxSingleSchedulers = object : RxSingleSchedulers() {
            override fun <T> applySchedulers(): SingleTransformer<T, T> {
                return SingleTransformer<T, T> { upstream ->
                    upstream
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
            }
        }

        var TEST_SCHEDULER: RxSingleSchedulers = object : RxSingleSchedulers() {
            override fun <T> applySchedulers(): SingleTransformer<T, T> {

                return SingleTransformer<T, T> { upstream ->
                    upstream
                            .subscribeOn(Schedulers.trampoline())
                            .observeOn(Schedulers.trampoline())
                }
            }
        }

    }
}
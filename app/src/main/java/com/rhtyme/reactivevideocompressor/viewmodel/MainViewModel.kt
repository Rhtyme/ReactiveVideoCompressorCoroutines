package com.rhtyme.reactivevideocompressor.viewmodel

import android.content.Context
import com.rhtyme.reactivevideocompressor.data.repo.MainRepo
import java.lang.ref.WeakReference

class MainViewModel(context: Context, val repo: MainRepo): BaseViewModel(WeakReference(context)) {

}
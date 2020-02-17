package com.rhtyme.coroutinevideocompressor.viewmodel

import android.content.Context
import com.rhtyme.coroutinevideocompressor.data.repo.MainRepo
import java.lang.ref.WeakReference

class MainViewModel(context: Context, val repo: MainRepo): BaseViewModel(WeakReference(context)) {

}
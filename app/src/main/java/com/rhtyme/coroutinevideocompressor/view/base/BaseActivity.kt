package com.rhtyme.coroutinevideocompressor.view.base

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

abstract class BaseActivity: AppCompatActivity() {

    fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    fun setupToolbarWithBackFunction(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

}
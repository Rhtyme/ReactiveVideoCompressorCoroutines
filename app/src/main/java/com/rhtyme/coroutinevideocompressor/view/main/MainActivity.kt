package com.rhtyme.coroutinevideocompressor.view.main

import android.Manifest
import android.os.Bundle
import com.rhtyme.coroutinevideocompressor.view.base.BaseActivity
import com.rhtyme.coroutinevideocompressor.view.videolist.VideosViewPagerFragment
import com.rhtyme.coroutinevideocompressor.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest
import com.rhtyme.coroutinevideocompressor.R


class MainActivity : BaseActivity() {

    val mainViewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.tag(MAIN_TAG).d("onCreate")

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    Timber.tag(MAIN_TAG).d("onPermissionRationaleShouldBeShown")

                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    Timber.tag(MAIN_TAG).d("onPermissionsChecked")
                    initFragment()
                }
            }).check()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun initFragment() {
        Timber.tag(MAIN_TAG).d("initFragment")

        supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContent,
            VideosViewPagerFragment.getInstance()
        ).commit()
    }

    companion object {
        const val MAIN_TAG = "MAIN_TAG"
    }
}
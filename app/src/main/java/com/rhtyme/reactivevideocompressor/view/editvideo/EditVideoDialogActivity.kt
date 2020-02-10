package com.rhtyme.reactivevideocompressor.view.editvideo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.MediaController
import android.widget.Spinner
import androidx.annotation.ArrayRes
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import com.arthenica.mobileffmpeg.MediaInformation
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.rhtyme.reactivevideocompressor.R
import com.rhtyme.reactivevideocompressor.model.AlbumFile
import com.rhtyme.reactivevideocompressor.model.CompressionConfig
import com.rhtyme.reactivevideocompressor.model.Resource
import com.rhtyme.reactivevideocompressor.model.VideoResolution
import com.rhtyme.reactivevideocompressor.utils.CompressorUtils
import com.rhtyme.reactivevideocompressor.utils.TimeUtils
import com.rhtyme.reactivevideocompressor.view.base.BaseActivity
import com.rhtyme.reactivevideocompressor.viewmodel.EditVideoViewModel
import kotlinx.android.synthetic.main.circular_progress_with_percentage.*
import kotlinx.android.synthetic.main.dialog_edit_video.*
import org.koin.android.ext.android.inject
import java.io.File


class EditVideoDialogActivity : BaseActivity() {

    lateinit var originalFile: AlbumFile

    lateinit var mainMediaController: MediaController

    lateinit var compressedMediaController: MediaController


    val editVideViewModel: EditVideoViewModel by inject()

    val resolutions = arrayListOf<VideoResolution>()

    var mediaInfo: MediaInformation? = null

    var compressedFile: AlbumFile? = null

    lateinit var mainPlayer: SimpleExoPlayer
    lateinit var compressedPlayer: SimpleExoPlayer

    var mode = EDIT_MODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_video)

        mainPlayer = SimpleExoPlayer.Builder(this).build()
        compressedPlayer = SimpleExoPlayer.Builder(this).build()
        mainVideoView.player = mainPlayer
        compressedVideoView.player = compressedPlayer

        originalFile = intent.extras?.getSerializable(ALBUM_FILE_KEY) as AlbumFile

        mode = intent.extras?.getInt(MODE_KEY, EDIT_MODE) ?: EDIT_MODE

        editVideViewModel.mediaInformationLiveData.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    it.data?.let {
                        onInfoSuccess(it)
                    }
                }
                is Resource.Error -> {

                }
            }
        })

        editVideViewModel.compressInformationLiveData.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    showCompressProgressLoading(true, it.progress)
                }
                is Resource.Success -> {
                    if (it.data != null) {
                        showCompressProgressLoading(false, null)
                        onCompressedSuccess(it.data)
                    } else {
                        showCompressProgressLoading(false, null)
                    }
                }

                is Resource.Error -> {
                    showCompressProgressLoading(false, null)
                }
            }
        })

        initViews()
        editVideViewModel.fetchMediaInformation(originalFile.path ?: "")
    }


    private fun initViews() {

        tvVideoTitle.text = originalFile.fileName

        mainMediaController = MediaController(this)
        compressedMediaController = MediaController(this)
        val uri: Uri = Uri.parse(originalFile.path)
        preparePlayer(uri, mainPlayer)

        setupToolbarWithBackFunction(mToolbar)

        initSpinner(spinnerQuality, R.array.quality_options)
        initSpinner(spinnerFormat, R.array.format_options)

        processButton.setOnClickListener { prBtn ->
            startCompressing()
        }

        if (mode == VIEW_MODE) {
            processButton.isGone = true
            rowQuality.isGone = true
            rowFormat.isGone = true
            rowResolution.isGone = true
            mainShareDeleteContent.isGone = false
        }
    }

    private fun preparePlayer(mp4VideoUri: Uri, player: SimpleExoPlayer) {
        // Produces DataSource instances through which media data is loaded.
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "ReactiveVideoCompressor")
        )
// This is the MediaSource representing the media to be played.
        // This is the MediaSource representing the media to be played.
        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mp4VideoUri)
// Prepare the player with the source.
        // Prepare the player with the source.
        player.prepare(videoSource)

    }

    private fun startCompressing() {
        val path = originalFile.path ?: return
        val name = File(path).name
        val config = CompressionConfig.Builder
            .with(path)
            .name(name)
            .quality(spinnerQuality.selectedItem.toString())
            .format(spinnerFormat.selectedItem.toString())
            .resolution(spinnerResolution.selectedItem as VideoResolution)
            .duration(mediaInfo?.duration ?: 0)
            .build()
        editVideViewModel.startCompression(this, config)
    }

    fun onInfoSuccess(mediaInfo: MediaInformation) {
        this.mediaInfo = mediaInfo
        tvDurationValue.text = TimeUtils.secToTime((mediaInfo.duration / 1000).toInt())
        tvFileSize.text = CompressorUtils.humanReadableByteCountBin(originalFile.size)
        tvFileName.text = originalFile.fileName

        var width = 1920L
        var height = 1080L

        for (stream in mediaInfo.streams) {
            if (stream.type == VIDEO_STREAM) {
                width = stream.width
                height = stream.height
            }
        }
        resolutions.clear()
        resolutions.addAll(CompressorUtils.fetchVideoResolutionArray(width, height))
        initSpinnerResolution()
    }

    fun onCompressedSuccess(compressed: AlbumFile) {
        compressedFile = compressed
        compressedVideoContent.visibility = View.VISIBLE
        compressedTvDurationValue.text = TimeUtils.secToTime((compressed.duration).toInt())

        val rate = (originalFile.size * 100 / compressed.size).toInt()

        compressedTvFileSize.text = getString(
            R.string.compressed_file_size_percent,
            CompressorUtils.humanReadableByteCountBin(compressed.size), rate
        )

        val uri: Uri = Uri.parse(compressed.path)
        //Starting VideView By Setting MediaController and URI
        //Starting VideView By Setting MediaController and URI
//        videoView.setMediaController(mediaController)
        preparePlayer(uri, compressedPlayer)
    }

    fun initSpinner(spinner: Spinner, @ArrayRes list: Int) {

        ArrayAdapter.createFromResource(
            spinner.context,
            list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    fun showCompressProgressLoading(show: Boolean, progress: Double?) {
        if (!show) {
            progressCompress.visibility = View.GONE
            return
        }
        progressCompress.visibility = View.VISIBLE

        if (progress != null && progress > 0) {
            val percent = (progress * 100).toInt()
            circularProgressBar.setProgress(percent, true)
            circularTxtProgress.visibility = View.VISIBLE
            circularTxtProgress.text = getString(R.string.percentage, percent)
        } else {
            circularTxtProgress.visibility = View.GONE
        }
    }

    fun initSpinnerResolution() {
        val spinAdapter = SpinAdapter(
            this, android.R.layout.simple_spinner_item,
            resolutions
        )
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerResolution.adapter = spinAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        const val ALBUM_FILE_KEY = "ALBUM_FILE_KEY"

        const val VIDEO_STREAM = "video"
        const val AUDIO_STREAM = "audio"

        const val MODE_KEY = "mode_key" //view mode edit (0) or view mode

        const val EDIT_MODE = 0
        const val VIEW_MODE = 1

        fun getInstanceIntent(
            albumFile: AlbumFile, c: Context,
            editMode: Boolean = true
        ): Intent {
            val bundle = Bundle()
            bundle.putSerializable(ALBUM_FILE_KEY, albumFile)
            bundle.putInt(MODE_KEY, if (editMode) EDIT_MODE else VIEW_MODE)
            val intent = Intent(c, EditVideoDialogActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }
    }

}
package com.rhtyme.reactivevideocompressor.utils.widgets

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class SquareImageView : AppCompatImageView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val mConfig: Configuration

    init {
        this.mConfig = resources.configuration
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val orientation = mConfig.orientation
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                super.onMeasure(widthMeasureSpec, widthMeasureSpec)
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                super.onMeasure(heightMeasureSpec, heightMeasureSpec)
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }
}
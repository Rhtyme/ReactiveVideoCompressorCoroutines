/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.rhtyme.coroutinevideocompressor.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.text.InputType
import androidx.annotation.IdRes
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView

import timber.log.Timber

object ViewUtils {


    private var density = 1f

    fun fetchDp(value: Float, context: Context): Int {
        if (density == 1f) {
            checkDisplaySize(context)
        }
        return if (value == 0f) {
            0
        } else Math.ceil((density * value).toDouble()).toInt()
    }


    private fun checkDisplaySize(context: Context) {
        try {
            density = context.resources.displayMetrics.density
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return Math.round(dp * density)
    }

    fun dpToPxFloat(dp: Float): Float {
        val density = Resources.getSystem().displayMetrics.density
        return dp * density
    }

    fun spToPixels(spValue: Float): Float {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }


    fun pxToDp(px: Float): Float {
        val densityDpi = Resources.getSystem().displayMetrics.densityDpi.toFloat()
        return px / (densityDpi / 160f)
    }

    fun View.gone(): Boolean {
        if (this.visibility != View.GONE) {
            this.visibility = View.GONE
            return true
        }
        return false
    }

    fun View.visible(): Boolean {
        if (this.visibility != View.VISIBLE) {
            this.visibility = View.VISIBLE
            return true
        }
        return false
    }

    fun View.invisible(): Boolean {
        if (this.visibility != View.INVISIBLE) {
            this.visibility = View.INVISIBLE
            return true
        }
        return false
    }


    fun hide(vararg views: View) {
        for (v in views) {
            v.visibility = View.GONE
        }
    }

    fun show(vararg views: View) {
        for (v in views) {
            v.visibility = View.VISIBLE
        }
    }

    /**
     * sets the same padding to the view for all the sides
     *
     * @param view    view whose padding is to be set
     * @param padding padding for all the sides, in dp
     */
    fun setPadding(view: View, padding: Int) {
        val pix = ViewUtils.dpToPx(padding.toFloat())
        view.setPadding(pix, pix, pix, pix)
    }

    fun setConcealableTextViewText(text: String?, tv: TextView) {
        if (text.isNullOrEmpty()) {
            tv.visibility = View.GONE
        } else {
            tv.visibility = View.VISIBLE
            tv.text = text
        }

    }

    fun EditText.setReadOnly() {
        isFocusable = false
        isFocusableInTouchMode = false
        this.inputType = InputType.TYPE_NULL
        isEnabled = false
    }

    fun EditText.enableEditMode() {
        isFocusable = true
        isFocusableInTouchMode = true
        this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        isEnabled = true
    }


    fun <T : View> findById(parent: View, @IdRes resId: Int): T {
        return parent.findViewById<View>(resId) as T
    }

    fun <T : View> findById(parent: Activity, @IdRes resId: Int): T {
        return parent.findViewById<View>(resId) as T
    }


    fun dimAround(view: View) {
        try {
            val container = view
            val context = view.context
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val p = container.layoutParams as WindowManager.LayoutParams
            p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            p.dimAmount = 0.3f
            wm.updateViewLayout(container, p)
        } catch (e: java.lang.Exception) {
            Timber.e(e)
        }
    }

    fun getDrawableFromView(view: View): BitmapDrawable {
        val bitmap = getBitmapFromView(view)
        val bitmapDrawable = BitmapDrawable(view.resources, bitmap)

        val offsetViewBounds = Rect()
        //returns the visible bounds
        view.getDrawingRect(offsetViewBounds)
        // calculates the relative coordinates to the parent
        (view.rootView as ViewGroup).offsetDescendantRectToMyCoords(view, offsetViewBounds)

        bitmapDrawable.setBounds(offsetViewBounds.left, offsetViewBounds.top, offsetViewBounds.right, offsetViewBounds.bottom)
        return bitmapDrawable
    }

    fun getBitmapFromView(view: View): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

}// This class is not publicly instantiable

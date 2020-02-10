package com.rhtyme.reactivevideocompressor.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.graphics.drawable.TransitionDrawable
import android.view.animation.AlphaAnimation
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

object ViewAnimationUtils {

    fun scaleAnimateView(view: View) {
        val animation = ScaleAnimation(
                1.15f, 1f, 1.15f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)

        view.animation = animation
        animation.duration = 100
        animation.start()
    }


    fun scaleAnimateViewFromZero(view: View) {
        val animation = ScaleAnimation(
                1.15f, 1f, 1.15f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)

        view.animation = animation
        animation.duration = 100
        animation.start()
    }

    private fun getAlphaAnimation(from: Float, to: Float, duration: Long): Animation {
        val anim = AlphaAnimation(from, to)
        anim.interpolator = FastOutSlowInInterpolator()
        anim.duration = duration
        return anim
    }

    fun fadeIn(view: View, duration: Long) {
        animateIn(view, getAlphaAnimation(0f, 1f, duration))
    }

    fun fadeOut(view: View, duration: Long) {
        fadeOut(view, duration, View.GONE)
    }

    fun fadeOut(view: View, duration: Long, visibility: Int) {
        animateOut(view, getAlphaAnimation(1f, 0f, duration), visibility)
    }

    fun animateOut(view: View, animation: Animation, visibility: Int) {
        if (view.visibility != visibility) {
            view.clearAnimation()
            animation.reset()
            animation.startTime = 0
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationRepeat(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    view.visibility = visibility
                }
            })
            view.startAnimation(animation)
        }
    }

    fun animateIn(view: View, animation: Animation) {
        if (view.visibility == View.VISIBLE) return

        view.clearAnimation()
        animation.reset()
        animation.startTime = 0
        view.visibility = View.VISIBLE
        view.startAnimation(animation)
    }

}// This class is not publicly instantiable

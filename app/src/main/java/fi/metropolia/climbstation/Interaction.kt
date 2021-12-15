package fi.metropolia.climbstation

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation


    fun View.scaleAnimation(fromX:Float, toX:Float,fromY:Float,toY:Float, animDuration:Long, animInterpolator: AccelerateDecelerateInterpolator =AccelerateDecelerateInterpolator()){
        val animation = ScaleAnimation(fromX,toX,fromY,toY, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f).apply {
            interpolator = animInterpolator
            duration = animDuration
            fillAfter = true
        }
        startAnimation(animation)
    }

    fun View.feedBackTouchListener(){
        setOnTouchListener(object:View.OnTouchListener{
        override fun onTouch(view: View, event:MotionEvent):Boolean {
            Log.d("ontouch", "on")
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("down", "dddd")
                    scaleAnimation(1.0f, 0.95f, 1.0f, 0.95f, 100)
                    return true
                }
                MotionEvent.ACTION_UP,  ->{
                    Log.d("up", "up")
                    scaleAnimation(0.95f, 1.0f,0.95f, 1.0f, 500)
                }
            }
            view.performClick()
           return false
        }
    })
    }


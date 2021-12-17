package fi.metropolia.climbstation.ui

import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

// class for showing and hiding an element with transition
class Transition(private val targetView: View, private val parent:ViewGroup) {

    private fun setTransition() {
        val transition = Slide(Gravity.BOTTOM)
        transition.duration = 600
        transition.addTarget(targetView)
        TransitionManager.beginDelayedTransition(parent, transition)
    }

    fun showElement(){
        setTransition()
        targetView.visibility = View.VISIBLE
    }

    fun hideElement(){
        setTransition()
        targetView.visibility = View.GONE
    }
}
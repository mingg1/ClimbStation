package fi.metropolia.climbstation

import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

class Transition(private val targetView: View, private val parent:ViewGroup) {

    private fun showHideInfo() {
        val transition = Slide(Gravity.BOTTOM)
        transition.duration = 600
        transition.addTarget(targetView)
        TransitionManager.beginDelayedTransition(parent, transition)

    }

    fun showInfo(){
        showHideInfo()
        targetView.visibility = View.VISIBLE
    }

    fun hideInfo(){
        showHideInfo()
        targetView.visibility = View.GONE
    }
}
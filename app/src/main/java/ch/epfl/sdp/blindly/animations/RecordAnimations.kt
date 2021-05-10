package ch.epfl.sdp.blindly.animations

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * This class groups all animations used in the RecordingActivity. Namely, expanding and
 * collapsing items in a RecyclerView
 */
class RecordAnimations {

    companion object {
        /**
         * Expands a view, with an animation.
         *
         * @param view the view to be expanded
         */
        fun expand(view: View) {
            val animation = expandAction(view)
            view.startAnimation(animation)
        }

        /**
         * Collapses a view, with an animation.
         *
         * @param view the view to be collapsed
         */
        fun collapse(view: View) {
            val actualHeight: Int = view.measuredHeight
            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    if (interpolatedTime == 1f) {
                        view.visibility = View.GONE
                    } else {
                        view.layoutParams.height =
                            actualHeight - (actualHeight * interpolatedTime).toInt()
                        view.requestLayout()
                    }
                }
            }
            animation.duration =
                (actualHeight / view.context.resources.displayMetrics.density).toLong()
            view.startAnimation(animation)
        }

        private fun expandAction(view: View): Animation {
            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val actualheight: Int = view.measuredHeight
            view.layoutParams.height = 0
            view.visibility = View.VISIBLE
            val animation: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    view.layoutParams.height =
                        if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (actualheight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }
            animation.duration =
                (actualheight / view.context.resources.displayMetrics.density).toLong()
            view.startAnimation(animation)
            return animation
        }
    }
}
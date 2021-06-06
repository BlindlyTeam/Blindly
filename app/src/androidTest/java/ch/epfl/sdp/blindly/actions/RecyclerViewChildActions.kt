package ch.epfl.sdp.blindly.actions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher

class RecyclerViewChildActions {

    companion object {
        /**
         * Performs an action on a view with a given id inside a RecyclerView's item
         */
        fun actionOnChild(action: ViewAction, childId: Int): ViewAction {
            return object : ViewAction {
                override fun getDescription(): String {
                    return "Perform action on the view whose id is passed in"
                }

                override fun getConstraints(): Matcher<View> {
                    return allOf(isDisplayed(), isAssignableFrom(View::class.java))
                }

                override fun perform(uiController: UiController?, view: View?) {
                    view?.let {
                        val child: View = it.findViewById(childId)
                        action.perform(uiController, child)
                    }
                }
            }
        }

        /**
         * checks that the matcher childMatcher matches a view having a given id
         * inside a RecyclerView's item (given its position)
         */
        fun childOfViewAtPositionWithMatcher(
            childId: Int,
            position: Int,
            childMatcher: Matcher<View?>
        ): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText(
                        "Checks that the matcher childMatcher matches" +
                                " with a view having a given id inside a RecyclerView's item (given its position)"
                    )
                }

                override fun matchesSafely(recyclerView: RecyclerView?): Boolean {
                    val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position)
                    val matcher = hasDescendant(allOf(withId(childId), childMatcher))
                    return viewHolder != null && matcher.matches(viewHolder.itemView)
                }
            }
        }
    }
}
package ch.epfl.sdp.blindly.match

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import java.util.*
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.User

private const val VISIBLE_COUNT = 3
private const val TRANSLATION_INTERVAL = 8f
private const val SCALE_INTERVAL = 0.95f
private const val SWIPE_THRESHOLD = 0.3f
private const val MAX_DEGREE = 30f

class MatchActivity : AppCompatActivity(), CardStackListener {

    private lateinit var profiles: ArrayList<Profile>
    private val drawerLayout by lazy { findViewById<DrawerLayout>(R.id.drawer_layout) }
    private val cardStackView by lazy { findViewById<CardStackView>(R.id.card_stack_view) }
    private val manager by lazy { CardStackLayoutManager(this, this) }
    private val adapter by lazy { CardStackAdapter(profiles) }

    /**
     * Create the activity and setup the cardStack
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profiles = intent.getParcelableArrayListExtra("EXTRA_MATCH_PROFILES")!!
        setContentView(R.layout.activity_match)
        setupCardStackView()
        setupButton()
    }

    /**
     * close the drawer when the back button (toolbar) is pressed befor exiting the activity
     */
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
    }

    /**
     * here we have to save if the profile is liked or not for messaging them after
     *
     * @param direction the direction the card is swiped
     */
    override fun onCardSwiped(direction: Direction) {
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View, position: Int) {
    }

    override fun onCardDisappeared(view: View, position: Int) {
    }

    private fun setupCardStackView() {
        initialize()
    }

    /**
     * Sets the 3 buttons up (like, rewind, skip)
     */
    private fun setupButton() {

        val skip = findViewById<View>(R.id.skip_button)
        skip.setOnClickListener {
            setupButton(Direction.Left, AccelerateInterpolator(), {cardStackView.swipe()})
        }
        val rewind = findViewById<View>(R.id.rewind_button)
        rewind.setOnClickListener {
            setupButton(Direction.Bottom, DecelerateInterpolator(), {cardStackView.rewind()})
        }
        val like = findViewById<View>(R.id.like_button)
        like.setOnClickListener {
            setupButton(Direction.Right, AccelerateInterpolator(), {cardStackView.swipe()})
        }

    }

    private fun setupButton(direction: Direction, interpolation: Interpolator, func: () -> Unit) {
        val settings = SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(interpolation)
            .build()
        manager.setSwipeAnimationSetting(settings)
        func()
    }

    /**
     * Setup the behaviour of the cards in the swipeCardManager and their animations
     */
    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(VISIBLE_COUNT)
        manager.setTranslationInterval(TRANSLATION_INTERVAL)
        manager.setScaleInterval(SCALE_INTERVAL)
        manager.setSwipeThreshold(SWIPE_THRESHOLD)
        manager.setMaxDegree(MAX_DEGREE)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }
}

package ch.epfl.sdp.blindly.main_screen.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.match.MatchingAlgorithm
import ch.epfl.sdp.blindly.match.cards.CardStackAdapter
import ch.epfl.sdp.blindly.match.cards.Profile
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.firebase.storage.FirebaseStorage
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

private const val VISIBLE_COUNT = 3
private const val TRANSLATION_INTERVAL = 8f
private const val SCALE_INTERVAL = 0.95f
private const val SWIPE_THRESHOLD = 0.3f
private const val MAX_DEGREE = 30f

/**
 * Fragment to swipe potential matches
 */
@AndroidEntryPoint
class MatchPageFragment : Fragment(), CardStackListener {
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter
    private lateinit var cardStackView: CardStackView
    private lateinit var fragView: View

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var storage: FirebaseStorage

    companion object {
        private const val ARG_COUNT = "matchArgs"
        private var counter: Int? = 0

        /**
         * Create a new instance of MatchPageFragment
         *
         * @param counter the position of the fragment in the TabLayout
         * @return a MatchPageFragment
         */
        fun newInstance(counter: Int): MatchPageFragment {
            val fragment = MatchPageFragment()
            val args = Bundle()
            args.putInt(ARG_COUNT, counter)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            counter = requireArguments().getInt(ARG_COUNT)
        }
    }

    /**
     * Setup the view and retrieve the profiles to show on the match activity
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the fragment's view
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_match_page, container, false)

        setupButtons(fragView)
        setupManager()

        //Do network-fetching work in a coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            handleCoroutine()
        }
        return fragView
    }

    /**
     * Do some action when the card is dragged (still holded)
     *
     * @param direction where the card is dragged (left, right)
     * @param ratio between default (0) and swiped (1)
     */
    override fun onCardDragging(direction: Direction, ratio: Float) {
    }

    /**
     * Do some actions when the card is swiped
     *
     * @param direction the direction the card is swiped (left, right)
     */
    override fun onCardSwiped(direction: Direction) {
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    /**
     * Do some action when the card appear
     *
     * @param view in which the card is
     * @param position in the view
     */
    override fun onCardAppeared(view: View, position: Int) {
    }

    /**
     * Do some action when the card disappears
     *
     * @param view in which the card was
     * @param position in the view
     */
    override fun onCardDisappeared(view: View, position: Int) {
    }

    /**
     * Initialize the manager
     *
     */
    private fun setupManager() {
        manager = CardStackLayoutManager(context, this)
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
    }

    /**
     * Initialize the adapter
     *
     */
    private fun setupAdapterAndCardStackView(potentialMatches: List<Profile>) {
        adapter = CardStackAdapter(potentialMatches, storage)
        setupCardStackView(fragView)
    }

    /**
     * Sets up the adapter on the main scope when the coroutine
     * is done processing
     *
     */
    private suspend fun goBackOnMainThread(potentialProfiles: List<Profile>) {
        withContext(Main) {
            setupAdapterAndCardStackView(potentialProfiles)
        }
    }

    /**
     * Initialize the cardStackView
     *
     */
    private fun setupCardStackView(view: View) {
        cardStackView = view.findViewById(R.id.card_stack_view)!!
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handleCoroutine() {
        val potentialProfiles = getPotentialMatchesProfiles()

        //When the work is done in this coroutine, come back to the main scope
        goBackOnMainThread(potentialProfiles)
    }

    /**
     * This function calls the Matching Algorithm to get the potential matches and transforms them
     * into profiles by calling [createProfilesFromUsers]. Returns on the main scope when it's done.
     *
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getPotentialMatchesProfiles(): List<Profile> {
        val matchingAlgorithm = MatchingAlgorithm(userHelper, userRepository)
        val potentialUsers = matchingAlgorithm.getPotentialMatchesFromDatabase()

        return if (potentialUsers == null) {
            listOf()
        } else {
            createProfilesFromUsers(potentialUsers)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createProfilesFromUsers(users: List<User>?): List<Profile> {
        if (users == null) {
            return listOf()
        }
        val profiles = ArrayList<Profile>()
        for (user in users) {
            profiles.add(
                Profile(
                    user.username!!,
                    User.getUserAge(user)!!,
                    user.description!!,
                    user.passions!!.joinToString(", \n"),
                    user.recordingPath!!
                )
            )
        }
        return profiles
    }

    /**
     * Setup the 3 buttons (like, rewind, skip)
     *
     */
    private fun setupButtons(view: View) {
        val skip = view.findViewById<View>(R.id.skip_button)
        skip.setOnClickListener {
            listenerSettings(Direction.Left, AccelerateInterpolator(), { cardStackView.swipe() })
        }
        val rewind = view.findViewById<View>(R.id.rewind_button)
        rewind.setOnClickListener {
            listenerSettings(Direction.Bottom, DecelerateInterpolator(), { cardStackView.rewind() })
        }
        val like = view.findViewById<View>(R.id.like_button)
        like.setOnClickListener {
            listenerSettings(Direction.Right, AccelerateInterpolator(), { cardStackView.swipe() })
        }
    }

    /**
     * Setup the settings for the button's onClickListener with
     *
     * @param direction to swipe the card
     * @param interpolation
     * @param func the card action (swipe, rewind)
     */
    private fun listenerSettings(
        direction: Direction,
        interpolation: Interpolator,
        func: () -> Unit
    ) {
        val settings = SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(interpolation)
            .build()
        manager.setSwipeAnimationSetting(settings)
        func()
    }
}
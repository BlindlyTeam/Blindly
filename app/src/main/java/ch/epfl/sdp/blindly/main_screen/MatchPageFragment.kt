package ch.epfl.sdp.blindly.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.match.Profile
import ch.epfl.sdp.blindly.match.CardStackAdapter
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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

    companion object {
        private const val ARG_COUNT = "matchArgs"
        private var counter: Int? = null

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_match_page, container, false)

        setupButtons(view)
        setupCardStackView(view)

        return view
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
     * Initialize the manager, the adapter and the cardStackView
     *
     */
    private fun setupCardStackView(view: View) {
        cardStackView = view.findViewById(R.id.card_stack_view)!!
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
        adapter = CardStackAdapter(createProfiles())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    /**
     * create profiles for demo purpose
     *
     * TODO: !!! PLS REMOVE IT WHEN BINDING THE ACIVITY WITH THE DATABASE !!!
     *
     * this function have to be replaced with calls to the matching algorithms and retrieve porfiles
     * from the database
     *
     * @return a list of profiles for the matchActivity
     */
    private fun createProfiles(): List<Profile> {
        val profiles = ArrayList<Profile>()
        profiles.add(Profile("Michelle", 25))
        profiles.add(Profile("Jean", 32))
        profiles.add(Profile("Jacques", 28))
        profiles.add(Profile("Bernadette", 35))
        profiles.add(Profile("Jeannine", 46))
        profiles.add(Profile("Kilian", 25))
        profiles.add(Profile("Melissa", 20))
        profiles.add(Profile("Tibor", 36))
        profiles.add(Profile("Cagin", 27))
        profiles.add(Profile("Capucine", 21))
        return profiles
    }

    /**
     * This functions calls the Matching Algorithm to get the potential matches and transforms them
     * into profiles by calling [createProfilesFromUsers].
     *
     * TODO: Use it to retrieve potential matches
     * Since it is not used yet, I prefer to comment both functions for test coverage purposes.
     *
     * @return a list of profiles for the matchActivity
     */
    /*@RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getPotentialMatchesProfiles(): List<Profile> {
        val matchingAlgorithm = MatchingAlgorithm()
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
                Profile(user.username!!, User.getUserAge(user)!!)
            )
        }
        return profiles
    }*/
  
    /*
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
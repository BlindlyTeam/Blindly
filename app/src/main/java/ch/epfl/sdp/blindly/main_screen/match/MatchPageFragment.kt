package ch.epfl.sdp.blindly.main_screen.match

import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.audio.FirebaseRecordings
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.main_screen.match.algorithm.MatchingAlgorithm
import ch.epfl.sdp.blindly.main_screen.match.cards.CardStackAdapter
import ch.epfl.sdp.blindly.main_screen.match.cards.MediaPlayerStates
import ch.epfl.sdp.blindly.main_screen.match.cards.Profile
import ch.epfl.sdp.blindly.user.*
import ch.epfl.sdp.blindly.utils.CheckInternet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

private const val VISIBLE_COUNT = 3
private const val TRANSLATION_INTERVAL = 8f
private const val SCALE_INTERVAL = 0.95f
private const val SWIPE_THRESHOLD = 0.3f
private const val MAX_DEGREE = 30f
private const val M_TO_KM = 1000

/**
 * Fragment to swipe potential matches
 */
@AndroidEntryPoint
class MatchPageFragment : Fragment(), CardStackListener {
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter
    private lateinit var cardStackView: CardStackView
    private lateinit var fragView: View
    private lateinit var currentCardUid: String
    private lateinit var likedUserId: String
    private lateinit var currentUserId: String
    private lateinit var currentUser: User
    private var currentPosition = -1

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var recordings: FirebaseRecordings

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
        // While waiting for the profiles to load, show a message and
        // disable the play/pause button
        fragView.findViewById<TextView>(R.id.no_profile_text).text =
            getString(R.string.loading_profiles)
        fragView.findViewById<FloatingActionButton>(R.id.match_play_pause_button).isClickable =
            false

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
     * When the card is swiped right, add the uid to the liked profiles
     * and check for matches
     *
     * @param direction the direction the card is swiped (left, right)
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCardSwiped(direction: Direction) {
        if (direction == Direction.Right) {
            likedUserId = currentCardUid
            val updatedLikesList = currentUser.likes?.toMutableList()
            updatedLikesList?.add(likedUserId)
            viewLifecycleOwner.lifecycleScope.launch {
                userRepository.updateProfile(currentUserId, LIKES, updatedLikesList)
                checkMatch()
            }
        } else if (direction == Direction.Left) {
            val dislikedUserId = currentCardUid
            val updatedDislikesList = currentUser.dislikes?.toMutableList()
            updatedDislikesList?.add(dislikedUserId)
            viewLifecycleOwner.lifecycleScope.launch {
                userRepository.updateProfile(currentUserId, DISLIKES, updatedDislikesList)
            }
        }
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    /**
     * When the card appears, save the uid of the user
     * on the card along with its position
     *
     * @param view in which the card is
     * @param position in the view
     */
    override fun onCardAppeared(view: View, position: Int) {
        currentCardUid = adapter.uids[position]
        currentPosition = position
    }

    /**
     * When the card disappears, stops the mediaPlayer,
     * displays a message if the last card has been swiped
     * and launches the next mediaPlayer if there is one
     *
     * @param view in which the card was
     * @param position in the view
     */
    override fun onCardDisappeared(view: View, position: Int) {
        if (adapter.mediaPlayerStates[position] != MediaPlayerStates.STOP) {
            adapter.mediaPlayers[position].stop()
        }
        if (position == adapter.itemCount - 1) {
            fragView.findViewById<TextView>(R.id.no_profile_text).text =
                getString(R.string.no_more_swipes)
            fragView.findViewById<FloatingActionButton>(R.id.match_play_pause_button).isClickable =
                false
        } else {
            adapter.playPauseAudio(position + 1)
        }
    }

    /**
     * Initializes the manager
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
     * Initializes the adapter
     */
    private fun setupAdapterAndCardStackView(potentialMatches: List<Profile>) {
        if (CheckInternet.internetIsConnected(this.requireActivity())) {
            adapter = CardStackAdapter(potentialMatches, recordings, fragView)
            setupCardStackView(fragView)
        } else {
            fragView.findViewById<View>(R.id.skip_button).isClickable = false
            fragView.findViewById<View>(R.id.play_pause_button).isClickable = false
            fragView.findViewById<View>(R.id.like_button).isClickable = false
        }
    }

    /**
     * Sets up the adapter on the main scope when the coroutine
     * is done processing
     */
    private suspend fun goBackOnMainThread(potentialProfiles: List<Profile>) {
        withContext(Main) {
            setupAdapterAndCardStackView(potentialProfiles)
        }
    }

    /**
     * Initialize the cardStackView
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
        //Set the message text if no profiles are available
        if (adapter.itemCount == 0) {
            fragView.findViewById<TextView>(R.id.no_profile_text).text =
                getString(R.string.no_available_swipe)
        } else {
            fragView.findViewById<TextView>(R.id.no_profile_text).text = ""
        }
    }

    private suspend fun handleCoroutine() {
        val potentialProfiles = getPotentialMatchesProfiles()

        //When the work is done in this coroutine, come back to the main scope
        goBackOnMainThread(potentialProfiles)
    }

    /**
     * This function calls the Matching Algorithm to get the potential matches and transforms them
     * into profiles by calling [createProfilesFromUsers]. Returns on the main scope when it's done
     */
    private suspend fun getPotentialMatchesProfiles(): List<Profile> {
        val matchingAlgorithm = MatchingAlgorithm(userHelper, userRepository)
        val potentialUsers = matchingAlgorithm.getPotentialMatchesFromDatabase()

        return if (potentialUsers == null) {
            listOf()
        } else {
            createProfilesFromUsers(potentialUsers)
        }
    }

    private suspend fun createProfilesFromUsers(users: List<User>?): List<Profile> {
        if (users == null) {
            return listOf()
        }
        currentUserId = userHelper.getUserId()!!
        currentUser = userRepository.getUser(currentUserId)!!
        val profiles = ArrayList<Profile>()
        for (user in users) {
            profiles.add(
                Profile(
                    user.uid!!,
                    user.username!!,
                    User.getUserAge(user)!!,
                    user.gender!!,
                    computeDistance(currentUser.location!!, user.location!!),
                    user.recordingPath!!
                )
            )
        }
        return profiles
    }

    /**
     * Setup the 3 buttons (like, rewind, skip)
     */
    private fun setupButtons(view: View) {
        val skip = view.findViewById<View>(R.id.skip_button)
        skip.setOnClickListener {
            listenerSettings(Direction.Left, AccelerateInterpolator(), { cardStackView.swipe() })
        }
        val playPause = view.findViewById<View>(R.id.match_play_pause_button)
        playPause.setOnClickListener {
            adapter.playPauseAudio(currentPosition)
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

    /**
     * Compute the distance between the currentUser's location
     * and the location of the user on the card
     *
     * @param thisLocation the location of the currentUser
     * @param otherLocation the location of the user on the card
     * @return the computed distance
     */
    private fun computeDistance(thisLocation: List<Double>, otherLocation: List<Double>): Int {
        val thisLoc = Location("")
        thisLoc.latitude = thisLocation[0]
        thisLoc.longitude = thisLocation[1]

        val otherLoc = Location("")
        otherLoc.latitude = otherLocation[0]
        otherLoc.longitude = otherLocation[1]

        return thisLoc.distanceTo(otherLoc).roundToInt() / M_TO_KM
    }

    /**
     * When a user likes someone, check if the other liked them
     * too and match them both
     *
     */
    private suspend fun checkMatch() {
        val otherUser = userRepository.getUser(likedUserId)
        
        if (otherUser?.likes?.contains(currentUserId)!!) {
            val otherUserUpdatedMatchList = otherUser.matches?.toMutableList()
            otherUserUpdatedMatchList?.add(currentUserId)
            userRepository.updateProfile(
                likedUserId,
                MATCHES,
                otherUserUpdatedMatchList
            )

            val currentUserUpdatedMatchList = currentUser.matches?.toMutableList()
            currentUserUpdatedMatchList?.add(likedUserId)
            userRepository.updateProfile(
                currentUserId,
                MATCHES,
                currentUserUpdatedMatchList
            )
        }
    }
}
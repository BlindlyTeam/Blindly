package ch.epfl.sdp.blindly.main_screen.my_matches

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.weather.WeatherActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MyMatchesFragment : Fragment(), MyMatchesAdapter.OnItemClickListener {

    private lateinit var myMatchesRecyclerView: RecyclerView
    private lateinit var adapter: MyMatchesAdapter
    private lateinit var fragView: View
    private lateinit var userId: String

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository

    companion object {
        private const val ARG_COUNT = "myMatchesArgs"
        private var counter: Int? = null

        /**
         * Create a new instance of MyMatchesFragment
         *
         * @param counter the position of the fragment in the TabLayout
         * @return a MyMatchesFragment
         */
        fun newInstance(counter: Int): MyMatchesFragment {
            val fragment = MyMatchesFragment()
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
        fragView = inflater.inflate(R.layout.activity_my_matches, container, false)

        var myMatchesUids: List<String>?
        var myMatches: ArrayList<MyMatch>

        //Needs to be done in a coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            userId = userHelper.getUserId()!!
            myMatchesUids = userRepository.getMyMatchesUids(userId)
            if (myMatchesUids != null) {
                myMatches = arrayListOf()
                for (userId in myMatchesUids!!) {
                    myMatches.add(
                        MyMatch(
                            userRepository.getUser(userId)?.username!!,
                            userId,
                            false
                        )
                    )
                }
                setAdapterOnMainThread(myMatches)
            }
        }

        val weatherActivityButton =
            fragView.findViewById<FloatingActionButton>(R.id.buttonWeatherEvent)
        weatherActivityButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                userId = userHelper.getUserId()!!
                startWeather()
            }
        }

        return fragView
    }

    /**
     * Sets the adapter on main thread
     *
     * @param myMatches List of user's matches
     */
    private suspend fun setAdapterOnMainThread(myMatches: ArrayList<MyMatch>) {
        withContext(Dispatchers.Main) {
            setupRecylerView(myMatches)
        }
    }

    /**
     * Sets up the adapter for recycler view
     *
     * @param myMatches List of user's matches
     */
    private fun setupRecylerView(myMatches: ArrayList<MyMatch>) {
        myMatchesRecyclerView = fragView.findViewById(R.id.my_matches_recyler_view)
        myMatchesRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MyMatchesAdapter(myMatches, arrayListOf(), requireContext(), this)
        myMatchesRecyclerView.adapter = adapter
    }

    /**
     * Handles clicks, used by collapsing and expanding layouts
     *
     * @param position Position of the item clicked
     */
    override fun onItemClick(position: Int) {
        adapter.notifyItemChanged(position)
    }

    // gets the location of user and starts WeatherActivity
    private suspend fun startWeather() {
        val intent = Intent(activity, WeatherActivity::class.java)
        val location = userRepository.getLocation(userId)
        intent.putExtra(WeatherActivity.LOCATION, location)
        startActivity(intent)
    }
}


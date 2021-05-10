package ch.epfl.sdp.blindly.main_screen.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.match.my_matches.MyMatch
import ch.epfl.sdp.blindly.match.my_matches.MyMatchesAdapter
import ch.epfl.sdp.blindly.user.User.Companion.toUser
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "MyMatches"


@AndroidEntryPoint
class MyMatchesFragment : Fragment(), MyMatchesAdapter.OnItemClickListener {

    private lateinit var myMatchesRecyclerView: RecyclerView
    private lateinit var adapter: MyMatchesAdapter
    private lateinit var fragView: View


    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository


    companion object {
        private const val ARG_COUNT = "mapArgs"
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
            MyMatchesFragment.counter = requireArguments().getInt(MyMatchesFragment.ARG_COUNT)
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
        fragView = inflater.inflate(R.layout.activity_my_matches, container, false)
        var myMatches = ArrayList<MyMatch>()
        myMatches = arrayListOf()
        //Needs to be done in a coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            myMatches = getMyMatches()!!
        }
        val adapter =
            context?.let {
                MyMatchesAdapter(
                    myMatches,
                    ArrayList(),
                    requireContext(),
                    this
                )
            }!!
        setupRecylerView(fragView )
        return fragView
    }



    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun getMyMatches(): ArrayList<MyMatch>? {
        val userId = userHelper.getUserId()!!
        val currentUser = userRepository.getUser(userId)
        var myMatches: ArrayList<MyMatch>? = arrayListOf()


        val query: Query? = currentUser!!.matches?.let {
            userRepository.getCollectionReference()
                .whereArrayContains("matches", it)
        }
        try {
            val userIds = query?.get()?.await()
            if (userIds != null) {
                for (userId in userIds) {
                    myMatches!!.add(MyMatch(userId.toUser()!!.username!!, userId.toString(), false))
//                    userRepository.getUser(userId.toString())?.username?.let {
//                        MyMatch(
//                            it,
//                            userId.toString(), false
//                        )
//                    }?.let { myMatches!!.add(it) }
                }
            }
        } catch (exception: Exception) {
            Log.w(TAG, "Error getting users : ", exception)
        }

        return myMatches
    }

    private fun setupRecylerView(view: View ){
        myMatchesRecyclerView = view.findViewById(R.id.my_matches_recyler_view)
        myMatchesRecyclerView.layoutManager = LinearLayoutManager(context)
        var myMatches = ArrayList<MyMatch>()
        myMatches = arrayListOf()

        myMatches.add( MyMatch("abc", "asdasd", false))
        myMatches.add(MyMatch("asdadasd","Asdsad", true))
        adapter = MyMatchesAdapter(myMatches, arrayListOf(), requireContext(), this)
        myMatchesRecyclerView.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        adapter.notifyItemChanged(position)
    }
}


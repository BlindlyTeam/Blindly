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
import ch.epfl.sdp.blindly.main_screen.match.my_matches.MyMatchesAdapter
import ch.epfl.sdp.blindly.user.UserHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        fragView = inflater.inflate(R.layout.activity_my_matches, container, false)

        //Needs to be done in a coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            getMyMatches()
        }

        return fragView
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun getMyMatches() {
        val userId = userHelper.getUserId()!!
        var myMatches: ArrayList<MyMatch>?
        var myMatchesUids: List<String>

        val docRef = userRepository.getCollectionReference().document(userId)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                myMatchesUids = snapshot["matches"] as List<String>
                viewLifecycleOwner.lifecycleScope.launch {
                    myMatches = arrayListOf()
                    for (userId in myMatchesUids) {
                        myMatches!!.add(
                            MyMatch(
                                userRepository.getUser(userId)?.username!!,
                                userId,
                                false
                            )
                        )
                    }
                    myMatches?.let { setAdapterOnMainThread(it) }
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

    }


    private suspend fun setAdapterOnMainThread(input: ArrayList<MyMatch>) {
        withContext(Dispatchers.Main) {
            setupRecylerView(fragView, input)
        }
    }

    private fun setupRecylerView(view: View, input: ArrayList<MyMatch>) {
        myMatchesRecyclerView = view.findViewById(R.id.my_matches_recyler_view)
        myMatchesRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MyMatchesAdapter(input, arrayListOf(), requireContext(), this)
        myMatchesRecyclerView.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        adapter.notifyItemChanged(position)
    }
}


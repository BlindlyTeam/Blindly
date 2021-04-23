package ch.epfl.sdp.blindly.main_screen

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.match.MatchActivity
import ch.epfl.sdp.blindly.match.Profile
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

/**
 * Fragment to access the match page
 */
@AndroidEntryPoint
class MatchPageFragment : Fragment() {

    companion object {
        private const val ARG_COUNT = "matchArgs"
        private var counter: Int? = null

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

        val intent = Intent(context, MatchActivity::class.java)
        val profiles = createProfiles()
        intent.putParcelableArrayListExtra(
            "EXTRA_MATCH_PROFILES",
            profiles as ArrayList<out Parcelable>
        )
        val matchActivityButton = view.findViewById<Button>(R.id.button_to_match)
        matchActivityButton.setOnClickListener { startActivity(intent) }

        return view
    }

    /**
     * create profiles for demo purpose
     *
     * !!! PLS REMOVE IT WHEN BINDING THE ACIVITY WITH THE DATABASE !!!
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

}
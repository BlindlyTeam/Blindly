package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindly.match.MatchActivity
import ch.epfl.sdp.blindly.match.Profile
import java.util.ArrayList

class MatchPage : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_match_page, container, false)

        val intent = Intent(context, MatchActivity::class.java)
        val profiles = createProfiles()
        intent.putParcelableArrayListExtra("EXTRA_MATCH_PROFILES", profiles as ArrayList<out Parcelable>)
        val match_activity_button = view.findViewById<Button>(R.id.button_to_match)
        match_activity_button.setOnClickListener{startActivity(intent)}

        return view
    }

    /**
     * create profiles for demo purpose
     * this function have to be replaced with calls to the matching algorithms and retrieve porfiles
     * from the database
     *
     * @return
     */
    private fun createProfiles(): List<Profile> {
        val profiles = ArrayList<Profile>()
        profiles.add(Profile(name = "Michelle", age = 25))
        profiles.add(Profile(name = "Jean", age = 32))
        profiles.add(Profile(name = "Jacques", age = 28))
        profiles.add(Profile(name = "Bernadette", age = 35))
        profiles.add(Profile(name = "Jeannine", age = 46))
        profiles.add(Profile(name = "Kilian", age = 18))
        profiles.add(Profile(name = "Melissa", age = 20))
        profiles.add(Profile(name = "Tibor", age = 36))
        profiles.add(Profile(name = "Cagin", age = 27))
        profiles.add(Profile(name = "Capucine", age = 21))
        return profiles
    }
}
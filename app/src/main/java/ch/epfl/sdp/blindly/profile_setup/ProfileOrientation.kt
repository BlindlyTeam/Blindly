package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.User
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private const val SELECTION_LIMIT = 3

/**
 * Activity that asks for the sexual orientation of the user
 */
class ProfileOrientation : AppCompatActivity() {

    private var sexualOrientations: ArrayList<String> = ArrayList()
    private lateinit var userBuilder: User.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_orientation)

        val bundle = intent.extras
        userBuilder = bundle?.getString(EXTRA_USER)?.let { Json.decodeFromString(it) }!!
    }

    /**
     * Checks the number of orientation user chooses, if it's not within limits appropriate
     * error messages are shown. Otherwise choices are passed to helper function for builder
     *
     * @param view the current view
     */
    fun startProfileShowMe(view: View) {
        findViewById<TextView>(R.id.at_least_1_warning).visibility = INVISIBLE
        findViewById<TextView>(R.id.no_more_than_3_warning).visibility = INVISIBLE

        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup_p5)
        val ids = chipGroup.checkedChipIds
        val size = ids.size

        when {
            //none selected
            size < 1 -> {
                findViewById<TextView>(R.id.at_least_1_warning).visibility = VISIBLE
            }
            size > SELECTION_LIMIT -> {
                findViewById<TextView>(R.id.no_more_than_3_warning).visibility = VISIBLE
            }
            //correct numbers of selection
            else -> {
                bundleExtrasAndStartProfileShowMe(chipGroup)
            }
        }
    }

    //helper function to get the choices to builder
    private fun bundleExtrasAndStartProfileShowMe(chipGroup: ChipGroup) {
        sexualOrientations = getChipTextsFromIds(chipGroup)
        val intent = Intent(this, ProfileShowMe::class.java)
        val bundle = Bundle()
        userBuilder.setSexualOrientations(sexualOrientations)
        bundle.putSerializable(
            EXTRA_USER,
            Json.encodeToString(User.Builder.serializer(), userBuilder)
        )
        intent.putExtras(bundle)

        startActivity(intent)
    }

    companion object {
        /**
         * Given a chipGroup finds the corresponding text of each chip that is checked
         *
         * @param chipGroup
         * @return an ArrayList<String> containing the text of each chip that is checked
         */
        fun getChipTextsFromIds(chipGroup: ChipGroup): ArrayList<String> {
            val texts = arrayListOf<String>()
            val ids = chipGroup.checkedChipIds
            ids.forEach {
                val chipText = chipGroup.findViewById<Chip>(it).text.toString()
                texts.add(chipText)
            }
            return texts
        }
    }
}
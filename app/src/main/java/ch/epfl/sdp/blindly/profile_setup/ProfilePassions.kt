package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.User
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private const val SELECTION_LIMIT = 5

/**
 * Activity that asks for the passions of the user. The user has to select passions from
 * a list of chips.
 */
class ProfilePassions : AppCompatActivity() {
    private lateinit var userBuilder: User.Builder
    private var passions: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_passions)

        val bundle = intent.extras
        userBuilder = bundle?.getString(EXTRA_USER)?.let { Json.decodeFromString(it) }!!
    }

    /**
     * Checks the number of selected chip buttons by the user and passes them to the builder via
     * helper function getCheckedChip and starts ProfileAudioRecording if the number of selected
     * chips is within limits, if not outputs error.
     *
     * @param view the current view
     */
    fun startProfileAudioRecording(view: View) {
        findViewById<TextView>(R.id.warning_p7_1).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning_p7_2).visibility = View.INVISIBLE

        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup_p7)
        val ids = chipGroup.checkedChipIds
        val size = ids.size

        when {
            //none selected
            size < 1 -> {
                findViewById<TextView>(R.id.warning_p7_1).visibility = View.VISIBLE
            }
            size > SELECTION_LIMIT -> {
                findViewById<TextView>(R.id.warning_p7_2).visibility = View.VISIBLE
            }
            //correct numbers of selection
            else -> {
                passions = ProfileOrientation.getChipTextsFromIds(chipGroup)
                val bundle = Bundle()
                userBuilder.setPassions(passions)
                bundle.putSerializable(
                    EXTRA_USER,
                    Json.encodeToString(User.Builder.serializer(), userBuilder)
                )
                val intent = Intent(this, ProfileAudioRecording::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }
}

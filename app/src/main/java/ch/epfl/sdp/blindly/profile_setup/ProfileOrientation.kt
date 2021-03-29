package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

const val EXTRA_SEXUAL_ORIENTATIONS = "sexual_orientation"

class ProfileOrientation : AppCompatActivity() {

    private val SELECTION_LIMIT = 3
    private var username: String? = null
    private var birthday: String? = null
    private var genre: String? = null
    private val sexualOriantations : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_orientation)

        val bundle = intent.extras
        username = bundle?.getString(EXTRA_USERNAME)
        birthday = bundle?.getString(EXTRA_BIRTHDAY)
        genre = bundle?.getString(EXTRA_GENRE)
    }

    fun startProfileShowMe(view: View) {
        findViewById<TextView>(R.id.warning_p5_1).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning_p5_2).visibility = View.INVISIBLE

        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup_p5)
        val ids = chipGroup.checkedChipIds
        val size = ids.size

        when {
            //none selected
            size < 1 -> {
                findViewById<TextView>(R.id.warning_p5_1).visibility = View.VISIBLE
            }
            size > SELECTION_LIMIT -> {
                findViewById<TextView>(R.id.warning_p5_2).visibility = View.VISIBLE
            }
            //correct numbers of selection
            else -> {
                ids.forEach { i ->
                    val chipText =  chipGroup.findViewById<Chip>(i).text.toString()
                    sexualOriantations.add(chipText)
                }
                val intent = Intent(this, ProfileShowMe::class.java)
                val extras = Bundle()
                extras.putString(EXTRA_USERNAME, username)
                extras.putString(EXTRA_BIRTHDAY, birthday)
                extras.putString(EXTRA_GENRE, genre)
                extras.putStringArrayList(EXTRA_SEXUAL_ORIENTATIONS, sexualOriantations)
                intent.putExtras(extras)
                startActivity(intent)
            }
        }

    }
}
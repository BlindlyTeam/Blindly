package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.utils.UserHelper
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import javax.inject.Inject

const val EXTRA_PASSIONS = "passions"

class ProfilePassions : AppCompatActivity() {
    @Inject
    lateinit var user: UserHelper

    private var username: String? = null
    private var birthday: String? = null
    private var genre: String? = null
    private var sexualOriantations : ArrayList<String> = ArrayList()
    private var showMe: String? = null
    private val passions: ArrayList<String> = ArrayList()

    private val SELECTION_LIMIT = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_passions)

        val bundle = intent.extras
        username = bundle?.getString(EXTRA_USERNAME)
        birthday = bundle?.getString(EXTRA_BIRTHDAY)
        genre = bundle?.getString(EXTRA_GENRE)
        if(bundle != null)
            sexualOriantations = bundle.getStringArrayList(EXTRA_SEXUAL_ORIENTATIONS) as ArrayList<String>
        showMe = bundle?.getString(EXTRA_SHOW_ME)
    }

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
                getCheckedChip()
                val intent = Intent(this, ProfileAudioRecording::class.java)
                setUser()
                startActivity(intent)
            }
        }
    }

    private fun getCheckedChip() {
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup_p7)
        val ids = chipGroup.checkedChipIds
        ids.forEach { id ->
            val chipText = findViewById<Chip>(id).text.toString()
            passions.add(chipText)
        }
    }

    private fun setUser() {
        val location = AndroidLocationService(this).getCurrentLocation().toString()
        username?.let { birthday?.let { it1 ->
            genre?.let { it2 ->
                showMe?.let { it3 ->
                    user.setUserProfile(it, location,
                        it1, it2, sexualOriantations, it3, passions)
                }
            }
        } }
    }
}
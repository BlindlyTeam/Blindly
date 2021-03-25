package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

const val EXTRA_PASSIONS = "passions"
class Profile7 : AppCompatActivity() {

    private var username: String? = null
    private var age: String? = null
    private var genre: String? = null
    private var sexualOriantations : ArrayList<String> = ArrayList()
    private var showMe: String? = null
    private val passions: ArrayList<String> = ArrayList()

    private val SELECTION_LIMIT = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_7)

        val bundle = intent.extras
        username = bundle?.getString(EXTRA_USERNAME)
        age = bundle?.getString(EXTRA_BIRTHDAY)
        genre = bundle?.getString(EXTRA_GENRE)
        if(bundle != null)
            sexualOriantations = bundle.getStringArrayList(EXTRA_SEXUAL_ORIENTATIONS) as ArrayList<String>
        showMe = bundle?.getString(EXTRA_SHOW_ME)
    }

    fun startProfile8(view: View) {
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
                val intent = Intent(this, Profile8::class.java)
                //TODO:Call setUserProfile, but need localissation.
            }
        }
    }

    private fun getCheckedChip() {
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup_p5)
        val ids = chipGroup.checkedChipIds
        ids.forEach { id ->
            val chipText = findViewById<Chip>(id).text.toString()
            passions.add(chipText)
        }
    }
}
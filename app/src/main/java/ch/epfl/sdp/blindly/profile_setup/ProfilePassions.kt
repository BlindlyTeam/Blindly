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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class ProfilePassions : AppCompatActivity() {
    lateinit var userBuilder: User.Builder
    private val passions: ArrayList<String> = ArrayList()

    private val SELECTION_LIMIT = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_passions)

        val bundle = intent.extras
        userBuilder = bundle?.getString(EXTRA_USER)?.let { Json.decodeFromString(it) }!!
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

    private fun getCheckedChip() {
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup_p7)
        val ids = chipGroup.checkedChipIds
        ids.forEach { id ->
            val chipText = findViewById<Chip>(id).text.toString()
            passions.add(chipText)
        }
    }

}
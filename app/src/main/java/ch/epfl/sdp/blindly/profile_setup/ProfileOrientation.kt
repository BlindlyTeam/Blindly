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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val SELECTION_LIMIT = 3
class ProfileOrientation : AppCompatActivity() {

    private val sexualOriantations : ArrayList<String> = ArrayList()
    private lateinit var userBuilder : User.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_orientation)

        val bundle = intent.extras
        userBuilder = bundle?.getString(EXTRA_USER)?.let { Json.decodeFromString(it) }!!
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
                bundleExtrasAndStartProfileShowMe(ids, chipGroup)
            }
        }

    }

    private fun bundleExtrasAndStartProfileShowMe(ids: MutableList<Int>, chipGroup: ChipGroup) {
        ids.forEach { i ->
            val chipText =  chipGroup.findViewById<Chip>(i).text.toString()
            sexualOriantations.add(chipText)
        }
        val intent = Intent(this, ProfileShowMe::class.java)
        val bundle = Bundle()
        userBuilder.setSexualOrientations(sexualOriantations)
        bundle.putSerializable(EXTRA_USER, Json.encodeToString(User.Builder.serializer(),userBuilder))
        intent.putExtras(bundle)

        startActivity(intent)
    }
}
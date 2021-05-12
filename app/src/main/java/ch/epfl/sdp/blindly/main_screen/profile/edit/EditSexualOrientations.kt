package ch.epfl.sdp.blindly.main_screen.profile.edit

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.SEXUAL_ORIENTATIONS
import ch.epfl.sdp.blindly.user.enums.SexualOrientations
import com.google.android.material.chip.ChipGroup

private const val SELECTION_LIMIT = 3

class EditSexualOrientations : AppCompatActivity() {

    private lateinit var chipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_sexual_orientations)
        supportActionBar?.hide()

        chipGroup = findViewById(R.id.sexual_orientations_chip_group)
        val sexualOrientations = intent.getStringArrayListExtra(SEXUAL_ORIENTATIONS)
        if (sexualOrientations != null)
            setCheckedChip(sexualOrientations)
    }

    override fun onBackPressed() {
        if (sexualOrientationsAreCorrect()) {
            super.onBackPressed()
        }
    }

    private fun sexualOrientationsAreCorrect(): Boolean {
        findViewById<TextView>(R.id.at_least_1_warning).visibility = INVISIBLE
        findViewById<TextView>(R.id.no_more_than_3_warning).visibility = INVISIBLE

        val ids = chipGroup.checkedChipIds
        val size = ids.size

        return when {
            //none selected
            size < 1 -> {
                findViewById<TextView>(R.id.at_least_1_warning).visibility = VISIBLE
                false
            }
            size > SELECTION_LIMIT -> {
                findViewById<TextView>(R.id.no_more_than_3_warning).visibility = VISIBLE
                false
            }
            //correct numbers of selection
            else -> {
                true
            }
        }
    }

    private fun setCheckedChip(sexualOrientations: ArrayList<String>) {
        sexualOrientations.forEach { p ->
            SexualOrientations.values().forEach { v ->
                if(v.asString == p)
                    chipGroup.check(v.id)
            }
        }
    }
}
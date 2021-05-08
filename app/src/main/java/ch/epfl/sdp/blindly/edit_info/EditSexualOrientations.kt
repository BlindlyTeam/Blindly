package ch.epfl.sdp.blindly.edit_info

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.SEXUAL_ORIENTATIONS
import com.google.android.material.chip.ChipGroup

//Not private as they will be used in the EditProfile activity too
const val STRAIGHT = "Straight"
const val LESBIAN = "Lesbian"
const val GAY = "Gay"
const val BISEXUAL = "Bisexual"
const val ASEXUAL = "Asexual"
const val DEMISEXUAL = "Demisexual"
const val PANSEXUAL = "Pansexual"
const val QUEER = "Queer"
const val QUESTIONING = "Questioning"
private const val SELECTION_LIMIT = 3

class EditSexualOrientations : AppCompatActivity() {

    private lateinit var chipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_sexual_orientations)
        supportActionBar?.hide()

        chipGroup = findViewById(R.id.sexual_orientations_chip_group)
        val sexualOrientations = intent.getStringArrayListExtra(SEXUAL_ORIENTATIONS)
        if(sexualOrientations != null)
            setCheckedChip(sexualOrientations)
    }

    override fun onBackPressed() {
        if (sexualOrientationsAreCorrect()) {
            super.onBackPressed()
        }
    }

    private fun sexualOrientationsAreCorrect(): Boolean {
        findViewById<TextView>(R.id.warning_p5_1).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning_p5_2).visibility = View.INVISIBLE

        val ids = chipGroup.checkedChipIds
        val size = ids.size

        return when {
            //none selected
            size < 1 -> {
                findViewById<TextView>(R.id.warning_p5_1).visibility = View.VISIBLE
                false
            }
            size > SELECTION_LIMIT -> {
                findViewById<TextView>(R.id.warning_p5_2).visibility = View.VISIBLE
                false
            }
            //correct numbers of selection
            else -> {
                true
            }
        }
    }

    private fun setCheckedChip(sexualOrientations: ArrayList<String>) {
        sexualOrientations.forEach {
            when(it) {
                STRAIGHT -> chipGroup.check(R.id.chip1)
                LESBIAN -> chipGroup.check(R.id.chip2)
                GAY -> chipGroup.check(R.id.chip3)
                BISEXUAL -> chipGroup.check(R.id.chip4)
                ASEXUAL -> chipGroup.check(R.id.chip5)
                DEMISEXUAL -> chipGroup.check(R.id.chip6)
                PANSEXUAL -> chipGroup.check(R.id.chip7)
                QUEER -> chipGroup.check(R.id.chip8)
                QUESTIONING -> chipGroup.check(R.id.chip9)
            }
        }
    }
}
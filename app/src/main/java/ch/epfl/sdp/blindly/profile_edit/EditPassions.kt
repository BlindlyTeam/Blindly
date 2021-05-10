package ch.epfl.sdp.blindly.profile_edit

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.PASSIONS
import ch.epfl.sdp.blindly.user.enums.Passions
import com.google.android.material.chip.ChipGroup

private const val SELECTION_LIMIT = 5

class EditPassions : AppCompatActivity() {

    private lateinit var chipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_passions)
        supportActionBar?.hide()

        val passions = intent.getStringArrayListExtra(PASSIONS)
        chipGroup = findViewById(R.id.passions_chip_group)
        if (passions != null)
            setCheckedChips(passions)
    }

    override fun onBackPressed() {
        if (passionsAreCorrect()) {
            super.onBackPressed()
        }
    }

    private fun passionsAreCorrect(): Boolean {
        findViewById<TextView>(R.id.at_least_one_warning).visibility = INVISIBLE
        findViewById<TextView>(R.id.no_more_than_5_warning).visibility = INVISIBLE

        val ids = chipGroup.checkedChipIds
        val size = ids.size

        return when {
            //none selected
            size < 1 -> {
                findViewById<TextView>(R.id.at_least_one_warning).visibility = VISIBLE
                false
            }
            size > SELECTION_LIMIT -> {
                findViewById<TextView>(R.id.no_more_than_5_warning).visibility = VISIBLE
                false
            }
            //correct numbers of selection
            else -> {
                true
            }
        }
    }

    /**
     * Set the chipGroup to reflect the given passions
     *
     * @param passions
     */
    private fun setCheckedChips(passions: ArrayList<String>) {
        passions.forEach { p ->
            Passions.values().forEach { v ->
                if (v.asString == p) {
                    chipGroup.check(v.id)
                }
            }
        }
    }
}
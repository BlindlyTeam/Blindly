package ch.epfl.sdp.blindly.profile_edit

import android.os.Build
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile_setup.ProfileOrientation
import ch.epfl.sdp.blindly.user.PASSIONS
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.enums.Passions
import ch.epfl.sdp.blindly.viewmodel.UserViewModel
import ch.epfl.sdp.blindly.viewmodel.ViewModelAssistedFactory
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val SELECTION_LIMIT = 5

@AndroidEntryPoint
class EditPassions : AppCompatActivity() {

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    private lateinit var viewModel: UserViewModel

    private lateinit var chipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_passions)
        supportActionBar?.hide()
        val uid = userHelper.getUserId()
        viewModel = UserViewModel.instantiateViewModel(
            uid,
            assistedFactory,
            this,
            this
        )

        val passions = intent.getStringArrayListExtra(PASSIONS)
        chipGroup = findViewById(R.id.passions_chip_group)
        if (passions != null)
            setCheckedChips(chipGroup, passions)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBackPressed() {
        if (passionsAreCorrect()) {
            viewModel.updateField(PASSIONS, ProfileOrientation.getChipTextsFromIds(chipGroup))
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
     * Check the chip from the chipGroup given an ArrayList of Strings
     *
     * @param chipGroup the chipGroup in which the chip can be checked
     * @param passions the ArrayList<String> containing the text of the chip to check
     */
    private fun setCheckedChips(chipGroup: ChipGroup, passions: ArrayList<String>) {
        passions.forEach { p ->
            Passions.values().forEach { v ->
                if (v.asString == p) {
                    chipGroup.check(v.id)
                }
            }
        }
    }
}
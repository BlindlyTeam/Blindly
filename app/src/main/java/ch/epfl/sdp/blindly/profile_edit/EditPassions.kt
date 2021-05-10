package ch.epfl.sdp.blindly.profile_edit

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.PASSIONS
import com.google.android.material.chip.ChipGroup

private const val BRUNCH = "Brunch"
private const val WINE = "Wine"
private const val FASHION = "Fashion"
private const val CYCLING = "Cycling"
private const val RUNNING = "Running"
private const val TEA = "Tea"
private const val COFFEE = "Coffee"
private const val COMEDY = "Comedy"
private const val WALKING = "Walking"
private const val BOARD_GAMES = "Borad Games"
private const val YOGA = "Yoga"
private const val KARAOKE = "Karaoke"
private const val DOG_LOVER = "Dog Lover"
private const val GAMER = "Gamer"
private const val ART = "Art"
private const val COCKTAILS = "Cocktails"
private const val DANCING = "Dancing"
private const val PHOTOGRAPHY = "Photography"
private const val WRITER = "Writer"
private const val FOODIE = "Foodie"
private const val BAKING = "Baking"
private const val SWIMMING = "Swimming"
private const val NETFLIX = "Netflix"
private const val OUTDOORS = "Outdoors"
private const val MUSIC = "Music"
private const val MOVIES = "Movies"
private const val CLIMBING = "Climbing"
private const val FISHING = "Fishing"
private const val CAT_LOVER = "Cat Lover"
private const val READING = "Reading"
private const val FOOTBALL = "Football"
private const val SPIRITUALITY = "Spirituality"
private const val GARDENING = "Gardening"

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
            setCheckedChips(chipGroup, passions)
    }

    override fun onBackPressed() {
        if (passionsAreCorrect()) {
            super.onBackPressed()
        }
    }

    private fun passionsAreCorrect(): Boolean {
        findViewById<TextView>(R.id.warning_p7_1).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning_p7_2).visibility = View.INVISIBLE

        val ids = chipGroup.checkedChipIds
        val size = ids.size

        return when {
            //none selected
            size < 1 -> {
                findViewById<TextView>(R.id.warning_p7_1).visibility = View.VISIBLE
                false
            }
            size > SELECTION_LIMIT -> {
                findViewById<TextView>(R.id.warning_p7_2).visibility = View.VISIBLE
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
        passions.forEach {
            when (it) {
                BRUNCH -> chipGroup.check(R.id.chip10)
                WINE -> chipGroup.check(R.id.chip11)
                FASHION -> chipGroup.check(R.id.chip12)
                CYCLING -> chipGroup.check(R.id.chip13)
                RUNNING -> chipGroup.check(R.id.chip14)
                TEA -> chipGroup.check(R.id.chip15)
                COFFEE -> chipGroup.check(R.id.chip16)
                COMEDY -> chipGroup.check(R.id.chip17)
                WALKING -> chipGroup.check(R.id.chip18)
                BOARD_GAMES -> chipGroup.check(R.id.chip19)
                YOGA -> chipGroup.check(R.id.chip20)
                KARAOKE -> chipGroup.check(R.id.chip21)
                DOG_LOVER -> chipGroup.check(R.id.chip22)
                GAMER -> chipGroup.check(R.id.chip23)
                ART -> chipGroup.check(R.id.chip24)
                COCKTAILS -> chipGroup.check(R.id.chip25)
                DANCING -> chipGroup.check(R.id.chip26)
                PHOTOGRAPHY -> chipGroup.check(R.id.chip27)
                WRITER -> chipGroup.check(R.id.chip28)
                FOODIE -> chipGroup.check(R.id.chip29)
                BAKING -> chipGroup.check(R.id.chip30)
                SWIMMING -> chipGroup.check(R.id.chip31)
                NETFLIX -> chipGroup.check(R.id.chip32)
                OUTDOORS -> chipGroup.check(R.id.chip33)
                MUSIC -> chipGroup.check(R.id.chip34)
                MOVIES -> chipGroup.check(R.id.chip35)
                CLIMBING -> chipGroup.check(R.id.chip36)
                FISHING -> chipGroup.check(R.id.chip37)
                CAT_LOVER -> chipGroup.check(R.id.chip38)
                READING -> chipGroup.check(R.id.chip39)
                FOOTBALL -> chipGroup.check(R.id.chip40)
                SPIRITUALITY -> chipGroup.check(R.id.chip41)
                GARDENING -> chipGroup.check(R.id.chip42)
            }
        }
    }
}
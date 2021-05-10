package ch.epfl.sdp.blindly.profile_edit

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.PASSIONS
import com.google.android.material.chip.ChipGroup

const val BRUNCH = "Brunch"
const val WINE = "Wine"
const val FASHION = "Fashion"
const val CYCLING = "Cycling"
const val RUNNING = "Running"
const val TEA = "Tea"
const val COFFEE = "Coffee"
const val COMEDY = "Comedy"
const val WALKING = "Walking"
const val BOARD_GAMES = "Borad Games"
const val YOGA = "Yoga"
const val KARAOKE = "Karaoke"
const val DOG_LOVER = "Dog Lover"
const val GAMER = "Gamer"
const val ART = "Art"
const val COCKTAILS = "Cocktails"
const val DANCING = "Dancing"
const val PHOTOGRAPHY = "Photography"
const val WRITER = "Writer"
const val FOODIE = "Foodie"
const val BAKING = "Baking"
const val SWIMMING = "Swimming"
const val NETFLIX = "Netflix"
const val OUTDOORS = "Outdoors"
const val MUSIC = "Music"
const val MOVIES = "Movies"
const val CLIMBING = "Climbing"
const val FISHING = "Fishing"
const val CAT_LOVER = "Cat Lover"
const val READING = "Reading"
const val FOOTBALL = "Football"
const val SPIRITUALITY = "Spirituality"
const val GARDENING = "Gardening"

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
     * Set the chipGroup to reflect the given passions
     *
     * @param passions
     */
    private fun setCheckedChips(passions: ArrayList<String>) {
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
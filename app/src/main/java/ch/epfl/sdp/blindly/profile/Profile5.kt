package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import com.google.android.material.chip.ChipGroup


class Profile5 : AppCompatActivity() {

    private val limitSelection = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_5)
    }

    fun startProfile6(view: View) {
        findViewById<TextView>(R.id.warning_p5_1).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning_p5_2).visibility = View.INVISIBLE

        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup_p5)
        val ids = chipGroup.checkedChipIds
        val size = ids.size

        when {
            //none selected
            size < 1 -> {
                findViewById<TextView>(R.id.guideline_p5).visibility = View.INVISIBLE
                findViewById<TextView>(R.id.warning_p5_1).visibility = View.VISIBLE
            }
            size > limitSelection -> {
                findViewById<TextView>(R.id.guideline_p5).visibility = View.INVISIBLE
                findViewById<TextView>(R.id.warning_p5_2).visibility = View.VISIBLE
            }
            //correct numbers of selection
            else -> {
                val intent = Intent(this, Profile6::class.java)
                startActivity(intent)
            }
        }

    }
}
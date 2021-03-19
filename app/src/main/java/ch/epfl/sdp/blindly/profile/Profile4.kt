package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

class Profile4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_4)
    }

    fun startProfile4_2Or5(view: View) {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val radioButtonMore = findViewById<RadioButton>(R.id.sex3_more)

        when {
            //No radio button is checked
            radioGroup.checkedRadioButtonId == -1 -> {
                findViewById<TextView>(R.id.warning_p4).visibility = View.VISIBLE
            }
            //more option is checked
            radioButtonMore.isChecked -> {
                val intent = Intent(this, Profile4_2::class.java)
                startActivity(intent)
            }
            //man or woman is checked
            else -> {
                val intent = Intent(this, Profile5::class.java)
                startActivity(intent)
            }
        }
    }

}
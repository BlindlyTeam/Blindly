package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

class Profile6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_6)
    }

    fun startProfile7(view: View) {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup_p6)
        findViewById<TextView>(R.id.warning_p6).visibility = View.INVISIBLE

        //nothing is checked
        if (radioGroup.checkedRadioButtonId == -1) {
            findViewById<TextView>(R.id.warning_p6).visibility = View.VISIBLE
        } else {
            val intent = Intent(this, Profile7::class.java)
            startActivity(intent)
        }
    }

}
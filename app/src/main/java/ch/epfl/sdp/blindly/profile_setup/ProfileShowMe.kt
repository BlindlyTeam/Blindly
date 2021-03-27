package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

class ProfileShowMe : AppCompatActivity() {

    private val NONE_CHECKED = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_show_me)
    }

    fun startProfile7(view: View) {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup_p6)
        findViewById<TextView>(R.id.warning_p6).visibility = View.INVISIBLE

        //nothing is checked
        if (radioGroup.checkedRadioButtonId == NONE_CHECKED) {
            findViewById<TextView>(R.id.warning_p6).visibility = View.VISIBLE
        } else {
            val intent = Intent(this, ProfilePassions::class.java)
            startActivity(intent)
        }
    }

}
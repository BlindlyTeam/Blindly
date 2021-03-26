package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

private val REGEX = Regex("^[a-zA-Z]*$")

class Profile4_2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_4_2)
    }

    fun startProfile5(view: View) {
        findViewById<TextView>(R.id.warning1_p4_2).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning2_p4_2).visibility = View.INVISIBLE

        val name = findViewById<TextView>(R.id.text_p4_2).text.toString().trim()

        if (!name.matches(REGEX)) {
            findViewById<TextView>(R.id.warning2_p4_2).visibility = View.VISIBLE
        } else {
            val len = name.length
            if (len > 0) {
                val intent = Intent(this, Profile5::class.java)
                startActivity(intent)
            } else {
                //empty text, output error
                findViewById<TextView>(R.id.warning1_p4_2).visibility = View.VISIBLE
            }
        }
    }
}
package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R


private const val MIN_LENGTH = 2
private const val MAX_LENGTH = 20
private val REGEX = Regex("^[a-zA-Z]*$")

class ProfileName : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_name)
    }

    fun startProfile3(view: View) {
        findViewById<TextView>(R.id.warning1_p2).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning2_p2).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning3_p2).visibility = View.INVISIBLE

        val name = findViewById<TextView>(R.id.text_first_name).text.toString().trim()

        if (!name.matches(REGEX)) {
            findViewById<TextView>(R.id.warning3_p2).visibility = View.VISIBLE
        } else {
            val len = name.length
            when {
                len < MIN_LENGTH -> {
                    findViewById<TextView>(R.id.warning1_p2).visibility = View.VISIBLE
                }
                len > MAX_LENGTH -> {
                    findViewById<TextView>(R.id.warning2_p2).visibility = View.VISIBLE
                }
                else -> {
                    val intent = Intent(this, ProfileBirthday::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
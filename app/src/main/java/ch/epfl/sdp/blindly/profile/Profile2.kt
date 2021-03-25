package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R


private const val MIN_LENGTH = 2
private const val MAX_LENGTH = 20
const val EXTRA_USERNAME = "username"
class Profile2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_2)
    }

    fun startProfile3(view: View) {
        findViewById<TextView>(R.id.warning1_p2).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning2_p2).visibility = View.INVISIBLE

        val name = findViewById<TextView>(R.id.text_first_name).text.toString().trim()
        val len = name.length
        when {
            len < MIN_LENGTH -> {
                findViewById<TextView>(R.id.warning1_p2).visibility = View.VISIBLE
            }
            len > MAX_LENGTH -> {
                findViewById<TextView>(R.id.warning2_p2).visibility = View.VISIBLE
            }
            else -> {
                val intent = Intent(this, Profile3::class.java).apply {
                    putExtra(EXTRA_USERNAME, name)
                }
                startActivity(intent)
            }
        }
    }
}
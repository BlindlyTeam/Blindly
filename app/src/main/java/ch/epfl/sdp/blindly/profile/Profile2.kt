package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R


private const val MIN_LENGTH = 2
private const val MAX_LENGTH = 20
<<<<<<< HEAD
const val EXTRA_USERNAME = "username"
=======
private val REGEX = Regex("^[a-zA-Z]*$")

>>>>>>> origin/main
class Profile2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_2)
    }

    fun startProfile3(view: View) {
        findViewById<TextView>(R.id.warning1_p2).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning2_p2).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning3_p2).visibility = View.INVISIBLE

        val name = findViewById<TextView>(R.id.text_first_name).text.toString().trim()
<<<<<<< HEAD
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
=======

        if(!name.matches(REGEX)){
            findViewById<TextView>(R.id.warning3_p2).visibility = View.VISIBLE
        }else {
            val len = name.length
            when {
                len < MIN_LENGTH -> {
                    findViewById<TextView>(R.id.warning1_p2).visibility = View.VISIBLE
                }
                len > MAX_LENGTH -> {
                    findViewById<TextView>(R.id.warning2_p2).visibility = View.VISIBLE
                }
                else -> {
                    val intent = Intent(this, Profile3::class.java)
                    startActivity(intent)
                }
>>>>>>> origin/main
            }
        }
    }
}
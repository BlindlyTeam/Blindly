package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

private val REGEX = Regex("^[a-zA-Z]*$")

class ProfileGenderMore : AppCompatActivity() {

    private var username: String? = null
    private var birthday: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_gender_more)
        val bundle = intent.extras
        username = bundle?.getString(EXTRA_USERNAME)
        birthday = bundle?.getString(EXTRA_BIRTHDAY)
    }

    fun startProfileOrientation(view: View) {
        findViewById<TextView>(R.id.warning1_p4_2).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning2_p4_2).visibility = View.INVISIBLE

        val name = findViewById<TextView>(R.id.text_p4_2).text.toString().trim()

        if (!name.matches(REGEX)) {
            findViewById<TextView>(R.id.warning2_p4_2).visibility = View.VISIBLE
        } else {
            val len = name.length
            if (len > 0) {
                val intent = Intent(this, ProfileOrientation::class.java)
                val extras = Bundle()
                extras.putString(EXTRA_USERNAME, username)
                extras.putString(EXTRA_BIRTHDAY, birthday)
                extras.putString(EXTRA_GENRE, name)
                intent.putExtras(extras)
                startActivity(intent)
            } else {
                //empty text, output error
                findViewById<TextView>(R.id.warning1_p4_2).visibility = View.VISIBLE
            }
        }
    }
}
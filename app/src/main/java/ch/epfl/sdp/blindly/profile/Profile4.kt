package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

const val EXTRA_GENRE = "user_genre"
class Profile4 : AppCompatActivity() {

    private var username: String ?= null
    private var age: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_4)

        val bundle = intent.extras
        username = bundle?.getString(EXTRA_USERNAME)
        age = bundle?.getString(EXTRA_BIRTHDAY)
    }

    fun startNextProfile(view: View) {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val radioButtonMore = findViewById<RadioButton>(R.id.sex3_more)
        val radioButtonWomen = findViewById<RadioButton>(R.id.sex1_user)
        val intent = Intent(this, Profile5::class.java)

        when {
            //No radio button is checked
            radioGroup.checkedRadioButtonId == -1 -> {
                findViewById<TextView>(R.id.warning_p4).visibility = View.VISIBLE
            }
            //more option is checked
            radioButtonMore.isChecked -> {
                val intentMore = Intent(this, Profile4_2::class.java)
                val genre = "More"
                bundleExtrasAndStartActivity(intentMore, username, age, genre)
            }
            //women is checked
            radioButtonWomen.isChecked -> {
                val genre = "Woman"
                bundleExtrasAndStartActivity(intent, username, age, genre)
            }
            //man is checked
            else -> {
                val genre = "Man"
                bundleExtrasAndStartActivity(intent, username, age, genre)
            }
        }
    }

    private fun bundleExtrasAndStartActivity(intent: Intent, username: String?, age: String?, genre: String) {
        val extras = Bundle()
        extras.putString(EXTRA_USERNAME, username)
        extras.putString(EXTRA_BIRTHDAY, age)
        extras.putString(EXTRA_GENRE, genre)
        intent.putExtras(extras)

        startActivity(intent)
    }
}
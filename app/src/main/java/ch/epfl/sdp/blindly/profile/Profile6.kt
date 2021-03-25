package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

const val EXTRA_SHOW_ME = "show_me"
class Profile6 : AppCompatActivity() {

    private val NONE_CHECKED = -1;
    private var username: String? = null
    private var age: String? = null
    private var genre: String? = null
    private var sexualOriantations : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_6)

        val bundle = intent.extras
        username = bundle?.getString(EXTRA_USERNAME)
        age = bundle?.getString(EXTRA_BIRTHDAY)
        genre = bundle?.getString(EXTRA_GENRE)
        if(bundle != null)
            sexualOriantations = bundle.getStringArrayList(EXTRA_SEXUAL_ORIENTATIONS) as ArrayList<String>
    }

    fun startProfile7(view: View) {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup_p6)
        findViewById<TextView>(R.id.warning_p6).visibility = View.INVISIBLE

        //nothing is checked
        if (radioGroup.checkedRadioButtonId == NONE_CHECKED) {
            findViewById<TextView>(R.id.warning_p6).visibility = View.VISIBLE
        } else {
            val intent = Intent(this, Profile7::class.java)
            val showMe = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                    .text.toString().toLowerCase().capitalize()
            val extras = Bundle()
            extras.putString(EXTRA_USERNAME, username)
            extras.putString(EXTRA_BIRTHDAY, age)
            extras.putString(EXTRA_GENRE, genre)
            extras.putStringArrayList(EXTRA_SEXUAL_ORIENTATIONS, sexualOriantations)
            extras.putString(EXTRA_SHOW_ME, showMe)
            intent.putExtras(extras)
            startActivity(intent)
        }
    }

}
package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Activity that asks the gender of the user.
 */
class ProfileGender : AppCompatActivity() {
    private lateinit var userBuilder: User.Builder
    private lateinit var nextIntent: Intent
    private lateinit var gender: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_gender)

        val bundle = intent.extras
        userBuilder = bundle?.getString(EXTRA_USER)?.let { Json.decodeFromString(it) }!!
    }

    /**
     * Controls the radio buttons and gives the input to builder, if
     * the user opts for More, then another intent is fired to let the user precise.
     *
     * @param view the current view
     */
    fun startProfileOrientationOrGenderMore(view: View) {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val radioButtonMore = findViewById<RadioButton>(R.id.sex3_more)
        val radioButtonWomen = findViewById<RadioButton>(R.id.sex1_user)
        nextIntent = Intent(this, ProfileOrientation::class.java)

        when {
            //No radio button is checked
            radioGroup.checkedRadioButtonId == -1 -> {
                findViewById<TextView>(R.id.warning_p4).visibility = View.VISIBLE
            }
            //more option is checked
            radioButtonMore.isChecked -> {
                nextIntent = Intent(this, ProfileGenderMore::class.java)
                gender = "More"
                bundleExtrasAndStartActivity()
            }
            //women is checked
            radioButtonWomen.isChecked -> {
                gender = "Woman"
                bundleExtrasAndStartActivity()
            }
            //man is checked
            else -> {
                gender = "Man"
                bundleExtrasAndStartActivity()
            }
        }
    }

    private fun bundleExtrasAndStartActivity() {
        val bundle = Bundle()
        userBuilder.setGender(gender)
        bundle.putSerializable(
            EXTRA_USER,
            Json.encodeToString(User.Builder.serializer(), userBuilder)
        )
        nextIntent.putExtras(bundle)

        startActivity(nextIntent)
    }
}
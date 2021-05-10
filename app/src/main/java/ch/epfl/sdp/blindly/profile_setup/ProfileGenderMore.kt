package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val REGEX = Regex("^[a-zA-Z]*$")

/**
 * Activity that allows the user to specify a certain gender.
 */
class ProfileGenderMore : AppCompatActivity() {
    private lateinit var userBuilder: User.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_gender_more)

        val bundle = intent.extras
        userBuilder = bundle?.getString(EXTRA_USER)?.let { Json.decodeFromString(it) }!!
    }

    /**
     * Lets user to precise their gender, in order to avoid incomprehensible text
     * or numbers user input is checked to be alpabetical. If it's not alphabetical or no input
     * is given, an error message is shown. Otherwise Orientation page is started.
     *
     * @param view the current view
     */
    fun startProfileOrientation(view: View) {
        findViewById<TextView>(R.id.warning1_p4_2).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning2_p4_2).visibility = View.INVISIBLE

        val gender = findViewById<TextView>(R.id.text_p4_2).text.toString().trim()

        if (!gender.matches(REGEX)) {
            //incorrect format, output error
            findViewById<TextView>(R.id.warning2_p4_2).visibility = View.VISIBLE
        } else {
            val len = gender.length
            if (len > 0) {
                val intent = Intent(this, ProfileOrientation::class.java)
                val bundle = Bundle()
                userBuilder.setGender(gender)
                bundle.putSerializable(
                    EXTRA_USER,
                    Json.encodeToString(User.Builder.serializer(), userBuilder)
                )
                intent.putExtras(bundle)

                startActivity(intent)
            } else {
                //empty text, output error
                findViewById<TextView>(R.id.warning1_p4_2).visibility = View.VISIBLE
            }
        }
    }
}
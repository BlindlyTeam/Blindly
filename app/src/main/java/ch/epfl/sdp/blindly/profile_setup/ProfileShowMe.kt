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

private const val NONE_CHECKED = -1

/**
 * Activity that allow the user to specify which gender he wants to see.
 */
class ProfileShowMe : AppCompatActivity() {
    private lateinit var userBuilder: User.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_show_me)

        val bundle = intent.extras
        userBuilder = bundle?.getString(EXTRA_USER)?.let { Json.decodeFromString(it) }!!
    }

    /**
     * Gets the choice of the user in order to determine which genders to show,
     * if none is checked outputs error otherwise passes the choice to builder and starts
     * ProfilePassions
     *
     * @param view the current view
     */
    fun startProfilePassions(view: View) {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup_p6)
        findViewById<TextView>(R.id.warning_p6).visibility = View.INVISIBLE

        //nothing is checked
        if (radioGroup.checkedRadioButtonId == NONE_CHECKED) {
            findViewById<TextView>(R.id.warning_p6).visibility = View.VISIBLE
        } else {
            val intent = Intent(this, ProfilePassions::class.java)
            val showMe = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                .text.toString().toLowerCase().capitalize()
            val bundle = Bundle()
            userBuilder.setShowMe(showMe)
            bundle.putSerializable(
                EXTRA_USER,
                Json.encodeToString(User.Builder.serializer(), userBuilder)
            )
            intent.putExtras(bundle)

            startActivity(intent)
        }
    }
}
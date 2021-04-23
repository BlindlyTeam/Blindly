package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.Period

private const val PAD_CHAR = '0'
private const val FORMAT = 2
const val MAJORITY_AGE = 18

/**
 * Gets the birthday of the user and do the checks on age.
 */
class ProfileBirthday : AppCompatActivity() {

    private lateinit var userBuilder: User.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_birthday)

        val bundle = intent.extras
        if (bundle != null) {
            userBuilder = bundle.getString(EXTRA_USER)?.let { Json.decodeFromString(it) }!!
        }
    }

    /**
     * Uses a DatePicker in spinner format, calculates the age of the user via helper function
     * getAge and if user is a minor prompts error, otherwise passes the age information to
     * the builder and starts the ProfileGender activity
     * @param view the current view
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun startProfileGender(view: View) {
        findViewById<TextView>(R.id.warning_p3).visibility = View.INVISIBLE
        val datePicker: DatePicker = findViewById<View>(R.id.datePicker) as DatePicker
        val day: Int = datePicker.dayOfMonth
        val month: Int = datePicker.month + 1 //month correction
        val year: Int = datePicker.year
        val age = getAge(year, month, day)

        if (age < MAJORITY_AGE) {
            findViewById<TextView>(R.id.warning_p3).visibility = View.VISIBLE
        } else {
            val dayString = day.toString().padStart(FORMAT, PAD_CHAR)
            val monthString = month.toString().padStart(FORMAT, PAD_CHAR)
            val birthday = "$dayString.$monthString.$year"

            val bundle = Bundle()
            userBuilder.setBirthday(birthday)
            bundle.putSerializable(
                EXTRA_USER,
                Json.encodeToString(User.Builder.serializer(), userBuilder)
            )
            val intent = Intent(this, ProfileGender::class.java)
            intent.putExtras(bundle)

            startActivity(intent)
        }
    }

    //Helper function to get the age via day, month, year inputs
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
        return Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    }

}
package ch.epfl.sdp.blindly.profile_edit

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.fragments.ProfilePageFragment

const val MIN_LENGTH = 2
const val MAX_LENGTH = 20
const val EXTRA_USER = "user"
private val REGEX = Regex("^[a-zA-Z]*$")

class EditUsername : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_username)
        supportActionBar?.hide()

        val updateUsername = findViewById<Button>(R.id.update_username)
        setOnClickListener(updateUsername)
    }

    private fun setOnClickListener(button: Button) {
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bouncy_button)
        button.setOnClickListener {
            button.startAnimation(bounce)
            Handler(Looper.getMainLooper()).postDelayed({
                findViewById<TextView>(R.id.warning1_p2).visibility = View.INVISIBLE
                findViewById<TextView>(R.id.warning2_p2).visibility = View.INVISIBLE
                findViewById<TextView>(R.id.warning3_p2).visibility = View.INVISIBLE
                findViewById<TextView>(R.id.update_username_success_notice).visibility =
                    View.INVISIBLE
                findViewById<TextView>(R.id.update_username_failure_notice).visibility =
                    View.INVISIBLE

                val username = findViewById<EditText>(R.id.edit_username).text.toString()
                if (!username.matches(REGEX)) {
                    findViewById<TextView>(R.id.warning3_p2).visibility = View.VISIBLE
                } else {
                    val len = username.length
                    when {
                        len < MIN_LENGTH -> {
                            findViewById<TextView>(R.id.warning1_p2).visibility = View.VISIBLE
                        }
                        len > MAX_LENGTH -> {
                            findViewById<TextView>(R.id.warning2_p2).visibility = View.VISIBLE
                        }
                        else -> {
                            findViewById<TextView>(R.id.update_username_success_notice).visibility =
                                View.VISIBLE
                        }
                    }
                }
            }, ProfilePageFragment.BOUNCE_DURATION)
        }
    }
}
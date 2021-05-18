package ch.epfl.sdp.blindly.main_screen.profile.edit

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.profile.ProfilePageFragment
import ch.epfl.sdp.blindly.user.USERNAME
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.viewmodel.UserViewModel
import ch.epfl.sdp.blindly.viewmodel.ViewModelAssistedFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val MIN_LENGTH = 2
const val MAX_LENGTH = 20
const val EXTRA_USER = "user"
private val REGEX = Regex("^[a-zA-Z]*$")

@AndroidEntryPoint
class EditUsername : AppCompatActivity() {

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    private lateinit var viewModel: UserViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_username)

        val uid = userHelper.getUserId()
        viewModel = UserViewModel.instantiateViewModel(
            uid,
            assistedFactory,
            this,
            this
        )

        val updateUsername = findViewById<Button>(R.id.update_username)
        setOnClickListener(updateUsername)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setOnClickListener(button: Button) {
        button.setOnClickListener {
            hideAllWarning()
            val username = findViewById<EditText>(R.id.edit_username).text.toString()
            if (usernameIsCorrect(username))
                viewModel.updateField(USERNAME, username)
        }
    }

    private fun usernameIsCorrect(username: String): Boolean {
        if (!username.matches(REGEX)) {
            findViewById<TextView>(R.id.warning3_p2).visibility = VISIBLE
            return false
        } else {
            val len = username.length
            return when {
                len < MIN_LENGTH -> {
                    findViewById<TextView>(R.id.warning1_p2).visibility = VISIBLE
                    false
                }
                len > MAX_LENGTH -> {
                    findViewById<TextView>(R.id.warning2_p2).visibility = VISIBLE
                    false
                }
                else -> {
                    findViewById<TextView>(R.id.update_username_success_notice).visibility =
                        VISIBLE
                    true
                }
            }
        }
    }

    private fun hideAllWarning() {
        findViewById<TextView>(R.id.warning1_p2).visibility = INVISIBLE
        findViewById<TextView>(R.id.warning2_p2).visibility = INVISIBLE
        findViewById<TextView>(R.id.warning3_p2).visibility = INVISIBLE
        findViewById<TextView>(R.id.update_username_success_notice).visibility =
            INVISIBLE
    }
}
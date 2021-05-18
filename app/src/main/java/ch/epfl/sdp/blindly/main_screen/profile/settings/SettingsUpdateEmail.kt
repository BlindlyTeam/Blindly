package ch.epfl.sdp.blindly.main_screen.profile.settings

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Activity to change the email of the user.
 */
@AndroidEntryPoint
class SettingsUpdateEmail : AppCompatActivity() {

    @Inject
    lateinit var user: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_update_email)

        val editText: EditText = findViewById<EditText>(R.id.update_email_field)
        editText.hint = user.getEmail()
    }

    /**
     * Handler for the update e-mail button
     *
     * Update the e-mail in the database and handle errors and result
     *
     * @param view the button
     */
    fun updateEmail(view: View) {
        val editText: EditText = findViewById(R.id.update_email_field)
        val newEmail = editText.text.toString()

        val task = user.setEmail(newEmail)
        task?.addOnSuccessListener {
            // When it succeeds show success notice, and hide failure notice
            findViewById<TextView>(R.id.update_email_failure_notice).visibility = View.GONE
            findViewById<TextView>(R.id.update_email_success_notice).visibility = View.VISIBLE
        }
        // When something wrong happens
        task?.addOnFailureListener { exception ->
            val errMsg = when (exception) {
                is FirebaseAuthInvalidCredentialsException -> R.string.invalid_email
                is FirebaseAuthUserCollisionException -> R.string.email_taken
                is FirebaseAuthInvalidUserException -> R.string.try_to_logout
                is FirebaseAuthRecentLoginRequiredException -> R.string.try_to_logout
                else -> R.string.update_email_unknown_error
            }
            findViewById<TextView>(R.id.update_email_success_notice).visibility = View.GONE
            val failureNotice = findViewById<TextView>(R.id.update_email_failure_notice)
            failureNotice.visibility = View.VISIBLE
            failureNotice.setText(errMsg)
        }
    }
}
package ch.epfl.sdp.blindly

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R.id.button
import ch.epfl.sdp.blindly.utils.UserHelper
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupButton()

    }

    private fun setupButton() {
        val button = findViewById<Button>(button)
        if (UserHelper.isLoggedIn()) {
            button.setText(R.string.home_button_user_loggedin)
            button.setOnClickListener { startActivity(Intent(this, MyAccountActivity::class.java)) }
        } else {
            button.setText(R.string.home_button_user_loggedout)
            button.setOnClickListener {
                startActivityForResult(UserHelper.getSignInIntent(), UserHelper.RC_SIGN_IN )
            }
        }
    }


    // [START auth_fui_result]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UserHelper.RC_SIGN_IN) {
            if (UserHelper.handleAuthResult(this, resultCode, data) )
                setupButton();
        }
    }
}
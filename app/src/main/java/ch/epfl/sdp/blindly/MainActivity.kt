package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.utils.UserHelper


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)    }

    fun start_profile_1(view: View) {
        val intent = Intent(this, Profile1::class.java)
        startActivity(intent)
    }

    fun openLogin() {
        startActivityForResult(UserHelper.getSignInIntent(), UserHelper.RC_SIGN_IN);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val success = UserHelper.handleAuthResult(this, resultCode, data)
        // Open the rest if the login is successful
        if (success) {
            val intent = Intent(this, Profile1::class.java)
            startActivity(intent)
        }
    }
    fun start_main_screen(view: View) {
        val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)
    }
}
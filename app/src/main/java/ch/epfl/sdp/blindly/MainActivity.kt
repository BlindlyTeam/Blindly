package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.profile_setup.ProfileHouseRules
import ch.epfl.sdp.blindly.utils.UserHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var user: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startHouseRules(view: View) {
        val intent = Intent(this, ProfileHouseRules::class.java)
        startActivity(intent)
    }

    fun openLogin(view: View) {
        startActivityForResult(user.getSignInIntent(), UserHelper.RC_SIGN_IN);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val success = user.handleAuthResult(this, resultCode, data)
        // Open the rest if the login is successful
        if (success) {
            val intent = Intent(this, ProfileHouseRules::class.java)
            startActivity(intent)
        }
    }

    fun startMainScreen(view: View) {
        val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)
    }
}
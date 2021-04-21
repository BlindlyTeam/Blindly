package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.MainScreen

class ProfileFinished : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_finished)
    }

    /**
     * Starts main screen activity
     *
     * @param v the current view
     */
    fun startMainScreen(v: View) {
        val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)
    }
}
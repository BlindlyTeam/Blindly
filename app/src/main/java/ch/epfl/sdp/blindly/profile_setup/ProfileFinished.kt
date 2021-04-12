package ch.epfl.sdp.blindly.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ch.epfl.sdp.blindly.MainScreen
import ch.epfl.sdp.blindly.R

class ProfileFinished : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_finished)
    }

    fun startMainScreen(v: View) {
        val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)
    }
}
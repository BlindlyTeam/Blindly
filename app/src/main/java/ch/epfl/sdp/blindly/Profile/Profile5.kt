package ch.epfl.sdp.blindly.Profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

class Profile5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_5)
    }

    fun start_profile_6(view: View) {
        val intent = Intent(this, Profile6::class.java)
        startActivity(intent)
    }
}
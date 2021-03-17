package ch.epfl.sdp.blindly.Profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

class Profile3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_3)
    }

    fun start_profile_4(view: View) {
        val intent = Intent(this, Profile4::class.java)
        startActivity(intent)
    }

}
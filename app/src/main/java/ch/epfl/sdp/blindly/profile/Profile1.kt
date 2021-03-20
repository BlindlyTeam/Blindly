package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

class Profile1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_1)
    }

    fun startProfile2(view: View) {
        val intent = Intent(this, Profile2::class.java)
        startActivity(intent)
    }

}
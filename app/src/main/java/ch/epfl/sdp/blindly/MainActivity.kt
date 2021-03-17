package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.Profile.Profile1


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun start_profile_1(view: View) {
        // Do something in response to button
        val intent = Intent(this, Profile1::class.java)
        startActivity(intent)
    }


}
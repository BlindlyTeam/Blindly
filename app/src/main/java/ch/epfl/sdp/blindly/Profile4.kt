package ch.epfl.sdp.blindly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Profile4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_4)
    }

    fun start_profile_5(view: View) {
        // Do something in response to button
        val intent = Intent(this, Profile5::class.java)
        startActivity(intent)
    }

    fun start_profile_4_2(view: View) {
        // Do something in response to button
        val intent = Intent(this, Profile4_2::class.java)
        startActivity(intent)
    }


}
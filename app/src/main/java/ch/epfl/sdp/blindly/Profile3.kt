package ch.epfl.sdp.blindly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Profile3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_3)
    }

    fun start_profile_4(view: View) {
        // Do something in response to button
        val intent = Intent(this, Profile4::class.java)
        startActivity(intent)
    }

}
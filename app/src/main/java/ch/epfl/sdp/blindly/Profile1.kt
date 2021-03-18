package ch.epfl.sdp.blindly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Profile1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_1)
    }

    fun start_profile_2(view: View) {
        // Do something in response to button
        val intent = Intent(this, Profile2::class.java)
        startActivity(intent)
    }

}
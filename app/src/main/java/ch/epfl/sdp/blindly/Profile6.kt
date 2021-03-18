package ch.epfl.sdp.blindly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Profile6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_6)
    }
    fun start_profile_7(view: View) {
        // Do something in response to button
        val intent = Intent(this, Profile7::class.java)
        startActivity(intent)
    }
}
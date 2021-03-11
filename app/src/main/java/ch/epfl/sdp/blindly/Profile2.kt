package ch.epfl.sdp.blindly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View

class Profile2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_2)
    }

    fun start_profile_3(view: View) {
        // Do something in response to button
        val intent = Intent(this, Profile3::class.java)
        startActivity(intent)
    }


    }
package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Profile4_2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_4_2)
    }

    fun start_profile_5(view: View) {
        // Do something in response to button
        val genderOther = findViewById<TextView>(ch.epfl.sdp.blindly.R.id.text_p4_2)
        if (genderOther.length() > 0) {
            val intent = Intent(this, Profile5::class.java)
            startActivity(intent)
        }


    }

}
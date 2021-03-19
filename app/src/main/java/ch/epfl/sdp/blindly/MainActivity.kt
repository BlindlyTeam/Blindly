package ch.epfl.sdp.blindly


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

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

    fun start_main_screen(view:View) {
        val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)
    }

    fun start_maps(view:View) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}
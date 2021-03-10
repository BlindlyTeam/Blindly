package ch.epfl.sdp.blindly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.goButton)

        button.setOnClickListener {
            val intent = Intent(
                this, MapsActivity::class.java)
            startActivity (intent)
        }
    }
}
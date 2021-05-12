package ch.epfl.sdp.blindly.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
    }
}
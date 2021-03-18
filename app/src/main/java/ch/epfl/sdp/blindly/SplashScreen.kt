package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


const val MAIN_SCREEN_DELAY : Long = 2500
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        val heart = findViewById<ImageView>(R.id.splashscreen_heart)
        val beating = AnimationUtils.loadAnimation(this, R.anim.beating_heart)

        val interpolator = BounceInterpolator(0.3, 20.0)
        beating.interpolator = interpolator

        heart.startAnimation(beating)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        }, MAIN_SCREEN_DELAY)
    }
}
package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


val MAIN_SCREEN_DELAY : Long = 2500
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        val heart = findViewById<ImageView>(R.id.splashscreen_heart)
        val beating = AnimationUtils.loadAnimation(this, R.anim.beating_heart)

        // Use bounce interpolator with amplitude 0.2 and frequency 20

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        val interpolator = BounceInterpolator(0.2, 20.0)
        beating.interpolator = interpolator

        heart.startAnimation(beating)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        }, MAIN_SCREEN_DELAY)
    }
}
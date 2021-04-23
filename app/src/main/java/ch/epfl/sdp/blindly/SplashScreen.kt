package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.animations.BounceInterpolator
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


const val MAIN_SCREEN_DELAY: Long = 2500

/**
 * The SplashScreen Activity starts when the app is launched
 *
 */
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    @Inject
    lateinit var user: UserHelper

    @Inject
    lateinit var userRepository: UserRepository

    companion object {
        const val TAG = "SplashScreen"
    }

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
            if (user.isLoggedIn()) {
                val intent = Intent(this, MainScreen::class.java)
                startActivity(intent)
            } else {
                startActivityForResult(user.getSignInIntent(), UserHelper.RC_SIGN_IN)
            }
        }, MAIN_SCREEN_DELAY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "data: $data")
        super.onActivityResult(requestCode, resultCode, data)
        val nextIntent = user.handleAuthResult(this, resultCode, data)
        // Open the rest if the login is successful
        if (nextIntent != null) {
            startActivity(nextIntent)
        }
    }

}
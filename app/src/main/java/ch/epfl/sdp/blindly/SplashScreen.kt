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
import ch.epfl.sdp.blindly.database.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

const val MAIN_SCREEN_DELAY: Long = 2500

/**
 * The SplashScreen Activity starts when the app is launched
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

        val isNewUser = GlobalScope.async { user.isNewUser() }
        heart.startAnimation(beating)
        Handler(Looper.getMainLooper()).postDelayed({
            runBlocking {
                if (user.isLoggedIn()) {
                    if (!isNewUser.await()) {
                        val intent = Intent(this@SplashScreen, MainScreen::class.java)
                        startActivity(intent)
                    } else {
                        // If new user, setup profile page
                        startActivity(user.getProfileSetupIntent(this@SplashScreen))
                    }
                } else {
                    startActivityForResult(user.getSignInIntent(), UserHelper.RC_SIGN_IN)
                }
            }
        }, MAIN_SCREEN_DELAY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        runBlocking(Dispatchers.IO) {
            val nextIntent = user.handleAuthResult(this@SplashScreen, resultCode, data)
            // Open the rest if the login is successful
            if (nextIntent != null) {
                startActivity(nextIntent)
            }
        }
    }
}
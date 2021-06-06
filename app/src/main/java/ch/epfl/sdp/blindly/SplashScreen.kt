package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.animations.BounceInterpolator
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.user.UserHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

const val MAIN_SCREEN_DELAY: Long = 2500

/**
 * The SplashScreen Activity starts when the app is launched
 */
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private var animationFinished: Boolean = false
    private var waitingAuthResult: Boolean = true
    private lateinit var isNewUser: Deferred<Boolean>

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

        isNewUser = GlobalScope.async { user.isNewUser() }
        heart.startAnimation(beating)
        Handler(Looper.getMainLooper()).postDelayed({
            animationFinished = true
            launchNext()

        }, MAIN_SCREEN_DELAY)
    }

    override fun onResume() {
        super.onResume()
        // If animation is already finished the callback won't be called again
        // so we have to be sure to run the launch the next activity
        // in order not to be blocked on this one
        if (animationFinished)
            if (!waitingAuthResult)
                launchNext()
            else
                waitingAuthResult = false
    }

    private fun launchNext() {
        if (user.isLoggedIn()) {
            GlobalScope.launch(Dispatchers.Main) {
                if (!isNewUser.await()) {
                    val intent = Intent(this@SplashScreen, MainScreen::class.java)
                    startActivity(intent)
                } else {
                    // If new user, setup profile page
                    startActivity(user.getProfileSetupIntent(this@SplashScreen))
                }
            }
        } else {
            waitingAuthResult = true
            startActivityForResult(user.getSignInIntent(), UserHelper.RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        isNewUser = GlobalScope.async { user.isNewUser() }
        GlobalScope.launch(Dispatchers.Main) {
            val nextIntent = user.handleAuthResult(this@SplashScreen, resultCode, data)
            // Open the rest if the login is successful
            if (nextIntent != null) {
                startActivity(nextIntent)
            }
        }
    }
}
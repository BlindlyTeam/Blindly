package ch.epfl.sdp.blindly

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * The test runner for Blindly
 */
class BlindlyTestRunner : AndroidJUnitRunner() {

    /**
     * Create a hilt application to be used for the tests
     *
     * @param cl the class loader for the app
     * @param name app name, not used
     * @param context the app context
     *
     * @return Application the application
     */
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}

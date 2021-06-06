package ch.epfl.sdp.blindly.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import ch.epfl.sdp.blindly.BuildConfig
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.profile_setup.MAJORITY_AGE
import ch.epfl.sdp.blindly.profile_setup.ProfileHouseRules
import ch.epfl.sdp.blindly.utils.Date
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Class that contains helpful functions regarding the user.
 */
class UserHelper(private val userRepository: UserRepository) {
    companion object {
        const val RC_SIGN_IN = 123
        private const val TAG = "UserHelper"
        private const val USER_COLLECTION: String = "usersMeta"
        const val DEFAULT_RADIUS = 80
        const val DEFAULT_RANGE = 10
        const val EXTRA_UID = "uid"
    }

    /**
     * Get the sign-in intent
     *
     * Gets the intent for the activity used to sign-in and sign-up
     *
     * @return Intent the sign-intent
     */
    fun getSignInIntent(): Intent {
        // Optionnaly get phone number to set default in login form
        val phoneProvider = AuthUI.IdpConfig.PhoneBuilder()

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().setRequireName(false).build(),
            phoneProvider.build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        /* You must provide a custom layout XML resource and configure at least one
         * provider button ID. It's important that that you set the button ID for every provider
         * that you have enabled.
         */
        val customLayout = AuthMethodPickerLayout.Builder(R.layout.activity_log_in)
            .setGoogleButtonId(R.id.google_sign_in)
            .setEmailButtonId(R.id.email_sign_in)
            .setPhoneButtonId(R.id.phone_number_sign_in)
            .setFacebookButtonId(R.id.facebook_sign_in)
            .build()

        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */)
            .setAvailableProviders(providers)
            .setTheme(R.style.Theme_Blindly) // Set theme
            .setAuthMethodPickerLayout(customLayout)
            .build()

    }

    fun getProfileSetupIntent(activity: Activity) = Intent(activity, ProfileHouseRules::class.java)

    /**
     * Handle the authentication result in onActivityResult
     *
     * Returns an intent to launch the account creation steps if it is a new account,
     * otherwise an intent for main screen, also handle unsuccessful results
     *
     * @param activity the calling activity
     * @param resultCode the result code the activity callback received
     * @param data the result data
     *
     * @return Null if sign-in failed, an intent to be launched otherwise
     */
    suspend fun handleAuthResult(activity: Activity, resultCode: Int, data: Intent?): Intent? {
        val response = IdpResponse.fromResultIntent(data)

        if (resultCode == Activity.RESULT_OK) {
            // Successfully signed in
            if (isNewUser()) {
                return getProfileSetupIntent(activity)
            }
            return Intent(activity, MainScreen::class.java)
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            if (response != null) {
                Toast.makeText(
                    activity.applicationContext,
                    activity.getString(
                        R.string.login_err,
                        response.error?.errorCode ?: -1
                    ),
                    Toast.LENGTH_LONG
                ).show()
            }
            return null
        }
    }

    /**
     * Check is the user has already created an account
     *
     * @param user the user to test the condition on
     *
     * @return true if the user just created an account, false otherwise
     */
    suspend fun isNewUser(): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return if (user == null)
            true
        else
            userRepository.getUser(user.uid) == null
    }

    fun delete(activity: Activity): Task<Void> {
        return AuthUI.getInstance()
            .delete(activity)
    }

    fun logout(context: Context): Task<Void> {
        return AuthUI.getInstance()
            .signOut(context)
    }

    fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    /**
     * Sets all the information passed by the userBuilder in Firebase Firestore
     *
     * @param userBuilder a builder from which a User can be built
     */
    fun setUserProfile(userBuilder: User.Builder) {
        val database = Firebase.firestore
        val uid = getUserId()
        if (uid != null) {
            val birthday = userBuilder.birthday
            val date = Date.getDate(birthday)
            var age = MAJORITY_AGE
            if (date != null) {
                age = date.getAge()
            }
            val minAge =
                if (age >= MAJORITY_AGE + DEFAULT_RANGE)
                    age - DEFAULT_RANGE
                else MAJORITY_AGE
            val maxAge = age + DEFAULT_RANGE

            val newUser = userBuilder.setRadius(DEFAULT_RADIUS)
                .setAgeRange(listOf(minAge, maxAge))
                .build()

            database.collection(USER_COLLECTION).document(uid).set(newUser)
                .addOnSuccessListener {
                    Log.d(TAG, "User \"$uid\" was set in firestore")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error setting the user's profile", e)
                }
        }
    }

    /**
     * Set the e-mail of the user
     *
     * @param email the new e-mail
     *
     * @return a Task completes upon e-mail update
     */
    fun setEmail(email: String): Task<Void>? {
        return FirebaseAuth.getInstance().currentUser?.updateEmail(email)
    }

    /**
     * Get the e-mail of the user
     *
     * @return the e-mail
     */
    fun getEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }
}
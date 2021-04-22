package ch.epfl.sdp.blindly.user

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.profile_setup.ProfileHouseRules
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserHelper {
    companion object {
        const val RC_SIGN_IN = 123
        private const val TAG = "UserHelper"
        private const val USER_COLLECTION: String = "usersMeta"
        private const val DEFAULT_RADIUS = 80
    }

    fun getSignInIntent(): Intent {
        // Optionnaly get phone number to set default in login form
        val phoneProvider = AuthUI.IdpConfig.PhoneBuilder()
        /*
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED ){
            var tMgr: TelephonyManager = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            phoneProvider.setDefaultNumber(tMgr.line1Number)
        }*/
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().setRequireName(false).build(),
                phoneProvider.build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build()
        )


        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                //.setLogo(R.drawable.my_great_logo) // Set logo drawable
                .setTheme(R.style.Theme_Blindly) // Set theme
                /*.setTosAndPrivacyPolicyUrls(
        "https://example.com/terms.html",
        "https://example.com/privacy.html")*/
                .build()

    }

    fun handleAuthResult(activity: Activity, resultCode: Int, data: Intent?): Intent? {
        val response = IdpResponse.fromResultIntent(data)

        if (resultCode == Activity.RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            if (isNewUser(user!!)) {
                return Intent(activity, ProfileHouseRules::class.java)
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
     * @param user: the user to test the condition on
     * @return true if the user just created an account, false otherwise
     */
    private fun isNewUser(user: FirebaseUser) : Boolean {
        val metadata = user.metadata
        return metadata?.creationTimestamp == metadata?.lastSignInTimestamp
    }

    fun delete(activity: Activity, onComplete: OnCompleteListener<Void>) {
        AuthUI.getInstance()
                .delete(activity)
                .addOnCompleteListener(onComplete)
    }

    fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    //TODO refactor this and move to UserRepository
    /**
     * Sets all the information passed by the userBuilder in Firebase Firestore
     *
     * @param userBuilder: a builder from which a User can be build
     */
    fun setUserProfile(userBuilder:User.Builder) {
        val database = Firebase.firestore
        val uid = getUserId()
        if (uid != null) {
            val newUser = userBuilder.setRadius(DEFAULT_RADIUS)
                .setMatches(listOf())
                .setDescription("")
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

    fun setEmail(email: String): Task<Void>? {
        return FirebaseAuth.getInstance().currentUser?.updateEmail(email)
    }

    fun getEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }

    fun updatePassword(password: String) {
        val user = FirebaseAuth.getInstance().currentUser

        user!!.updatePassword(password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(Companion.TAG, "User password updated.")
                    } else {
                        Log.d(Companion.TAG, "Error: Could not update password.")
                    }
                }
    }
}
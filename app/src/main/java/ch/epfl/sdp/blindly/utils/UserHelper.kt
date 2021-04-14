package ch.epfl.sdp.blindly.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import ch.epfl.sdp.blindly.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Module to be installed in Activities
@Module
@InstallIn(SingletonComponent::class)
class UserHelperModule {
    @Provides
    fun provideUserHelper(): UserHelper = UserHelper()
}

class UserHelper {
    companion object {
        const val RC_SIGN_IN = 123
        private const val DEFAULT_RADIUS = 80
        private const val TAG = "User"
        private const val USER_PATH: String = "usersMeta"
        val userFields = arrayListOf(
                "username",
                "location",
                "birthday",
                "genre",
                "sexual_orientations",
                "show_me",
                "passions",
                "radius")

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
                .build();

    }

    fun signOut(activity: Activity, onComplete: OnCompleteListener<Void>) {
        AuthUI.getInstance()
                .signOut(activity)
                .addOnCompleteListener(onComplete);
    }

    fun delete(activity: Activity, onComplete: OnCompleteListener<Void>) {
        AuthUI.getInstance()
                .delete(activity)
                .addOnCompleteListener(onComplete)
    }

    fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun getEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }

    fun setEmail(email: String): Task<Void>? {
        return FirebaseAuth.getInstance().currentUser?.updateEmail(email)
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

    private fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun handleAuthResult(activity: Activity, resultCode: Int, data: Intent?): Boolean {
        val response = IdpResponse.fromResultIntent(data)

        if (resultCode == Activity.RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            return true;
            // ...
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
                ).show();
            }
            return false;
        }
    }

    //Set User's information in firestore after user entered his information in set_profile
    fun setUserProfile(name: String, location: String, birthday: String, genre: String,
                       sexualOrientations: List<String>, showMe: String,
                       passions: List<String>) {

        val database = Firebase.firestore

        if (getUserId() != null) {
            val information = arrayListOf(name,
                location,
                birthday,
                genre,
                sexualOrientations,
                showMe,
                passions,
                DEFAULT_RADIUS)
            val newUser = userFields.zip(information).toMap()

            database.collection(USER_PATH).add(newUser)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "User's profile was set: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error setting the user's profile", e)
                    }
        }
    }

    /**
     * field: the field of the value to change inside the database
     * newValue: the new value to set for the user
     */
    fun <T> updateProfile(field: String, newValue: T) {
        if(!userFields.contains(field))
            throw IllegalArgumentException("Path must be one of $userFields")
        if(newValue !is String || newValue !is ArrayList<*>)
            throw IllegalArgumentException("Expected String or ArrayList<String>")
        val database = Firebase.firestore
        val user = getUserId()
        if (user != null)
            database.collection(USER_PATH)
                    .document(user)
                    .update(field, newValue)
    }

}
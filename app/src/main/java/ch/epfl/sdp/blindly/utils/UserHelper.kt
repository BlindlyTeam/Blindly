package ch.epfl.sdp.blindly.utils

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import ch.epfl.sdp.blindly.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
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
        public const val RC_SIGN_IN = 123
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
                AuthUI.IdpConfig.GoogleBuilder().build()
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

    public fun signOut(activity: Activity, onComplete: OnCompleteListener<Void>) {
        AuthUI.getInstance()
                .signOut(activity)
                .addOnCompleteListener(onComplete);
    }

    public fun delete(activity: Activity, onComplete: OnCompleteListener<Void>) {
        AuthUI.getInstance()
                .delete(activity)
                .addOnCompleteListener(onComplete)
    }

    public fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun getEmail(): String? {
        return FirebaseAuth.getInstance()?.currentUser?.email
    }

    fun setEmail(email: String): Task<Void> {
        return FirebaseAuth.getInstance().currentUser.updateEmail(email)
    }

    private fun getUserId(): String? {
        return FirebaseAuth.getInstance()?.currentUser?.uid
    }

    private fun getMeta() {
        val db = Firebase.firestore
        if (getUserId() != null) {
            db.collection("usersMeta").document(getUserId()!!)
                    .get()
                    .addOnSuccessListener { document ->
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents.", exception)
                    }
        }
    }

    public fun handleAuthResult(activity: Activity, resultCode: Int, data: Intent?): Boolean {
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


}
package ch.epfl.sdp.blindly.utils

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import ch.epfl.sdp.blindly.R
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UserHelper {

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().setRequireName(false).build(),
        //AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    public fun startSignInActivity(activity: Activity) {
        startActivityForResult(
            activity,
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                //.setLogo(R.drawable.my_great_logo) // Set logo drawable
                .setTheme(R.style.Theme_Blindly) // Set theme
                /*.setTosAndPrivacyPolicyUrls(
                "https://example.com/terms.html",
                "https://example.com/privacy.html")*/
                .build(),
            UserHelper.RC_SIGN_IN, null
        )
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

    private fun getUserId(): String {
        return FirebaseAuth.getInstance().currentUser.uid
    }

    private fun getMeta() {
        val db = Firebase.firestore
        db.collection("usersMeta").document(getUserId())
            .get()
            .addOnSuccessListener { document ->
                    Log.d(TAG, "${document.id} => ${document.data}")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    companion object {

        private const val RC_SIGN_IN = 123
    }
}
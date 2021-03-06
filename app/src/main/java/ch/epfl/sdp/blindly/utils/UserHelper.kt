package ch.epfl.sdp.blindly.utils

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import ch.epfl.sdp.blindly.R
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UserHelper {

    companion object {

        public fun startSignInActivity(activity: Activity) {
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

        public fun isLoggedIn(): Boolean {
            return FirebaseAuth.getInstance().currentUser != null
        }

        public fun getEmail(): String {
            return FirebaseAuth.getInstance().currentUser.email
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


        public const val RC_SIGN_IN = 123
    }
}
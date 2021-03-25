package ch.epfl.sdp.blindly

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class User {

    companion object {
        private const val TAG = "User"
        private const val USER_PATH: String = "users"

        //Set User's information after set_profile
        fun setUserProfile(name: String, location: String, birthday: String, genre: String,
                           sexualOrientations: List<String>, showMe: String,
                           passions: List<String>) {

            val database = Firebase.database
            val user = Firebase.auth.currentUser

            if (user != null) {
                updateName(name)
                val newUser = mapOf("name" to name,
                        "email" to user.email,
                        "location" to location,
                        "birthday" to birthday,
                        "genre" to genre,
                        "sexual_orientation" to sexualOrientations,
                        "show_me" to showMe,
                        "passions" to passions)

                database.reference.child(USER_PATH).child(user.uid).setValue(newUser)
            }
        }

        fun updateName(name: String) {
            /*val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }

        user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                }

         */
        }

        fun updateEmail(email: String) {
            val user = Firebase.auth.currentUser

            user!!.updateEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User email address updated.")
                        }
                    }
        }

        fun updatePassword(password: String) {
            val user = Firebase.auth.currentUser

            user!!.updatePassword(password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User password updated.")
                        }
                    }
        }

        /**
         * path: the path of the value to change inside the database
         * newValue: should either be a String or a List<String>
         */
        fun <T> updateProfile(path: String, newValue: T) {
            val database = Firebase.database
            val user = Firebase.auth.currentUser
            if (newValue !is String || newValue !is List<*>) {
                throw IllegalArgumentException("newValue must be a String or a List of Strings")
            }

            if (user != null)
                database.reference.child(USER_PATH)
                        .child(user.uid)
                        .child(path)
                        .setValue(newValue)
        }
    }
}
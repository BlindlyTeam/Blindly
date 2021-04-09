package ch.epfl.sdp.blindly.user

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField

data class User(val uid:String? = null,
                val username: String? = null,
                val location: String? = null,
                val birthday: String? = null,
                val genre: String? = null,
                val sexual_orientations: List<String>,
                val show_me: String? = null,
                val passions: List<String>,
                val radius: Int? = null,
                val matches: List<User>,
                val description: String? = null) {

    companion object {
        fun DocumentSnapshot.toUser(): User? {
            try {
                val uid = getString("uid")!!
                val username = getString("username")!!
                val location = getString("location")!!
                val birthday = getString("birthday")!!
                val genre = getString("genre")!!
                val sexual_orientations = getField<List<String>>("sexual_orientations")!!
                val show_me = getString("show_me")!!
                val passions = getField<List<String>>("passions")!!
                val radius = getField<Int>("radius")!!
                val matches = getField<List<User>>("description")!!
                val description = getString("description")!!
                return User(uid, username, location, birthday, genre, sexual_orientations, show_me, passions, radius, matches, description)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                return null
            }
        }
        private const val TAG = "User"
    }
}
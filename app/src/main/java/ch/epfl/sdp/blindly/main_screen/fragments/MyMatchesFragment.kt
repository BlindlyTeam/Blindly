package ch.epfl.sdp.blindly.main_screen.fragments

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "MyMatches"


@AndroidEntryPoint
class MyMatchesFragment {

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository


    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun getMyMatches(): List<String> {
        val userId = userHelper.getUserId()!!
        val currentUser = userRepository.getUser(userId)
        var myMatches: List<String> = listOf()


        val query: Query? = currentUser!!.matches?.let {
            userRepository.getCollectionReference()
                .whereArrayContainsAny("matches", it)
        }
        try {
            val userIds = query?.get()?.await()
            if (userIds != null) {
                for (userId in userIds) {
                    myMatches += userId.toString()
                }
            }
        } catch (exception: Exception) {
            Log.w(TAG, "Error getting users : ", exception)
        }

        return myMatches
    }
}
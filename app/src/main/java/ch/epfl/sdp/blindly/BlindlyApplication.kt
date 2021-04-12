package ch.epfl.sdp.blindly

import android.app.Application
import ch.epfl.sdp.blindly.user.UserCache
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

// Custom app to let hilt hook into
@HiltAndroidApp
class BlindlyApplication : Application() {

}

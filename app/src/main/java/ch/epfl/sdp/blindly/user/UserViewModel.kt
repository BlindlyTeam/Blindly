package ch.epfl.sdp.blindly.user

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import ch.epfl.sdp.blindly.settings.SettingsShowMe
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper.Companion.EXTRA_UID
import ch.epfl.sdp.blindly.user.UserRepository
import com.facebook.internal.LockOnGetVariable
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

/**
 * The viewModel associated with ProfilePage, it holds the livedata for a given user
 *
 */
@RequiresApi(Build.VERSION_CODES.N)
class UserViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    userRepository: UserRepository
) : ViewModel() {

    companion object {
        private const val TAG = "UserViewModel"
    }

    private var userRepo: UserRepository = userRepository

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val userId: String =
        savedStateHandle[EXTRA_UID] ?: throw IllegalArgumentException("Missing user id")

    init {
        userUpdate()
    }

    fun userUpdate() {
        viewModelScope.launch {
            Log.d(TAG, "Getting user")
            _user.value = userRepo.getUser(userId)
        }
    }

    fun <T> updateField(field: String, newValue: T) {
        viewModelScope.launch {
            Log.d(TAG, "Launching update profile")
            userRepo.updateProfile(userId, field, newValue)
        }
    }
}
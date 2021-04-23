package ch.epfl.sdp.blindly.main_screen.profile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

/**
 * The viewModel associated with ProfilePage, it holds the livedata for a given user
 *
 */
@RequiresApi(Build.VERSION_CODES.N)
class ProfilePageViewModel@AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    userRepository: UserRepository): ViewModel() {

    companion object {
        private const val TAG = "ProfilePageViewModel"
    }

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val userId : String = savedStateHandle["uid"] ?:
    throw IllegalArgumentException("Missing user id")

    init {
        viewModelScope.launch {
            _user.value = userRepository.getUser(userId)
        }
    }
}
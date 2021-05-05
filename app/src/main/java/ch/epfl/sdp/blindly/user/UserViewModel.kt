package ch.epfl.sdp.blindly.user

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
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
        private const val TAG = "ProfilePageViewModel"
        const val EXTRA_UID = "uid"
    }

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val userId: String =
        savedStateHandle[EXTRA_UID] ?: throw IllegalArgumentException("Missing user id")

    init {
        viewModelScope.launch {
            _user.value = userRepository.getUser(userId)
        }
    }
}
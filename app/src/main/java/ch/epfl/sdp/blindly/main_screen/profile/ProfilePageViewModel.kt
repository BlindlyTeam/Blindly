package ch.epfl.sdp.blindly.main_screen.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The viewModel associated with ProfilePage, it holds the livedata for a given user
 */
@RequiresApi(Build.VERSION_CODES.N)
class ProfilePageViewModel@AssistedInject constructor(
    savedStateHandle: SavedStateHandle,
    userRepository: UserRepository): ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val userId : String = savedStateHandle["uid"] ?:
    throw IllegalArgumentException("missing user id")

    init {
        viewModelScope.launch {
            _user.value = userRepository.getUser(userId)
        }
    }
}
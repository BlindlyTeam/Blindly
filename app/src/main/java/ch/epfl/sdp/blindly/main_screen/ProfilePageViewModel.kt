package ch.epfl.sdp.blindly.main_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@HiltViewModel
class ProfilePageViewModel@Inject constructor(
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
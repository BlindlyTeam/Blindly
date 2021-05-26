package ch.epfl.sdp.blindly.viewmodel

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import ch.epfl.sdp.blindly.audio.AudioStorage
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.user.LIKES
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper.Companion.EXTRA_UID
import com.google.firebase.storage.FirebaseStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

/**
 * The viewModel associated with ProfilePage, it holds the livedata for a given user
 */
class UserViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    val userRepository: UserRepository,
    val storage: FirebaseStorage
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val userId: String =
        savedStateHandle[EXTRA_UID] ?: throw IllegalArgumentException("Missing user id")

    init {
        userUpdate()
    }

    /**
     * Set the _user MutableLiveData to observe the userRepo
     *
     */
    fun userUpdate() {
        viewModelScope.launch {
            _user.value = userRepository.getUser(userId)
        }
    }

    /**
     * Update the given field with the new value
     *
     * @param T either a String, an Int, or a List<*>
     * @param field the filed to be updated
     * @param newValue the new value of the field to be updated
     */
    fun <T> updateField(field: String, newValue: T) {
        viewModelScope.launch {
            userRepository.updateProfile(userId, field, newValue)
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            AudioStorage(storage).removeAudio(userId)
            userRepository.deleteUser(userId) //set the flag to 1
        }
    }

    companion object {
        /**
         * Instantiate the UserViewModel with the given parameters
         *
         * @param uid the uid of the user to observe
         * @param assistedFactory the pre injected assistedFactory in the activity that
         *     requires the viewModel
         * @param owner the activity that needs the viewModel
         * @param viewModelStoreOwner the activity that needs the viewModel
         * @return an instance of UserViewModel
         */
        fun instantiateViewModel(
            uid: String?,
            assistedFactory: ViewModelAssistedFactory,
            owner: SavedStateRegistryOwner,
            viewModelStoreOwner: ViewModelStoreOwner
        ): UserViewModel {
            val bundle = Bundle()
            bundle.putString(EXTRA_UID, uid)
            val viewModelFactory = assistedFactory.create(owner, bundle)
            return ViewModelProvider(
                viewModelStoreOwner,
                viewModelFactory
            )[UserViewModel::class.java]
        }
    }
}
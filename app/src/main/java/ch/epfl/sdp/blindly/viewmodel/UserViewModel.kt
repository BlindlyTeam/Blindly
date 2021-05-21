package ch.epfl.sdp.blindly.viewmodel

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.user.UserHelper.Companion.EXTRA_UID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

/**
 * The viewModel associated with ProfilePage, it holds the livedata for a given user
 */
class UserViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    userRepository: UserRepository
) : ViewModel() {

    private var userRepo: UserRepository = userRepository

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
            _user.value = userRepo.getUser(userId)
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
            userRepo.updateProfile(userId, field, newValue)
        }
    }

    companion object {
        private const val TAG = "UserViewModel"

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
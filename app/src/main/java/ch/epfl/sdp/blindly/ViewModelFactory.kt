package ch.epfl.sdp.blindly

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ch.epfl.sdp.blindly.user.UserViewModel
import ch.epfl.sdp.blindly.user.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * A factory that helps to create ViewModels that uses the UserRepository
 *
 * @property userRepository the UserRepository is automatically injected
 * @constructor
 * Arguments that need to be passed to the constructor:
 *
 * @param owner the Activity associated with the ViewModel to create
 * @param bundle additionnal arguments that the ViewModel may need to be instantiated,
 *                which are automatically passed in the SavedStateHandle
 */
class ViewModelFactory @AssistedInject constructor(
    private val userRepository: UserRepository,
    @Assisted owner: SavedStateRegistryOwner,
    @Assisted bundle: Bundle
) : AbstractSavedStateViewModelFactory(owner, bundle) {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun <T : ViewModel?> create(
        key: String, modelClass: Class<T>, handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(handle, userRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

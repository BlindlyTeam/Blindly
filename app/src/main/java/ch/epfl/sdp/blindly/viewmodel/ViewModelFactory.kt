package ch.epfl.sdp.blindly.viewmodel

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ch.epfl.sdp.blindly.audio.FirebaseRecordings
import ch.epfl.sdp.blindly.database.UserRepository
import com.google.firebase.storage.FirebaseStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * A factory that helps to create ViewModels that uses the UserRepository
 *
 * @property userRepository the UserRepository is automatically injected
 * @property storage the storage is automatically injected
 * @constructor
 * Arguments that need to be passed to the constructor:
 *
 * @param owner the Activity associated with the ViewModel to create
 * @param bundle additionnal arguments that the ViewModel may need to be instantiated,
 *                which are automatically passed in the SavedStateHandle
 */
class ViewModelFactory @AssistedInject constructor(
    private val userRepository: UserRepository,
    private val recordings: FirebaseRecordings,
    @Assisted owner: SavedStateRegistryOwner,
    @Assisted bundle: Bundle
) : AbstractSavedStateViewModelFactory(owner, bundle) {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun <T : ViewModel?> create(
        key: String, modelClass: Class<T>, handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(handle, userRepository, recordings) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

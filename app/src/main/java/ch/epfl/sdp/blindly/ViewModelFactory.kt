package ch.epfl.sdp.blindly

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ch.epfl.sdp.blindly.main_screen.profile.ProfilePageViewModel
import ch.epfl.sdp.blindly.user.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class ViewModelFactory @AssistedInject constructor(
    private val userRepository: UserRepository,
    @Assisted owner: SavedStateRegistryOwner,
    @Assisted bundle: Bundle
) : AbstractSavedStateViewModelFactory(owner, bundle) {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle
    ): T {
        if(modelClass.isAssignableFrom(ProfilePageViewModel::class.java)) {
            return ProfilePageViewModel(handle, userRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
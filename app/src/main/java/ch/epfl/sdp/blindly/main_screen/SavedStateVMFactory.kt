package ch.epfl.sdp.blindly.main_screen

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ch.epfl.sdp.blindly.user.UserRepository


class SavedStateVMFactory(
    private val userRepository: UserRepository,
    owner: SavedStateRegistryOwner, defaultArgs: Bundle?
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

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
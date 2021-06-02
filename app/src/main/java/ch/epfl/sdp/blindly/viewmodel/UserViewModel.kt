package ch.epfl.sdp.blindly.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Chronometer
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import androidx.test.core.app.ApplicationProvider
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.audio.AudioPlayerFragment
import ch.epfl.sdp.blindly.audio.BlindlyMediaPlayer
import ch.epfl.sdp.blindly.audio.FirebaseRecordings
import ch.epfl.sdp.blindly.audio.PRESENTATION_AUDIO_NAME
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper.Companion.EXTRA_UID
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

/**
 * The viewModel associated with ProfilePage, it holds the livedata for a given user
 */
class UserViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val recordings: FirebaseRecordings,
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val userId: String =
        savedStateHandle[EXTRA_UID] ?: throw IllegalArgumentException("Missing user id")
    private lateinit var filePath: String

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

    /**
     * Delete the user associated with this view model
     *
     */
    fun deleteUser() {
        viewModelScope.launch {
            val recordingPath = FirebaseRecordings.getPresentationAudionName(userId)
            recordings.deleteFile(recordingPath, object :
                FirebaseRecordings.RecordingOperationCallback() {
                override fun onSuccess() {
                    Log.d(TAG, "Successfully removed the audio.")
                }

                override fun onError() {
                    Log.e(TAG, "An error occurred, the audio could not be deleted.")
                }
            })
            userRepository.deleteUser(userId) //set the flag to 1
        }
    }

    /**
     * Prepare a media player for the user associated with this view model.
     * It retrieves the audio path either from the app directory if it exists or from
     * Firebase storage in which case the retrieved audio file will also be saved in the
     * directory
     *
     * @param blindlyMediaPlayer
     * @param playBar
     * @param remainingTimer
     * @param playTimer
     * @param playPauseButton
     * @param context
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun prepareUserMediaPlayer(
        blindlyMediaPlayer: BlindlyMediaPlayer,
        playBar: SeekBar,
        remainingTimer: Chronometer,
        playTimer: Chronometer,
        playPauseButton: FloatingActionButton,
        context: Context
    ) {
        try {
            filePath = "${context.filesDir?.absolutePath}/$PRESENTATION_AUDIO_NAME"
            // This throws an exception if the file doesn't existin the app directory
            blindlyMediaPlayer.createMediaPlayer(filePath)
            blindlyMediaPlayer.setupMediaPlayer(
                playBar,
                playTimer,
                remainingTimer,
                playPauseButton,
                filePath
            )
            Log.d(TAG, "Setup from File")
        } catch (e: Exception) {
            val audioFile = File.createTempFile("Profile_Audio", "amr")
            recordings.getFile(
                _user.value?.recordingPath!!,
                audioFile,
                object : FirebaseRecordings.RecordingOperationCallback() {
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onSuccess() {
                        filePath = Uri.fromFile(audioFile).path!!
                        // Save the audioRecord in the app directory to be available offline
                        val newFile = File("${context.filesDir.absolutePath}/$PRESENTATION_AUDIO_NAME")
                        audioFile.copyTo(
                            newFile,
                            overwrite = true
                        )
                        blindlyMediaPlayer.setupMediaPlayer(
                            playBar,
                            playTimer,
                            remainingTimer,
                            playPauseButton,
                            filePath
                        )
                        Log.d(TAG, "Setup from Storage")
                    }

                    override fun onError() {
                        Log.e("Blindly", "Can't play a file")
                    }
                }
            )
        }
    }

    fun resetUserMediaPlayer(
        blindlyMediaPlayer: BlindlyMediaPlayer,
        playBar: SeekBar,
        remainingTimer: Chronometer,
        playTimer: Chronometer,
        playPauseButton: FloatingActionButton,
    ) {
        blindlyMediaPlayer.resetRecordPlayer(
            filePath,
            playTimer,
            remainingTimer,
            playPauseButton,
            playBar
        )
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
package ch.epfl.sdp.blindly.audio

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

open class AudioStorage @Inject constructor(val storage: FirebaseStorage) {

    open fun removeAudio(uid: String) {
        val recordingPath = "Recordings/$uid-$PRESENTATION_AUDIO_NAME"
        val storageRef = storage.reference.child(recordingPath)

        storageRef.delete()
            .addOnSuccessListener {
                Log.d(TAG, "AudioRecord was successfully deleted from storage")
            }.addOnFailureListener {
                Log.e(TAG, "An error occurred while trying to delete the AudioRecord")
            }
    }

    companion object {
        private const val TAG = "AudioStorage"
    }
}
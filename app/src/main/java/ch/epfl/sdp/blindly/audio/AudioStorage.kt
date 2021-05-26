package ch.epfl.sdp.blindly.audio

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
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

    open fun addAudio(recordingPath: String, newFile: File): UploadTask {
        val storageRef = storage.reference.child(recordingPath)
        return storageRef.putFile(Uri.fromFile(newFile))
    }

    companion object {
        private const val TAG = "AudioStorage"
    }
}
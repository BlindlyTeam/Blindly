package ch.epfl.sdp.blindly.audio

import android.net.Uri
import ch.epfl.sdp.blindly.audio.FirebaseRecordings.RecordingOperationCallback
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class FirebaseRecordingsImpl(private val storage: FirebaseStorage) : FirebaseRecordings {
    private fun getPathRef(recordingPath: String) = storage.reference.child(recordingPath)

    /**
     * Put [file] with key [recordingPath] in storage
     *
     * @param recordingPath the key
     * @param file the file to store
     * @param callback callback fired on completion
     */
    override fun putFile(
        recordingPath: String,
        file: File,
        callback: RecordingOperationCallback
    ) {
        getPathRef(recordingPath).putFile(Uri.fromFile(file)).addOnCompleteListener {
            if (it.isSuccessful)
                callback.onSuccess()
            else
                callback.onError()
        }.addOnFailureListener {
            callback.onError()
        }.addOnCanceledListener { callback.onError() }
    }

    /**
     * Retrieve a file with key [recordingPath] to [file]
     *
     * @param recordingPath the key of the file
     * @param file the destination file
     * @param callback callback fired on completion
     */
    override fun getFile(
        recordingPath: String,
        file: File,
        callback: RecordingOperationCallback
    ) {
        getPathRef(recordingPath).getFile(file).addOnCompleteListener {
            if (it.isSuccessful)
                callback.onSuccess()
            else
                callback.onError()
        }.addOnFailureListener {
            callback.onError()
        }.addOnCanceledListener { callback.onError() }
    }

    override fun deleteFile(
        recordingPath: String,
        callback: RecordingOperationCallback
    ) {
        getPathRef(recordingPath).delete().addOnCompleteListener {
            if (it.isSuccessful)
                callback.onSuccess()
            else
                callback.onError()
        }.addOnFailureListener {
            callback.onError()
        }.addOnCanceledListener { callback.onError() }
    }

}
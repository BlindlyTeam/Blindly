package ch.epfl.sdp.blindly.audio

import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import java.io.File



class FirebaseRecordings(private val storage: FirebaseStorage) : Recordings {
    private fun getPathRef(recordingPath: String) = storage.reference.child(recordingPath)

    /**
     * Put [file] with key [recordingPath] in storage
     *
     * @param recordingPath the key
     * @param file the file to store
     * @param callback callback fired on completion
     */
    override fun putFile(recordingPath: String, file: File, callback: Recordings.RecordingOperationCallback) {
        getPathRef(recordingPath).putFile(Uri.fromFile(file)).addOnCompleteListener {
            callback.onSuccess()
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
    override fun getFile(recordingPath: String, file: File, callback: Recordings.RecordingOperationCallback) {
        getPathRef(recordingPath).getFile(file).addOnCompleteListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onError()
        }.addOnCanceledListener { callback.onError() }
    }

}
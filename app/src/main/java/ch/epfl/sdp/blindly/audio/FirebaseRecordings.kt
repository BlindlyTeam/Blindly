package ch.epfl.sdp.blindly.audio

import java.io.File

interface FirebaseRecordings {
    abstract class RecordingOperationCallback() {
        abstract fun onSuccess();
        abstract fun onError()
    }

    /**
     * Put [file] with key [recordingPath] in storage
     *
     * @param recordingPath the key
     * @param file the file to store
     * @param callback callback fired on completion
     */
    fun putFile(recordingPath: String, file: File, callback: RecordingOperationCallback)

    /**
     * Retrieve a file with key [recordingPath] to [file]
     *
     * @param recordingPath the key of the file
     * @param file the destination file
     * @param callback callback fired on completion
     */
    fun getFile(recordingPath: String, file: File, callback: RecordingOperationCallback)

    fun deleteFile(recordingPath: String, callback: RecordingOperationCallback)

    companion object {
        private const val PRESENTATION_AUDIO_NAME = "PresentationAudio.amr"
        fun getPresentationAudionName(userId: String) =
            "Recordings/$userId-${PRESENTATION_AUDIO_NAME}"
    }
}
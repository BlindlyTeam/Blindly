package ch.epfl.sdp.blindly.recording

/**
 * Class that represents an audio record.
 *
 * @property name the name of the file
 * @property durationText the duration as a string
 * @property filePath the file path to the corresponding file
 * @property isExpanded true if the audio is currently expanded in view (see [AudioLibraryAdapter])
 */
data class AudioRecord(var name: String,
                       val durationText: String,
                       val filePath: String,
                       var isExpanded: Boolean)
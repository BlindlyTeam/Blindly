package ch.epfl.sdp.blindly.recording

data class AudioRecord(var name: String,
                       val duration: Int,
                       val filePath: String,
                       var isExpanded: Boolean) {
}
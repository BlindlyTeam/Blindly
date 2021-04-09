package ch.epfl.sdp.blindly.recording

data class AudioRecord(var name: String,
                       val durationText: String,
                       val filePath: String,
                       var isExpanded: Boolean)
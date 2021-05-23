package ch.epfl.sdp.blindly.main_screen.my_matches

/**
 * Data class for the shown matches
 *
 * @property name Name of user's match
 * @property uid UID of user's match
 * @property isExpanded State of layout in Recycler View
 */
data class MyMatch(
    var name: String,
    var uid: String,
    var isExpanded: Boolean
)
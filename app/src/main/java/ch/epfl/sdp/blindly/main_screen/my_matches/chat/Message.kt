package ch.epfl.sdp.blindly.main_screen.my_matches.chat

/**
 * A message to send to another user using [DatatbaseHelper]
 *
 * @param T The type to be sent inside the message
 */
class Message<T> {

    /**
     * Empty constructor is needed for the Realtime Database.
     */
    constructor()

    // timestamp is to achieve correct ordering of the messages and ensure fetching is in
    // same ordering the next time users open the chat
    var timestamp: Long? = null
    var messageText: T? = null
    var currentUserId: String? = null

    constructor(
        _messageText: T,
        _currentUserId: String,
    ) {
        messageText = _messageText
        currentUserId = _currentUserId
        timestamp = System.currentTimeMillis()
    }
}
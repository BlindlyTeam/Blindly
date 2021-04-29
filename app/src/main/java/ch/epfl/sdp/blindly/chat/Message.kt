package ch.epfl.sdp.blindly.chat

class Message {

    /**
     * Empty constructor is needed for the Realtime Database.
     */
    constructor()

    // timestamp is to achieve correct ordering of the messages and ensure fetching is in
    // same ordering the next time users open the chat
    var timestamp: Long? = null
    var messageText: String? = null
    var currentUserId: String? = null

    constructor(
        _messageText: String,
        _currentUserId: String,
    ) {
        messageText = _messageText
        currentUserId = _currentUserId
        timestamp = System.currentTimeMillis()
    }


}
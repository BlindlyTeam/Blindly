package ch.epfl.sdp.blindly.chat

class Message {
    constructor()

    var timestamp: Long = System.currentTimeMillis()
    var messageText: String? = null
    var currentUserId: String? = null
    //var matchUserId: String? = null
    //var isCurrentUser: Boolean = false

    constructor(
        _messageText: String,
        _currentUserId: String,
        //_matchUserId: String,
        //_isCurrentUser: Boolean
    ) {
        messageText = _messageText
        currentUserId = _currentUserId
        //matchUserId = _matchUserId
        //isCurrentUser = _isCurrentUser
    }


}
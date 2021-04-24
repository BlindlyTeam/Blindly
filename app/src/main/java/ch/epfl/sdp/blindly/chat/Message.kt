package ch.epfl.sdp.blindly.chat

class Message {
    constructor()

    var timestamp: Long = System.currentTimeMillis()
    var messageText: String? = null
    var senderUid: String? = null
    var receiverUid: String? = null
    var isCurrentUser: Boolean = false

    constructor(
        _messageText: String,
        _senderUid: String,
        _receiverUid: String,
        _isCurrentUser: Boolean
    ) {
        messageText = _messageText
        senderUid = _senderUid
        receiverUid = _receiverUid
        isCurrentUser = _isCurrentUser
    }


}
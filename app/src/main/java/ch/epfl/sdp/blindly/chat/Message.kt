package ch.epfl.sdp.blindly.chat

class Message {
    constructor()

    var timestamp: Long = System.currentTimeMillis()
    var messageText: String? = null
    var senderUid: String? = null
    var receiverUid: String? = null

    constructor(_messageText: String, _senderUid: String, _receiverUid: String) {

        messageText = _messageText
        senderUid = _senderUid
        receiverUid = _receiverUid
    }


}
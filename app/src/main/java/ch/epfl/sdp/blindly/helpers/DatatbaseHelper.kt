package ch.epfl.sdp.blindly.helpers

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class DatatbaseHelper {

    private val databaseInstance = FirebaseDatabase.getInstance("https://blindly-24119-default-rtdb.europe-west1.firebasedatabase.app/")

    public enum class MessageTypes {
        CHAT, LOCATION
    }

    fun <T> getUserMessages(messageType: MessageTypes, userId: String, otherUserId: String): BlindlyLiveDatabase<T> {
        //this is done to get the same chatId from both sides
        val chatId = if (userId < otherUserId) {
            "($userId, $otherUserId)"
        } else {
            "($otherUserId, $userId)"
        }

        if (messageType == MessageTypes.CHAT)
            return BlindlyLiveDatabase<T>(databaseInstance.getReference("messages").child(chatId), userId)
        else
            TODO("unimplemented")
    }

    class BlindlyLiveDatabase<T>(dr: DatabaseReference, userId: String) {
        // Init at one as most of the time we don't register more than one listener per db
        private val eventListeners: MutableList<EventListener<T>> = ArrayList(1)
        private val dr = dr
        private val userId = userId
        init {
            // Add the event listener that dispatches to other listeners
            dr.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue<Message<T>>()
                    if (message != null) {
                        eventListeners.forEach { eventListener ->
                            eventListener.onMessageReceived(message)
                        }
                    } else {
                        Log.w("LiveDatabaseHelper", "Unhandled message received")
                    }
                }

                // we don't allow any changes, removals etc. so these stay only for the compilation
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })
        }

        fun sendMessage(message: T) {
            val newMessage = Message(
                message,
                userId
            )
            dr.child(newMessage.timestamp.toString()).setValue(newMessage)
        }
        fun addListener(listener: EventListener<T>) {
            eventListeners.add(listener)
        }
        fun removeListener(listener: EventListener<T>) {
            eventListeners.remove(listener)
        }
        abstract class EventListener<T> {
            abstract fun onMessageReceived(message: Message<T>)
        }
    }
}
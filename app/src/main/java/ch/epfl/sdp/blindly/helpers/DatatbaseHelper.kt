package ch.epfl.sdp.blindly.helpers

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class DatatbaseHelper {

    private val databaseInstance =
        FirebaseDatabase.getInstance("https://blindly-24119-default-rtdb.europe-west1.firebasedatabase.app/")

    private fun getConversationId(userId: String, otherUserId: String): String {
        //this is done to get the same chatId from both sides
        return if (userId < otherUserId) {
            "($userId, $otherUserId)"
        } else {
            "($otherUserId, $userId)"
        }
    }

    fun getChatLiveDatabase(
        userId: String,
        otherUserId: String
    ): ChatLiveDatabase {
        return ChatLiveDatabase(
            databaseInstance.getReference("messages").child(getConversationId(userId, otherUserId)),
            userId
        )
    }

    fun getLocationLiveDatabase(
        userId: String,
        otherUserId: String
    ): LocationLiveDatabase {
        return LocationLiveDatabase(
            databaseInstance.getReference("locations").child(getConversationId(userId, otherUserId)),
            userId
        )
    }

    class LocationLiveDatabase(dr: DatabaseReference, userId: String) :
        BlindlyLiveDatabase<BlindlyLatLng>(dr, userId) {
        override val ti = object : GenericTypeIndicator<Message<BlindlyLatLng>>() {};

        fun updateLocation(location: BlindlyLatLng) {
            val newMessage = Message(
                location,
                userId
            )
            // We keep only one entry
            dr.child(userId).setValue(newMessage)
        }
    }

    class ChatLiveDatabase(dr: DatabaseReference, userId: String) :
        BlindlyLiveDatabase<String>(dr, userId) {
        override val ti = object : GenericTypeIndicator<Message<String>>() {};

        open fun sendMessage(message: String) {
            val newMessage = Message(
                message,
                userId
            )
            dr.child(newMessage.timestamp.toString()).setValue(newMessage)
        }
    }
    abstract class BlindlyLiveDatabase<T> internal constructor(dr: DatabaseReference, userId: String) {
        // Init at one as most of the time we don't register more than one listener per db
        private val eventListeners: MutableList<EventListener<T>> = ArrayList(1)
        protected val dr = dr
        protected val userId = userId
        // We need to indicate type on each class initialization (=runtime) as
        // the JVM has type-ereasure on runtime
        abstract val ti: GenericTypeIndicator<Message<T>>
        init {
            // Add the event listener that dispatches to other listeners
            dr.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(ti)
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
                    val message = snapshot.getValue(ti)
                    if (message != null) {
                        eventListeners.forEach { eventListener ->
                            eventListener.onMessageUpdated(message)
                        }
                    } else {
                        Log.w("LiveDatabaseHelper", "Unhandled message updated")
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    //TODO("Not yet implemented")
                    Log.e("LiveDatabaseHelper", "Operation cancelled: ", error.toException())
                }


            })
        }

        fun addListener(listener: EventListener<T>) {
            eventListeners.add(listener)
        }
        fun removeListener(listener: EventListener<T>) {
            eventListeners.remove(listener)
        }
        abstract class EventListener<T> {
            abstract fun onMessageReceived(message: Message<T>)
            open fun onMessageUpdated(message: Message<T>) {
                TODO("Not yet implemented")
            }
        }
    }
}
package ch.epfl.sdp.blindly.helpers

import android.util.Log
import com.google.firebase.database.*

/**
 * Class to be injected to provide the firebase live database
 *
 */
class DatatbaseHelper {

    private val databaseInstance =
        FirebaseDatabase.getInstance("https://blindly-24119-default-rtdb.europe-west1.firebasedatabase.app/")

    companion object {
        /**
         * Get the conversation id between two users
         *
         * @param userId the logged-in user
         * @param otherUserId the user to converse with
         * @return the conversation id
         */
        fun getConversationId(userId: String, otherUserId: String): String {
            //this is done to get the same chatId from both sides
            return if (userId < otherUserId) {
                "($userId, $otherUserId)"
            } else {
                "($otherUserId, $userId)"
            }
        }
    }

    /**
     * The live database for the chat
     *
     * @param userId the logged-in user id
     * @param otherUserId the other user's id to send message to
     * @return The live database
     */
    fun getChatLiveDatabase(
        userId: String,
        otherUserId: String
    ): ChatLiveDatabase {
        return ChatLiveDatabase(
            databaseInstance.getReference("messages").child(getConversationId(userId, otherUserId)),
            userId
        )
    }

    /**
     * The live database for the location
     *
     * @param userId the logged-in user id
     * @param otherUserId the other user's id to share the location with
     * @return The live database
     */
    fun getLocationLiveDatabase(
        userId: String,
        otherUserId: String
    ): LocationLiveDatabase {
        return LocationLiveDatabase(
            databaseInstance.getReference("locations").child(getConversationId(userId, otherUserId)),
            userId
        )
    }

    /**
     * The live database to share the location live
     *
     * @constructor
     * Internal constructor to get an instance see [getLocationLiveDatabase]
     *
     * @param dr the firebase database reference
     * @param userId the logged-in user id
     */
    class LocationLiveDatabase internal constructor(dr: DatabaseReference, userId: String) :
        BlindlyLiveDatabase<BlindlyLatLng>(dr, userId) {
        override val typeIndicator = object : GenericTypeIndicator<Message<BlindlyLatLng>>() {}

        fun updateLocation(location: BlindlyLatLng) {
            val newMessage = Message(
                location,
                userId
            )
            // We keep only one entry
            dr.child(userId).setValue(newMessage)
        }
    }

    /**
     * The live database for the chat
     *
     * @constructor
     * Internal constructor to get an instance see [getChatLiveDatabase]
     *
     * @param dr the firebase database reference
     * @param userId the logged-in user id
     */
    class ChatLiveDatabase internal constructor(dr: DatabaseReference, userId: String) :
        BlindlyLiveDatabase<String>(dr, userId) {
        override val typeIndicator = object : GenericTypeIndicator<Message<String>>() {}

        fun sendMessage(message: String) {
            val newMessage = Message(
                message,
                userId
            )
            dr.child(newMessage.timestamp.toString()).setValue(newMessage)
        }
    }

    /**
     * A generic live database using [Message] to communicate to other users
     *
     * @param T The type contained in messages, see [Message]
     * @constructor
     * Internal constructor to create an instance, use one of the public methods to
     * get an instance
     *
     * @param dr the firebase database reference
     * @param userId the logged-in user id
     */
    abstract class BlindlyLiveDatabase<T> internal constructor(protected val dr: DatabaseReference,
                                                               protected val userId: String
    ) {
        // Init at one as most of the time we don't register more than one listener per db
        private val eventListeners: MutableList<EventListener<T>> = ArrayList(1)

        // We need to indicate type on each class initialization (=runtime) as
        // the JVM has type-ereasure on runtime
        abstract val typeIndicator: GenericTypeIndicator<Message<T>>
        init {
            // Add the event listener that dispatches to other listeners
            dr.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(typeIndicator)
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
                    val message = snapshot.getValue(typeIndicator)
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

        /**
         * Add an event listener on this live database
         *
         * @param listener A listener to register
         */
        fun addListener(listener: EventListener<T>) {
            eventListeners.add(listener)
        }

        /**
         * Remove an event listener on this live database
         *
         * @param listener The listener to remove
         */
        @Suppress("unused")
        fun removeListener(listener: EventListener<T>) {
            eventListeners.remove(listener)
        }

        /**
         * The event listener for Blindly live databases
         *
         * @param T the type contained in the messages, it must match [BlindlyLiveDatabase] message type
         */
        abstract class EventListener<T> {
            abstract fun onMessageReceived(message: Message<T>)
            open fun onMessageUpdated(message: Message<T>) {
                TODO("Not yet implemented")
            }
        }
    }
}
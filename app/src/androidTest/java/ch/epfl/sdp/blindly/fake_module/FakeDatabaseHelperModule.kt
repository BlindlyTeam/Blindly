package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.database.DatabaseHelper
import ch.epfl.sdp.blindly.dependency_injection.DatabaseHelperModule
import ch.epfl.sdp.blindly.main_screen.my_matches.chat.Message
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.stubbing.Answer
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseHelperModule::class]
)
open class FakeDatabaseHelperModule {
    private val mockedChatDatabases = HashMap<String, DatabaseHelper.ChatLiveDatabase>(1)
    private val mockedLocationDatabases = HashMap<String, DatabaseHelper.LocationLiveDatabase>(1)

    /**
     * Provide the mocked database helpers
     *
     * @return the mocked database helper
     */
    @Singleton
    @Provides
    open fun provideDatabaseHelper(): DatabaseHelper {
        val db = mock(DatabaseHelper::class.java)
        // Return a mocked location live database
        `when`(db.getLocationLiveDatabase(anyString(), anyString()))
            .thenAnswer(Answer {
                val userId: String = it.getArgument(0, String::class.java)
                val otherUserId: String = it.getArgument(1, String::class.java)

                return@Answer getLocationMockDb(userId, otherUserId)
            })
        // Return a mocked chat live database
        `when`(db.getChatLiveDatabase(anyString(), anyString()))
            .thenAnswer(Answer {
                val userId: String = it.getArgument(0, String::class.java)
                val otherUserId: String = it.getArgument(1, String::class.java)

                return@Answer getChatMockDb(userId, otherUserId)
            })
        return db
    }

    private fun getChatMockDb(
        userId: String,
        otherUserId: String
    ): DatabaseHelper.ChatLiveDatabase {
        return mockedChatDatabases.getOrPut(userId, {
            DatabaseHelper.ChatLiveDatabase(
                getMockedDbRef(otherUserId),
                userId
            )
        })
    }

    private fun getLocationMockDb(
        userId: String,
        otherUserId: String
    ): DatabaseHelper.LocationLiveDatabase {
        return mockedLocationDatabases.getOrPut(userId, {
            DatabaseHelper.LocationLiveDatabase(
                getMockedDbRef(otherUserId),
                userId
            )
        })
    }

    /**
     * Create a new mocked database reference that reply to every message
     * with a message with the same content but from [otherUserId]
     *
     * @param otherUserId The user to send the message to
     *
     * @return the newly created DatabaseReference
     */
    private fun getMockedDbRef(otherUserId: String): DatabaseReference {
        val dbRef = mock(DatabaseReference::class.java, RETURNS_DEEP_STUBS)
        // keep track of inserted values so we can call onChanged/onAdded
        val alreadyDefinedEntries = HashSet<String>()
        val listeners: MutableList<ChildEventListener> = ArrayList(1)
        `when`(dbRef.addChildEventListener(any())).then(Answer<ChildEventListener> { invocationOnMock ->
            val listener: ChildEventListener =
                invocationOnMock.getArgument(0, ChildEventListener::class.java)
            listeners.add(listener)
            // We need to return the added listener for the signature to be correct
            listener
        })
        `when`(dbRef.child(any())).then(Answer<DatabaseReference> { invocationOnMock ->
            val colectionName: String? = invocationOnMock.getArgument(0, String::class.java)
            val childRef = mock(DatabaseReference::class.java)

            // Send the messages
            val dispatchMessage = { message: Message<Any> ->

                // Create a mock snapshot to give to the callback
                val dataSnapshot = mock(DataSnapshot::class.java)
                `when`(dataSnapshot.getValue(ArgumentMatchers.any(GenericTypeIndicator::class.java))).thenReturn(
                    message
                )
                if (alreadyDefinedEntries.contains(colectionName)) {
                    listeners.forEach { listener ->
                        listener.onChildChanged(
                            dataSnapshot,
                            "NOT MOCKED"
                        )
                    }
                } else {
                    listeners.forEach { listener ->
                        listener.onChildAdded(
                            dataSnapshot,
                            "NOT MOCKED"
                        )
                    }
                }
            }
            `when`(childRef.setValue(any())).then { invOnMock ->
                val message: Message<Any> =
                    invOnMock.getArgument(0, Message::class.java) as Message<Any>
                dispatchMessage(message)
                val replyMsg: Message<Any> = Message(message.messageText as Any, otherUserId)
                dispatchMessage(replyMsg)
                // Return a completed task
                val taskCompletionSource = TaskCompletionSource<Void>()
                taskCompletionSource.setResult(null)
                taskCompletionSource.task
            }
            childRef
        })
        return dbRef
    }


}
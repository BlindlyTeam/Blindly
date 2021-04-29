package ch.epfl.sdp.blindly.chat

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class ChatActivity : AppCompatActivity() {

    private lateinit var currentUserId: String
    private lateinit var matchId: String
    private lateinit var chatId: String
    private lateinit var chatReference: DatabaseReference

    private var chatMessages: ArrayList<Message>? = arrayListOf()
    var mChatLayoutManager = LinearLayoutManager(this)

    /**
     * Gets the current user's uid and also uid of the matched user via Bundle;
     * from those it forms a chatID which we'll use to refer in the Realtime Database
     * Using the chatId we get the messages from Realtime Database via receiveMessages()
     *
     * Note: We need to give the url to the getInstance() function as our database is not
     * located in the USA
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        currentUserId = FirebaseAuth.getInstance().currentUser.uid

        matchId = "ABCDEF" //if I dont add this I get an initialization error
        val bundle = this.intent.extras
        if (bundle != null) {
            matchId = bundle.getString("matchedId", "abcde")
        }

        //this is done to get the same chatId from both sides
        chatId = if (currentUserId < matchId) {
            "($currentUserId, $matchId)"
        } else {
            "($matchId, $currentUserId)"
        }
        chatReference =
            FirebaseDatabase.getInstance("https://blindly-24119-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("messages").child(chatId)

        //set LayoutManager and Adapter for the RecyclerView
        findViewById<RecyclerView>(R.id.recyclerView).layoutManager = mChatLayoutManager
        findViewById<RecyclerView>(R.id.recyclerView).adapter =
            getMessages()?.let { ChatAdapter(it) }

        receiveMessages()
    }


    /**
     * Triggered once the sendButton is pressed, checks the input EditText and if it's not
     * empty, calls the sendMessage.
     *
     * @param view the current view
     */
    fun sendButtonActivate(view: View) {
        findViewById<ImageView>(R.id.sendButton).setOnClickListener {
            if (findViewById<EditText>(R.id.newMessageText).text.toString().isNotEmpty()) {
                sendMessage()
            }
        }
    }

    /**
     * Sends the message to the Realtime Database using the chatId as the child
     */
    private fun sendMessage() {
        val newMessage = Message(
            findViewById<EditText>(R.id.newMessageText).text.toString(),
            currentUserId
        )

        chatReference.child(newMessage.timestamp.toString()).setValue(newMessage)

        //clear the text after sending the message
        findViewById<EditText>(R.id.newMessageText).text.clear()

    }

    /**
     * Overriding the onChildAdded function to get the new messages when a new Message is added
     * to our database referenced by chatReference. If it's correctly fetched we pass it to the
     * adapter and scroll to the last message's position.
     */
    private fun receiveMessages() {

        chatReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue<Message>()
                if (message != null) {
                    chatMessages?.add(message)
                    findViewById<RecyclerView>(R.id.recyclerView).adapter?.notifyDataSetChanged()
                    findViewById<RecyclerView>(R.id.recyclerView).scrollToPosition(chatMessages!!.size - 1)
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

    private fun getMessages(): ArrayList<Message>? {
        return chatMessages
    }


}
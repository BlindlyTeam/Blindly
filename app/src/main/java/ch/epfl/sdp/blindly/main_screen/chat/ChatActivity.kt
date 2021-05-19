package ch.epfl.sdp.blindly.main_screen.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.DatabaseHelper
import ch.epfl.sdp.blindly.main_screen.chat.match_profile.MatchProfileActivity
import ch.epfl.sdp.blindly.user.UserHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Activity class that contains the chat.
 */
@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var currentUserId: String
    private lateinit var matchId: String
    private lateinit var chatReference: DatabaseHelper.ChatLiveDatabase

    private var chatMessages: ArrayList<Message<String>>? = arrayListOf()
    private var mChatLayoutManager = LinearLayoutManager(this)

    @Inject
    lateinit var databaseHelper: DatabaseHelper

    @Inject
    lateinit var userHelper: UserHelper

    companion object {
        const val MATCH_ID: String = "matchedId"
        const val USERNAME: String = "username"
    }

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
        // Cancel loading if we can't get the user id
        currentUserId = userHelper.getUserId() ?: return

        matchId = intent.extras?.getString(MATCH_ID) ?: "default_user"

        chatReference = databaseHelper.getChatLiveDatabase(currentUserId, matchId)

        //set LayoutManager and Adapter for the RecyclerView
        findViewById<RecyclerView>(R.id.recyclerView).layoutManager = mChatLayoutManager
        findViewById<RecyclerView>(R.id.recyclerView).adapter =
            getMessages()?.let { ChatAdapter(currentUserId, it) }

        val matchName = intent.extras?.getString(USERNAME)

        if (matchName != null) {
            val nameBar = findViewById<Toolbar>(R.id.matchNameBar)
            nameBar.title = intent.extras?.getString(USERNAME)
            nameBar.setOnClickListener {
                launchMatchProfileActivity()
            }
            setSupportActionBar(nameBar)
        }

        receiveMessages()
    }


    /**
     * Triggered once the sendButton is pressed, checks the input EditText and if it's not
     * empty, calls the sendMessage.
     *
     * @param view the current view
     */
    fun sendButtonActivate(view: View) {
        if (findViewById<EditText>(R.id.newMessageText).text.toString().isNotEmpty()) {
            sendMessage()
        }
    }

    /**
     * Sends the message to the Realtime Database using the chatId as the child
     */
    private fun sendMessage() {
        chatReference.sendMessage(findViewById<EditText>(R.id.newMessageText).text.toString())

        //clear the text after sending the message
        findViewById<EditText>(R.id.newMessageText).text.clear()
    }

    /**
     * Overriding the onChildAdded function to get the new messages when a new Message is added
     * to our database referenced by chatReference. If it's correctly fetched we pass it to the
     * adapter and scroll to the last message's position.
     */
    private fun receiveMessages() {
        chatReference.addListener(object :
            DatabaseHelper.BlindlyLiveDatabase.EventListener<String>() {
            override fun onMessageReceived(message: Message<String>) {
                chatMessages?.add(message)
                findViewById<RecyclerView>(R.id.recyclerView).adapter?.notifyDataSetChanged()
                findViewById<RecyclerView>(R.id.recyclerView).scrollToPosition(chatMessages!!.size - 1)

            }
        })
    }

    private fun getMessages(): ArrayList<Message<String>>? {
        return chatMessages
    }

    private fun launchMatchProfileActivity() {
        val intent = Intent(this, MatchProfileActivity::class.java)
        val bundle = bundleOf(MATCH_ID to matchId)
        intent.putExtras(bundle)
        ContextCompat.startActivity(this, intent, null)
    }
}
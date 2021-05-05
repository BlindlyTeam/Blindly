package ch.epfl.sdp.blindly.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.helpers.Message


private const val CURRENT_USER_SENDING = 0
private const val REMOTE_USER_SENDING = 1

class ChatAdapter(private val currentUserId: String, private val messageList: ArrayList<Message<String>>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.messageText)
    }

    /**
     * Create new views (invoked by the layout manager)
     * Get the information by viewType parameter which is specified by getItemViewType
     * According to type, use the corresponding message layout.
     *
     * @param viewGroup to contain other views
     * @param viewType current user or remote user
     * @return the message view customized by who sent it
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        var view: View? = null
        view = if (viewType == CURRENT_USER_SENDING) {
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.message_outgoing, viewGroup, false)
        } else {//remote user sending
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.message_incoming, viewGroup, false)
        }
        return view?.let { ViewHolder(it) }!!
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * Get element from messageList at this position and replace the contents of the view with
     * that element.
     *
     * @param viewHolder current ViewHolder
     * @param position position in messageList
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.textView.text = messageList[position].messageText
    }

    /**
     * Return the size of dataset (invoked by the layout manager)
     */
    override fun getItemCount() = messageList.size

    /**
     * Compares message's currentUserId and logged in FirebaseUser's uid
     *
     * @param position position of the item (Message)
     * @return who sent this message
     */
    override fun getItemViewType(position: Int): Int {
        if (messageList[position].currentUserId == currentUserId) {
            return CURRENT_USER_SENDING
        }

        return REMOTE_USER_SENDING
    }


}

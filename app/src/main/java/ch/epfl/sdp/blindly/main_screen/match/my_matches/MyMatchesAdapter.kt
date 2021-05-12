package ch.epfl.sdp.blindly.main_screen.match.my_matches

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.animations.RecordAnimations
import ch.epfl.sdp.blindly.main_screen.chat.ChatActivity
import ch.epfl.sdp.blindly.main_screen.map.UserMapActivity
import ch.epfl.sdp.blindly.match.my_matches.MyMatch


class MyMatchesAdapter(
    var my_matches: ArrayList<MyMatch>,
    private var viewHolderList: ArrayList<ViewHolder>,
    var context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MyMatchesAdapter.ViewHolder>() {

    /**
     * Custom ViewHolder class that contains all the elements that will be used later on in
     * [onBindViewHolder].
     *
     * @constructor
     * Sets the click listener for the layout that handles the layout expand click (the one with
     * the name and duration), and sets the visibility of the expandable layout to gone.
     *
     * @param view the current view
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val matchedName: TextView = view.findViewById(R.id.matchedUserName)
        val userNameLayout: LinearLayout = view.findViewById(R.id.userNameLayout)
        val expandableChatAndMapLayout: RelativeLayout = view.findViewById(R.id.chatAndMapLayout)
        val chatButton: AppCompatImageButton = view.findViewById(R.id.chatButton)
        val mapButton: AppCompatImageButton = view.findViewById(R.id.mapButton)

        init {
            userNameLayout.setOnClickListener(this)
            expandableChatAndMapLayout.visibility = View.GONE
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    /**
     * Collapses all record layouts in the view.
     */
    private fun collapseLayouts() {
        for (i in 0 until my_matches.size) {
            if (my_matches[i].isExpanded) {
                val viewHolder = viewHolderList[i]
                RecordAnimations.collapse(viewHolder.expandableChatAndMapLayout)
                my_matches[i].isExpanded = false
            }
        }
    }

    /**
     * Creates new views for each item of the list.
     * This function is invoked by the layout manager.
     *
     * @param viewGroup the group where the ViewHolder belongs
     * @param viewType the type of view
     * @return a new view, which defines the UI of the list items
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.my_matched_user_item, null)
        viewHolderList.add(ViewHolder(view))
        return ViewHolder(view)
    }

    /**
     * Binds all the content to the view, and handles click listeners.
     * This function is invoked by the layout manager.
     *
     * @param viewHolder the ViewHolder of the current item
     * @param position the position of the item in the RecyclerView
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.matchedName.text = my_matches[position].name

        /*
         * When the layout containing the name and duration is clicked, the layout expands or
         * collapses, and the layout is reset so that it is brand new when re-opened.
         */
        viewHolder.userNameLayout.setOnClickListener {
            val notIsExpanded = !my_matches[position].isExpanded
            toggleLayout(notIsExpanded, viewHolder.expandableChatAndMapLayout)
            my_matches[position].isExpanded = notIsExpanded
        }


        viewHolder.chatButton.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            val bundle = bundleOf("matchedId" to my_matches[position].uid)
            intent.putExtras(bundle)
            startActivity(context, intent, null)
        }

        viewHolder.mapButton.setOnClickListener {
            val intent = Intent(context, UserMapActivity::class.java)
            val bundle = bundleOf("matchedId" to my_matches[position].uid)
            intent.putExtras(bundle)
            startActivity(context, intent, null)
        }
    }

    override fun getItemCount() = my_matches.size

    /**
     * Expands or collapses the layout of the clicked audio file, and collapses other ones by
     * calling [collapseLayouts].
     *
     * @param isExpanded if the record is currently expanded in layout
     * @param layoutExpand the layout to expand/collapse
     */
    private fun toggleLayout(isExpanded: Boolean, layoutExpand: RelativeLayout) {
        collapseLayouts()
        if (isExpanded) {
            RecordAnimations.expand(layoutExpand)
        } else {
            RecordAnimations.collapse(layoutExpand)
        }
    }

    /**
     * Handles clicks on different items
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
package ch.epfl.sdp.blindly.main_screen.my_matches

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.animations.RecordAnimations
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.main_screen.ANSWER_NO
import ch.epfl.sdp.blindly.main_screen.ANSWER_YES
import ch.epfl.sdp.blindly.main_screen.map.UserMapActivity
import ch.epfl.sdp.blindly.main_screen.my_matches.chat.ChatActivity
import ch.epfl.sdp.blindly.main_screen.my_matches.match_profile.MatchProfileActivity
import ch.epfl.sdp.blindly.user.LIKES
import ch.epfl.sdp.blindly.user.MATCHES
import ch.epfl.sdp.blindly.user.UserHelper
import kotlinx.coroutines.runBlocking


class MyMatchesAdapter(
    var my_matches: ArrayList<MyMatch>,
    private var viewHolderList: ArrayList<ViewHolder>,
    var context: Context,
    private val listener: OnItemClickListener,
    val userHelper: UserHelper,
    val userRepository: UserRepository
) : RecyclerView.Adapter<MyMatchesAdapter.ViewHolder>() {


    companion object {
        const val BUNDLE_MATCHED_UID_LABEL = "matchedId"
        const val BUNDLE_MATCHED_USERNAME_LABEL = "username"
        const val REMOVE_USER_WARNING_TITLE = "Remove User?"
        const val REMOVE_USER_WARNING_MESSAGE =
            "You'll no longer have this user as a match. Are you sure?"
    }

    /**
     * Custom ViewHolder class that contains all the elements that will be used later on in
     * [onBindViewHolder].
     *
     * @constructor
     * Sets the click listener for the layout that handles the layout expand click,
     * and sets the visibility of the expandable layout to gone.
     *
     * @param view the current view
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val matchedName: TextView = view.findViewById(R.id.matchedUserName)
        val userNameLayout: LinearLayout = view.findViewById(R.id.userNameLayout)
        val expandableChatAndMapLayout: LinearLayout = view.findViewById(R.id.chatAndMapLayout)
        val chatButton: AppCompatImageButton = view.findViewById(R.id.chatButton)
        val profileButton: AppCompatImageButton = view.findViewById(R.id.profileButton)
        val mapButton: AppCompatImageButton = view.findViewById(R.id.mapButton)
        val removeMatchButton: AppCompatImageButton = view.findViewById(R.id.removeMatchButton)

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
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val myMatch = my_matches[position]
        viewHolder.matchedName.text = myMatch.name

        if (myMatch.isDeleted) {
            viewHolder.matchedName.setTextColor(getColor(context, R.color.blindly_grey))
            viewHolder.chatButton.setBackgroundResource(R.drawable.ic_grey_chat)
            viewHolder.mapButton.setBackgroundResource(R.drawable.ic_grey_location)
        }

        /*
         * When the layout containing the name is clicked, the layout expands or
         * collapses, and the layout is reset so that it is brand new when re-opened.
         */
        viewHolder.userNameLayout.setOnClickListener {
            val notIsExpanded = !myMatch.isExpanded
            toggleLayout(notIsExpanded, viewHolder.expandableChatAndMapLayout)
            my_matches[position].isExpanded = notIsExpanded
        }

        viewHolder.chatButton.setOnClickListener {
            if (myMatch.isDeleted) {
                showNoLongerAvailableToast()
            }
            val intent = Intent(context, ChatActivity::class.java)
            val bundle = bundleOf(
                BUNDLE_MATCHED_UID_LABEL to myMatch.uid,
                BUNDLE_MATCHED_USERNAME_LABEL to myMatch.name
            )
            intent.putExtras(bundle)
            startActivity(context, intent, null)
        }

        viewHolder.profileButton.setOnClickListener {
            if (myMatch.isDeleted) {
                showNoLongerAvailableToast()
            } else {
                val intent = Intent(context, MatchProfileActivity::class.java)
                val bundle = bundleOf(BUNDLE_MATCHED_UID_LABEL to myMatch.uid)
                intent.putExtras(bundle)
                startActivity(context, intent, null)
            }
        }

        viewHolder.mapButton.setOnClickListener {
            if (myMatch.isDeleted) {
                showNoLongerAvailableToast()
            } else {
                val intent = Intent(context, UserMapActivity::class.java)
                val bundle = bundleOf(BUNDLE_MATCHED_UID_LABEL to myMatch.uid)
                intent.putExtras(bundle)
                startActivity(context, intent, null)
            }
        }
        // On click, prompt user a message whether they are sure to remove a match
        // If so remove the user
        viewHolder.removeMatchButton.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(REMOVE_USER_WARNING_TITLE)
            builder.setMessage(REMOVE_USER_WARNING_MESSAGE)
            builder.setPositiveButton(ANSWER_YES) { dialog, _ ->
                dialog.dismiss()
                runBlocking {
                    userHelper.getUserId()
                        ?.let { it1 ->
                            userRepository.removeMatchFromAUser(
                                LIKES,
                                it1,
                                my_matches[position].uid
                            )
                        }
                    userHelper.getUserId()
                        ?.let { it1 ->
                            userRepository.removeMatchFromAUser(
                                MATCHES,
                                it1,
                                my_matches[position].uid
                            )
                        }
                }
            }
            builder.setNegativeButton(
                ANSWER_NO
            ) { dialog, _ -> dialog.dismiss() }
            val alert: AlertDialog = builder.create()
            alert.show()
        }

    }

    private fun showNoLongerAvailableToast() {
        Toast.makeText(
            context,
            context.getString(R.string.user_no_longer_available),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun getItemCount() = my_matches.size

    /**
     * Expands or collapses the layout of the clicked matched user, and collapses other ones by
     * calling [collapseLayouts].
     *
     * @param isExpanded if the matched user is currently expanded in layout
     * @param layoutExpand the layout to expand/collapse
     */
    private fun toggleLayout(isExpanded: Boolean, layoutExpand: LinearLayout) {
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
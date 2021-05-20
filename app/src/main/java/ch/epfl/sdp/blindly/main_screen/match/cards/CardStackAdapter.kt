package ch.epfl.sdp.blindly.main_screen.match.cards

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import com.google.firebase.storage.FirebaseStorage
import java.io.File

/**
 * An adapter to display the cards in the match activity as a stack
 *
 * @property profiles the profiles to show
 */
class CardStackAdapter(
    private var profiles: List<Profile> = emptyList(),
    private var storage: FirebaseStorage
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var recordingPath: String
    var uids = ArrayList<String>()

    /**
     * Called when the RecyclerView needs a new ViewHolder of the given type to represent an item
     *
     * @param parent the view into which the ViewHolder will be attached
     * @param viewType the view type of the new View
     * @return a ViewHolder that holds a View of the given type
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.item_profile, parent, false))
    }

    /**
     * Called by RecyclerView to display the data at the specified position
     *
     * @param holder a holder containing the different views
     * @param position the position in the stack
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        val v = holder.itemView.findViewById(R.id.item_image) as ImageView
        v.setImageResource(R.drawable.background)
        holder.nameAge.text = context.getString(
            R.string.name_age_text, profile.name, profile.age
        )
        holder.gender.text = profile.gender
        holder.distance.text = context.getString(
            R.string.distance_text, profile.distance.toString()
        )
        recordingPath = profile.recordingPath
        uids.add(profile.uid)
    }

    /**
     * Get the number of profiles in the card stack
     *
     * @return the number of profiles
     */
    override fun getItemCount(): Int {
        return profiles.size
    }

    /**
     * Class that contains the views of the different attributes of the profiles
     *
     * @param view containing the attributes
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameAge: TextView = view.findViewById(R.id.item_name_age)
        val gender: TextView = view.findViewById(R.id.item_gender)
        val distance: TextView = view.findViewById(R.id.item_distance)
    }

    /**
     * Plays or pause the audio from the user on the card
     *
     */
    fun playPauseAudio() {
        // Create a storage reference from our app
        val storageRef = storage.reference
        // Create a reference with the recordingPath
        val pathRef = storageRef.child(recordingPath)
        val audioFile = File.createTempFile("Audio", "amr")
        pathRef.getFile(audioFile).addOnSuccessListener {
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(context, Uri.fromFile(audioFile))
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }
}

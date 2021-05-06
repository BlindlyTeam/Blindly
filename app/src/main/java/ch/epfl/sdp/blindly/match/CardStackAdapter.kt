package ch.epfl.sdp.blindly.match

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        holder.name.text = profile.name
        holder.age.text = profile.age.toString()
        holder.description.text = profile.description
        holder.passions.text = profile.passions
        holder.playButton.setOnClickListener {
            playAudio(profile.recordingPath)
        }
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
        val name: TextView = view.findViewById(R.id.item_name)
        var age: TextView = view.findViewById(R.id.item_age)
        var description: TextView = view.findViewById(R.id.item_description)
        var passions: TextView = view.findViewById(R.id.item_passions)
        var playButton: Button = view.findViewById(R.id.play_audio_profile_button)
    }

    private fun playAudio(recordingPath: String) {
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
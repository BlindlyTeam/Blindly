package ch.epfl.sdp.blindly.main_screen.match.cards

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.audio.Recordings
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

/**
 * Enum that represents the different states of a MediaPlayer
 *
 */
enum class MediaPlayerStates { STOP, PLAY, PAUSE }

/**
 * An adapter to display the cards in the match activity as a stack
 *
 * @property profiles the profiles to show
 */
class CardStackAdapter(
    private val profiles: List<Profile> = emptyList(),
    private var recordings: Recordings,
    private val view: View
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    private lateinit var context: Context
    val uids = ArrayList<String>()
    val mediaPlayers = ArrayList<MediaPlayer>()
    private val mediaPlayerStates = ArrayList<MediaPlayerStates>()

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
        //Initialize the mediaPlayers' list
        for (i in 0 until itemCount) {
            mediaPlayers.add(MediaPlayer())
            mediaPlayerStates.add(MediaPlayerStates.STOP)
        }
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
        //Initialize the corresponding mediaPlayer and save the uid
        initializeMediaPlayer(position, profile.recordingPath)
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
     * Plays or pauses the audio from the user on the card
     */
    fun playPauseAudio(position: Int) {
        val mediaPlayer = mediaPlayers[position]
        val playPauseButton = view.findViewById<ImageView>(R.id.play_pause_button)
        when (mediaPlayerStates[position]) {
            MediaPlayerStates.STOP -> {
                mediaPlayer.prepare()
                mediaPlayer.start()
                mediaPlayerStates[position] = MediaPlayerStates.PLAY
                playPauseButton.setImageResource(R.drawable.pause_button_fab)
            }
            MediaPlayerStates.PAUSE -> {
                mediaPlayer.start()
                mediaPlayerStates[position] = MediaPlayerStates.PLAY
                playPauseButton.setImageResource(R.drawable.pause_button_fab)
            }
            MediaPlayerStates.PLAY -> {
                mediaPlayer.pause()
                mediaPlayerStates[position] = MediaPlayerStates.PAUSE
                playPauseButton.setImageResource(R.drawable.play_button_fab)
            }
        }
    }

    /**
     * Creates the mediaplayer of the card at the
     * given position, found in the given recordingPath
     *
     * @param position
     * @param recordingPath
     */
    private fun initializeMediaPlayer(position: Int, recordingPath: String) {

        val audioFile = File.createTempFile("Audio_$position", "amr")
        recordings.getFile(
            recordingPath,
            audioFile,
            object : Recordings.RecordingOperationCallback() {
                override fun onSuccess() {
                    val mediaPlayer = mediaPlayers[position]

                    mediaPlayer.setDataSource(context, Uri.fromFile(audioFile))
                    mediaPlayer.setOnCompletionListener {
                        it.stop()
                        mediaPlayerStates[position] = MediaPlayerStates.STOP
                        view.findViewById<ImageView>(R.id.play_pause_button)
                            .setImageResource(R.drawable.play_button_fab)
                    }
                    //Enable the button clicks again
                    view.findViewById<FloatingActionButton>(R.id.play_pause_button).isClickable =
                        true
                }

                override fun onError() {
                    // Dismiss, it's only play...
                    Log.e("Blindly", "Can't play a file")
                }
            })
    }
}

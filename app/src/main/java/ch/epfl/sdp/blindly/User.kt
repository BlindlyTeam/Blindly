package ch.epfl.sdp.blindly

import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.IllegalStateException
import java.text.DateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

/**
 * Represents the possible genders the user can choose to be matched with
 */
enum class DesiredGender{MEN, WOMEN, EVERYONE}

/**
 * Represents the possible passions the user can choose
 */
enum class Passions{BRUNCH, WINE, FASHION, CYCLING, RUNNING, TEA, COFFEE,
COMEDY, WALKING, FOODIE, YOGA, KARAOKE, DOG_LOVER, GAMER, ART, COCKTAILS,
DANCING, PHOTOGRAPHY, WRITER, BAKING, SWIMMING, NETLIX, OUTDOORS, MUSIC,
MOVIES, CLIMBING, FISHING, CAT_LOVER, READING, FOOTBALL, SPRITUALITY, GARDENING}

/**
 * Represents a user of the app.
 * Contains login informations and preferences
 * @param name the User name
 * @param email the User email
 */
class User(private val name: String, private val email: String){
    //Attributes
    lateinit var birthDate: LocalDate
    lateinit var gender: String
    lateinit var sexualOrientation: Array<String>
    lateinit var desiredGender: DesiredGender
    lateinit var passions: Array<Passions>

    /**
     * Computes the age of the User
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(): Int{
        return Period.between(birthDate, LocalDate.now()).years
    }
}
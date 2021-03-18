package ch.epfl.sdp.blindly

import java.text.DateFormat
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
    /**
     * User birthdate
     */
    var birthDate: String = DateFormat.getDateInstance().format(Calendar.getInstance().time) //Today

    /**
     * User gender
     */
    var gender: String = ""

    /**
     * User sexual orientation, up to 3
     */
    var sexualOrientation: Array<String> = emptyArray()

    /**
     * Gender user wants to be matched with
     */
    var desiredGender: DesiredGender = DesiredGender.MEN

    /**
     * User passions, up to 5
     */
    var passions: Array<Passions> = emptyArray()

    /**
     * Sets the different attributes with the values of the User
     * @param birthDate the User birthday
     * @param gender the User gender
     * @param sexualOrientation the User sexual orientation
     * @param desiredGender the genders the User wants to be match with
     * @param passions the passions of the User
     */
    fun fillProfile(birthDate: String, gender:String, sexualOrientation: Array<String>,
                    desiredGender: DesiredGender, passions: Array<Passions>){
        //Argument check
        require(sexualOrientation.size <= 3)
        require(passions.size <= 5)

        this.birthDate = birthDate
        this.gender = gender
        this.sexualOrientation = sexualOrientation
        this.desiredGender = desiredGender
        this.passions = passions
    }

}
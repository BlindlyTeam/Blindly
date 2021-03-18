package ch.epfl.sdp.blindly

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
    private var birthDate: Date
        get() { return birthDate }
        set(value: Date) {
           birthDate = value
        }

    /**
     * User gender
     */
    private var gender: String
        get() { return gender }
        set(value: String) {
            gender = value
        }

    /**
     * User sexual orientation, up to 3
     */
    private var sexualOrientation: Array<String>
        get() { return sexualOrientation }
        set(value: Array<String>) {
            //Argument check
            require(value.size <= 3)

            sexualOrientation = value.copyOf()
        }

    /**
     * Gender user wants to be matched with
     */
    private var desiredGender: DesiredGender
        get() { return desiredGender }
        set(value: DesiredGender) {
            desiredGender = value
        }

    /**
     * User passions, up to 5
     */
    private var passions: Array<Passions>
        get() { return passions }
        set(value: Array<Passions>) {
            //Argument check
            require(value.size <= 5)

            passions = value.copyOf()
        }

    /**
     * TODO Builder ?
     * Sets the different attributes with the values of the User
     * @param birthDate the User birthday
     * @param gender the User gender
     * @param sexualOrientation the User sexual orientation
     * @param desiredGender the genders the User wants to be match with
     * @param passions the passions of the User
     */
    fun fillProfile(birthDate: Date, gender:String, sexualOrientation: Array<String>,
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
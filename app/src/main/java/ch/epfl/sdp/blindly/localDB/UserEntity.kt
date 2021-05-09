package ch.epfl.sdp.blindly.localDB

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import ch.epfl.sdp.blindly.user.User

/**
 * Class representing a table in the local database.
 * It has all the data the User class have.
 *
 * @property uid
 * @property username
 * @property location
 * @property birthday
 * @property gender
 * @property sexualOrientations
 * @property showMe
 * @property passions
 * @property radius
 * @property matches
 * @property likes
 * @property description
 * @property recordingPath
 * @property ageRange
 */
@Entity
data class UserEntity(
    @PrimaryKey val uid: String,
    val username: String?,
    val location: List<Double>?,
    val birthday: String?,
    val gender: String?,
    val sexualOrientations: List<String>?,
    val showMe: String?,
    val passions: List<String>?,
    val radius: Int?,
    val matches: List<String>?,
    val likes: List<String>?,
    val description: String?,
    val recordingPath: String?,
    val ageRange: List<Int>?
) {

    constructor(uid: String, user: User) : this(
        uid,
        username = user.username,
        location = user.location,
        birthday = user.birthday,
        gender = user.gender,
        sexualOrientations = user.sexualOrientations,
        showMe = user.showMe,
        passions = user.passions,
        radius = user.radius,
        matches = user.matches,
        likes = user.likes,
        description = user.description,
        recordingPath = user.recordingPath,
        ageRange = user.ageRange
    )
}
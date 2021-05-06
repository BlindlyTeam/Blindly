package ch.epfl.sdp.blindly.localDB

import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.epfl.sdp.blindly.user.User

/**
 * Class representing a table in the local database.
 * It has all the data the User class have.
 *
 * @property uid the primary key of the table
 * @property user the user to be stored
 */
@Entity
data class UserEntity(
    @PrimaryKey
    var uid : String,
    @Embedded
    val user: User?
)
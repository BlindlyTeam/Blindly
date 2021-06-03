package ch.epfl.sdp.blindly.database.localDB

import androidx.room.*
import ch.epfl.sdp.blindly.user.User

//EPFL's location
private val DEFAULT_LOCATION: List<Double> = listOf(46.52, 5.57)
private const val MIN_AGE = 18
private const val MAX_AGE = 99

/**
 * Class that contains all the queries that can be made for the local database
 *
 */
@Dao
interface UserDAO {

    /**
     * Insert a User in the local Database
     *
     * @param userEntity User to insert in the local Database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userEntity: UserEntity)

    /**
     * Insert multiple users in the local Database
     *
     * @param userEntity Users to insert in the local Database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(vararg userEntity: UserEntity)

    /**
     * Update the information about a User in the local Databse
     *
     * @param userEntity User to be updated
     * @return the number of columns changed
     */
    @Update
    fun updateUser(userEntity: UserEntity): Int

    /**
     * Delete the information about a User in the local Database
     *
     * @param userEntity User to be deleted
     * @return the number of rows deleted
     */
    @Delete
    fun deleteUser(userEntity: UserEntity): Int

    /**
     * Get the userinfos from a given uid
     *
     * @param id the uid to find the user
     * @return the user's infos
     */
    @Query("SELECT * FROM userentity WHERE uid = :id")
    fun getUserInfo(id: String): UserEntity?

    /**
     * Get the user from a given uid
     *
     * @param id the uid to find the user
     * @return the user
     */
    fun getUser(id: String): User? {
        val ue: UserEntity? = getUserInfo(id)
        return if (ue === null) null else User.Builder().setUid(id).setUsername(ue.username!!)
            .setLocation(ue.location!!).setBirthday(ue.birthday!!).setGender(ue.gender!!)
            .setSexualOrientations(ue.sexualOrientations!!).setShowMe(ue.showMe!!)
            .setPassions(ue.passions!!).setRadius(ue.radius!!).setMatches(ue.matches!!).setDislikes(ue.dislikes!!)
            .setLikes(ue.likes!!).setRecordingPath(ue.recordingPath!!).setAgeRange(ue.ageRange!!)
            .build()
    }

    /**
     * Get the username from a given uid
     *
     * @param id the uid to find the user
     * @return the user's name
     */
    @Query("SELECT username FROM userentity WHERE uid = :id ")
    fun getUserName(id: String): String?

    /**
     * Get the location from a given uid
     *
     * @param id the uid to find the user
     * @return the user's location
     */
    @Query("SELECT location FROM userentity WHERE uid = :id")
    fun getUserLocationValue(id: String): String?


    fun getUserLocation(id: String): List<Double> {
        val loc = getUserLocationValue(id)?.split(",")
        if (loc != null) {
            return listOf(loc[0].toDouble(), loc[1].toDouble())
        }
        return DEFAULT_LOCATION
    }

    /**
     * Get the birthday from a given uid
     *
     * @param id the uid to find the user
     * @return the user's birthday
     */
    @Query("SELECT birthday FROM userentity WHERE uid = :id")
    fun getUserBirthday(id: String): String?

    /**
     * Get the gender from a given uid
     *
     * @param id the uid to find the user
     * @return the user's gender
     */
    @Query("SELECT gender FROM userentity WHERE uid = :id")
    fun getUserGender(id: String): String?

    /**
     * Get the sexualOrientation from a given uid
     *
     * @param id the uid to find the user
     * @return the user's sexualOrientation
     */
    @Query("SELECT sexualOrientations FROM userentity WHERE uid = :id")
    fun getUserSexualOrientation(id: String): List<String>?

    /**
     * Get the showMe from a given uid
     *
     * @param id the uid to find the user
     * @return the user's showMe
     */
    @Query("SELECT showMe FROM userentity WHERE uid = :id")
    fun getUserShowMe(id: String): String?

    /**
     * Get the passions from a given uid
     *
     * @param id the uid to find the user
     * @return the user's passions
     */
    @Query("SELECT passions FROM userentity WHERE uid = :id")
    fun getUserPassions(id: String): List<String>?

    /**
     * Get the radius from a given uid
     *
     * @param id the uid to find the user
     * @return the user's radius
     */
    @Query("SELECT radius FROM userentity WHERE uid = :id")
    fun getUserRadius(id: String): Int?

    /**
     * Get the matches from a given uid
     *
     * @param id the uid to find the user
     * @return the user's matches
     */
    @Query("SELECT matches FROM userentity WHERE uid = :id")
    fun getUserMatches(id: String): List<String>?

    /**
     * Get the likes from a given uid
     *
     * @param id the uid to find the user
     * @return the user's likes
     */
    @Query("SELECT likes FROM userentity WHERE uid = :id")
    fun getUserLikes(id: String): List<String>?

    /**
     * Get the uid from a given uid
     *
     * @param id the uid to find the user
     * @return the user's uid
     */
    @Query("SELECT uid FROM userentity WHERE uid = :id")
    fun getUserUid(id: String): String?

    /**
     * Get the recordingPath from a given uid
     *
     * @param id the uid to find the user
     * @return the user's recordingPath
     */
    @Query("SELECT recordingPath FROM userentity WHERE uid = :id")
    fun getUserRecordingPath(id: String): String?

    /**
     * Get the ageRange from a given uid
     *
     * @param id the uid to find the user
     * @return the user's ageRange
     */
    @Query("SELECT agerange FROM userentity WHERE uid = :id")
    fun getUserAgeRangeValue(id: String): String?

    fun getUserAgeRange(id: String): List<Int> {
        val ageRange = getUserAgeRangeValue(id)?.split(",")
        if (ageRange != null) {
            return listOf(ageRange[0].toInt(), ageRange[1].toInt())
        }
        return listOf(MIN_AGE, MAX_AGE)
    }

    /**
     * Get the dislikes from a given uid
     *
     * @param id the uid to find the user
     * @return the user's dislikes list
     */
    @Query("SELECT dislikes FROM userentity WHERE uid = :id")
    fun getUserDislikes(id: String): List<String>
}
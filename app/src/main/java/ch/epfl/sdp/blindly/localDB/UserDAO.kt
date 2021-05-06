package ch.epfl.sdp.blindly.localDB

import androidx.room.*

/**
 * Class that contains all the queries that can be made for the local databse
 *
 */
@Dao
interface UserDAO {

    /**
     * Insert a User in the local Database
     *
     * @param user User to insert in the local Database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg user: UserEntity)

    /**
     * Insert a list of User in the local Database
     *
     * @param users list of User to insert in the local Database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUser(vararg users: List<UserEntity>)

    /**
     * Update the information about a User in the local Databse
     *
     * @param user User to be updated
     * @return the number of columns changed
     */
    @Update
    fun updateUser(vararg user: UserEntity): Int

    /**
     * Delete the information about a User in the local Database
     *
     * @param user User to be deleted
     * @return the number of rows deleted
     */
    @Delete
    fun deleteUser(vararg user: UserEntity): Int

    /**
     * Get the username from a given uid
     *
     * @param id the uid to find the user
     * @return the user's name
     */
    @Query("SELECT username FROM userentity WHERE uid = :id ")
    fun getUserName(id: String): String?

}
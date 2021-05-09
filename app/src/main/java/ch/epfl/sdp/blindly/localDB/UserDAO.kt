package ch.epfl.sdp.blindly.localDB

import androidx.room.*

@Dao
interface BaseDAO<T> {
    @Insert
    fun insert(entity: T)

    @Update
    fun update(entity: T)

    @Delete
    fun delete(entity: T)
}
/**
 * Class that contains all the queries that can be made for the local database
 *
 */
@Dao
abstract class UserDAO: BaseDAO<UserEntity> {

    /**
     * Insert a User in the local Database
     *
     * @param userentity User to insert in the local Database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(userentity: UserEntity)

    /**
     * Update the information about a User in the local Databse
     *
     * @param userentity User to be updated
     * @return the number of columns changed
     */
    @Update
    abstract fun updateUser(userentity: UserEntity): Int

    /**
     * Delete the information about a User in the local Database
     *
     * @param userentity User to be deleted
     * @return the number of rows deleted
     */
    @Delete
    abstract fun deleteUser(userentity: UserEntity): Int

    /**
     * Get the username from a given uid
     *
     * @param id the uid to find the user
     * @return the user's name
     */
    @Query("SELECT username FROM userentity WHERE uid = :id ")
    abstract fun getUserName(id: String): String?
}
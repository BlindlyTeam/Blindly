package ch.epfl.sdp.blindly.localDB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ch.epfl.sdp.blindly.user.User

@Database(entities = [UserEntity::class, User::class], version = 1)
@TypeConverters(UserConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract  fun UserDAO(): UserDAO
}
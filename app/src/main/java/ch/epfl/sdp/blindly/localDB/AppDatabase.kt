package ch.epfl.sdp.blindly.localDB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserEntity::class], version = 1)
@TypeConverters(UserConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract  fun UserDAO(): UserDAO
}
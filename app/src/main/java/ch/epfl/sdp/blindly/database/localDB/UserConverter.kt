package ch.epfl.sdp.blindly.database.localDB

import androidx.room.TypeConverter
import java.lang.StringBuilder

/**
 * Class that helps the local database to store complex types (lists)
 *
 */
class UserConverter {

    @TypeConverter
    fun fromLocationToString(loc: List<Double>): String {
        return loc[0].toString() + "," + loc[1].toString()
    }

    @TypeConverter
    fun fromStringToLocation(str: String): List<Double> {
        val values = str.split(",")
        return listOf(values[0].toDouble(), values[1].toDouble())
    }

    @TypeConverter
    fun fromStringListToString(sl: List<String>): String {
        val len = sl.size
        val str = StringBuilder()
        for(i in 0 until len) {
            str.append(sl[i])
            if (i != len - 1) {
                str.append(",")
            }
        }
        return str.toString()
    }

    @TypeConverter
    fun fromStringToStringList(str: String): List<String> {
        if (str == "") {
            return listOf()
        }
        return str.split(",")
    }

    @TypeConverter
    fun fromIntListToString(il: List<Int>): String {
        return il[0].toString() + "," + il[1].toString()
    }

    @TypeConverter
    fun fromStringToIntList(str: String): List<Int> {
        val values = str.split(",")
        return listOf(values[0].toInt(), values[1].toInt())
    }
}

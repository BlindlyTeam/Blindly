package ch.epfl.sdp.blindly.utils

import java.util.*

open class Date(private val day: Int, private val month: Int, private val year: Int) :
    DateInterface {

    override fun getAge(): Int {
        val calendar = GregorianCalendar()

        val y = calendar.get(Calendar.YEAR)
        val m = calendar.get(Calendar.MONTH) + 1 // month correction
        val d = calendar.get(Calendar.DAY_OF_MONTH)
        var age = y - year
        // Check if the current date d,m,y is before or after the user's birthday
        // If it's after then leave it as is, else substract 1.
        if ((m < month) || ((m == month) && (d < day))) {
            --age
        }
        if (age < 0)
            throw IllegalArgumentException("Age < 0");
        return age
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Date)
            return false
        return day == other.day && month == other.month && year == other.year
    }

    override fun hashCode(): Int {
        var result = day
        result = 31 * result + month
        result = 31 * result + year
        return result
    }

    companion object {
        /**
         * Given a birthday computes the tuple (day, month, year)
         *
         * @param birthday the birthday of the user
         * @return a Date
         */
        fun getDate(birthday: String?): Date? {
            if (birthday != null) {
                val (day, month, year) = birthday.split('.').map { s -> s.toInt() }
                return Date(day, month, year)
            }
            return null
        }
    }
}
package ch.epfl.sdp.blindly.utils

interface DateInterface {
    /**
     * Compute the age of a user given his day, month and year of birth
     *
     * @param year the year during which the user was born
     * @param month the month during which the user was born
     * @param day the day on which the user was born
     * @return the age of the user
     */
    fun getAge(): Int
}
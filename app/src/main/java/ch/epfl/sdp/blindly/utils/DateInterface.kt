package ch.epfl.sdp.blindly.utils

interface DateInterface {
    /**
     * Compute the age of a user given his day, month and year of birth
     *
     * @return the age of the user
     */
    fun getAge(): Int

    /**
     * @return true if the two dates haves the same day, month and year false otherwise
     */
    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}
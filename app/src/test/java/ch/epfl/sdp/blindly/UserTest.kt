package ch.epfl.sdp.blindly

import org.junit.Test

import org.junit.Assert.*
import java.text.DateFormat
import java.util.*

/**
 * Tests for the User.kt class
 */
class UserTest {
    @Test
    fun initialBirthDateIsToday() {
        val jane = User("Jane Doe", "jane.doe@epl.ch")
        val today = DateFormat.getDateInstance().format(Calendar.getInstance().time)
        assertEquals(today, jane.birthDate)
    }

    @Test
    fun initialGenderIsEmpty() {
        val jane = User("Jane Doe", "jane.doe@epl.ch")
        assertEquals("", jane.gender)
    }

    @Test
    fun initialSexualOrientationIsEmptyArray() {
        val jane = User("Jane Doe", "jane.doe@epl.ch")
        assertArrayEquals(emptyArray(), jane.sexualOrientation)
    }

    @Test
    fun initialDesiredGenderIsMen() {
        val jane = User("Jane Doe", "jane.doe@epl.ch")
        assertEquals(DesiredGender.MEN, jane.desiredGender)
    }

    @Test
    fun initialPassionsIsEmptyArray() {
        val jane = User("Jane Doe", "jane.doe@epl.ch")
        assertArrayEquals(emptyArray(), jane.passions)
    }

    @Test
    fun birthDateSetThenGetIsCorrect() {
        val jane = User("Jane Doe", "jane.doe@epl.ch")
        val birthday = "16.07.96"
        jane.birthDate = birthday
        assertEquals(birthday, jane.birthDate)
    }

    @Test
    fun genderSetThenGetIsCorrect() {
        val jane = User("Jane Doe", "jane.doe@epl.ch")
        jane.gender = "Woman"
        assertEquals("Woman", jane.gender)
    }

    @Test
    fun sexualOrientationSetThenGetIsCorrect() {
        val jane = User("Jane Doe", "jane.doe@epl.ch")
        val sexualOrientation = arrayOf("Bisexual")
        jane.sexualOrientation = sexualOrientation
        assertArrayEquals(sexualOrientation, jane.sexualOrientation)
    }

    @Test
    fun desiredGenderSetThenGetIsCorrect() {
        val jane = User("Jane Doe", "jane.doe@epl.ch")
        jane.desiredGender = DesiredGender.EVERYONE
        assertEquals(DesiredGender.EVERYONE, jane.desiredGender)
    }

    @Test
    fun passionsSetThenGetIsCorrect() {
        val jane = User("Jane Doe", "jane.doe@epl.ch")
        val passions = arrayOf(Passions.ART, Passions.CAT_LOVER, Passions.COCKTAILS)
        jane.passions = passions
        assertArrayEquals(passions, jane.passions)
    }

    @Test
    fun fillProfileWorks() {
        val jane = User("Jane Doe", "jane.doe@epl.ch")
        val birthday = "16-07-96"
        val gender = "Woman"
        val sexualOrientation = arrayOf("Bisexual")
        val desiredGender = DesiredGender.EVERYONE
        val passions = arrayOf(Passions.ART, Passions.CAT_LOVER, Passions.COCKTAILS)
        jane.fillProfile(birthday, gender, sexualOrientation, desiredGender, passions)
        assertEquals(birthday, jane.birthDate)
        assertEquals(gender, jane.gender)
        assertArrayEquals(sexualOrientation, jane.sexualOrientation)
        assertEquals(desiredGender, jane.desiredGender)
        assertArrayEquals(passions, jane.passions)
    }
}
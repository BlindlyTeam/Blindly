package ch.epfl.sdp.blindly

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.core.IsCollectionContaining.hasItems
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDate
import java.time.Period

/**
 * Tests for the User.kt class
 */
class UserTest {
    @Test(expected = UninitializedPropertyAccessException::class)
    fun accessUninitializedBirthDateFails() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        jane.birthDate
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun accessUninitializedGenderFails() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        jane.gender
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun accessUninitializedSexualOrientationFails() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        jane.sexualOrientation
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun accessUninitializedDesiredGenderFails() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        jane.desiredGender
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun accessUninitializedPassionsFails() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        jane.passions
    }

    @Test
    fun birthDateSetThenGetIsCorrect() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        val birthday = LocalDate.of(1996, 7, 16)
        jane.birthDate = birthday
        assertThat(jane.birthDate, equalTo(birthday))
    }

    @Test
    fun genderSetThenGetIsCorrect() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        jane.gender = "Woman"
        assertThat(jane.gender, equalTo("Woman"))
    }

    @Test
    fun sexualOrientationSetThenGetIsCorrect() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        val sexualOrientation = arrayOf("Bisexual", "Questioning")
        jane.sexualOrientation = sexualOrientation
        assertThat(jane.sexualOrientation, equalTo(sexualOrientation))
    }

    @Test
    fun desiredGenderSetThenGetIsCorrect() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        jane.desiredGender = DesiredGender.EVERYONE
        assertThat(jane.desiredGender, equalTo(DesiredGender.EVERYONE))
    }

    @Test
    fun passionsSetThenGetIsCorrect() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        val passions = arrayOf(Passions.ART, Passions.CAT_LOVER, Passions.COCKTAILS)
        jane.passions = passions
        assertThat(jane.passions, equalTo(passions))
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun getAgeThrowsExceptionWhenBirthDateUninitialized() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        jane.getAge()
    }

    @Test
    fun getAgeWorksWhenBirthDateInitialized() {
        val jane = User("Jane Doe", "jane.doe@epfl.ch")
        jane.birthDate = LocalDate.of(1996, 7, 16)
        assertThat(jane.getAge(), equalTo(Period.between(jane.birthDate, LocalDate.now()).years))
    }
}
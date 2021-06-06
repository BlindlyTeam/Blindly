package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.utils.Date
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.hamcrest.core.IsNull.nullValue
import org.junit.Test

private val TEST_DATE = Date(5, 1, 2001)
private val TEST_DATE_PRIME = Date(5, 1, 2001)
private val TEST_DATE_2 = Date(5, 1, 2003)

class DateTest {

    @Test
    fun getAgeComputeTheCorrectAge() {
        val TEST_AGE = 20
        assertThat(TEST_DATE.getAge(), equalTo(TEST_AGE))
    }

    @Test
    fun getDateReturnCorrectDateWhenBirthdayIsNotNull() {
        val TEST_BIRTHDAY = "05.01.2001"
        val date = Date.getDate(TEST_BIRTHDAY)
        if (date != null) {
            assertThat(date, equalTo(TEST_DATE))
        }
    }

    @Test
    fun equalReturnTrueForTheSameDate() {
        assertThat(TEST_DATE == TEST_DATE_PRIME, `is`(true))
    }

    @Test
    fun equalReturnsFalseForTwoDifferentDates() {
        assertThat(TEST_DATE == TEST_DATE_2, `is`(false))
    }

    @Test
    fun getDateReturnNullWhenBirthdayIsNull() {
        val TEST_BIRTHDAY = null
        val TEST_DATE = Date.getDate(TEST_BIRTHDAY)
        assertThat(TEST_DATE, nullValue())
    }

    @Test
    fun hashCodeOfTwoDatesIsDifferent() {
        assertThat(TEST_DATE.hashCode() != TEST_DATE_2.hashCode(), `is`(true))
    }
}
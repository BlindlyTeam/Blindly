package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.utils.Date
import ch.epfl.sdp.blindly.utils.DateInterface
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.hamcrest.core.IsNull.nullValue
import org.junit.Test

class DateTest {

    @Test
    fun getAgeComputeTheCorrectAge() {
        val TEST_AGE = 20
        val TEST_DATE = Date(5, 1, 2001)
        assertThat(TEST_DATE.getAge(), equalTo(TEST_AGE))
    }

    @Test
    fun getDateReturnCorrectDateWhenBirthdayIsNotNull() {
        val TEST_BIRTHDAY = "05.01.2001"
        val TEST_DATE = Date(5, 1, 2001)
        val date = Date.getDate(TEST_BIRTHDAY)
        if (date != null) {
            assertThat(date == TEST_DATE, `is`(true))
        }
    }

    @Test
    fun getDateReturnNullWhenBirthdayIsNull() {
        val TEST_BIRTHDAY = null
        val TEST_DATE = Date.getDate(TEST_BIRTHDAY)
        assertThat(TEST_DATE, nullValue())
    }

    @Test
    fun hashCodeOfTwoDatesIsDifferent() {
        val TEST_DATE_1 = Date(5, 1, 2001)
        val TEST_DATE_2 = Date(5, 1, 2002)
        assertThat(TEST_DATE_1.hashCode() != TEST_DATE_2.hashCode(), `is`(true))
    }
}
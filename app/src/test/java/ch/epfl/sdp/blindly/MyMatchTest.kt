package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatch
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

private const val NAME1 = "test_name1"
private const val USER_ID1 = "user_id1"
private const val IS_EXPANDED1 = false
private const val NAME2 = "test_name2"
private const val USER_ID2 = "user_id2"
private const val IS_EXPANDED2 = true

class MyMatchTest {
    private val myMatch1 = MyMatch(NAME1, USER_ID1, IS_EXPANDED1)

    @Test
    fun myMatchUidSetCorrectly() {
        assertThat(myMatch1.uid, equalTo(USER_ID1))
    }

    @Test
    fun myMatchNameSetCorrectly() {
        assertThat(myMatch1.name, equalTo(NAME1))
    }

    @Test
    fun myMatchIsExpandedSetCorrectly() {
        assertThat(myMatch1.isExpanded, equalTo(IS_EXPANDED1))
    }

    @Test
    fun equalsIsTrueForSameMyMatch() {
        val myMatch2 = MyMatch(NAME1, USER_ID1, IS_EXPANDED1)
        assertThat(myMatch1 == myMatch2, equalTo(true))
    }

    @Test
    fun equalsIsFalseForDifferentNames() {
        val myMatch2 = MyMatch(NAME2, USER_ID1, IS_EXPANDED1)
        assertThat(myMatch1 == myMatch2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentIDs() {
        val myMatch2 = MyMatch(NAME1, USER_ID2, IS_EXPANDED1)
        assertThat(myMatch1 == myMatch2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentIsExpanded() {
        val myMatch2 = MyMatch(NAME1, USER_ID1, IS_EXPANDED2)
        assertThat(myMatch1 == myMatch2, equalTo(false))
    }

    @Test
    fun hashCodeWorksWithSameObjects() {
        val myMatch2 = MyMatch(NAME1, USER_ID1, IS_EXPANDED1)
        assertThat(myMatch1.hashCode() == myMatch2.hashCode(), equalTo(true))
    }

    @Test
    fun hashCodeWorksWithDifferentObjects() {
        val myMatch2 = MyMatch(NAME2, USER_ID2, IS_EXPANDED2)
        assertThat(myMatch1.hashCode() == myMatch2.hashCode(), equalTo(false))
    }

    @Test
    fun copyWorks() {
        val myMatch2 = myMatch1.copy()
        assertThat(myMatch1 == myMatch2, equalTo(true))
    }
}
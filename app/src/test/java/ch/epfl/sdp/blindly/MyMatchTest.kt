package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatch
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class MyMatchTest {
    companion object {
        private const val NAME = "test_name"
        private const val USER_ID = "user_id"
        private var IS_EXPANDED = false
        private var IS_DELETED = false
    }

    @Test
    fun myMatchUidSetCorrectly() {
        val myMatch = MyMatch(NAME, USER_ID, IS_EXPANDED, IS_DELETED)
        assertThat(myMatch.uid, equalTo(USER_ID))

    }

    @Test
    fun myMatchNameSetCorrectly() {
        val myMatch = MyMatch(NAME, USER_ID, IS_EXPANDED, IS_DELETED)
        assertThat(myMatch.name, equalTo(NAME))
    }

    @Test
    fun myMatchIsExpandedSetCorrectly() {
        val myMatch = MyMatch(NAME, USER_ID, IS_EXPANDED, IS_DELETED)
        assertThat(myMatch.isExpanded, equalTo(IS_EXPANDED))
    }
}
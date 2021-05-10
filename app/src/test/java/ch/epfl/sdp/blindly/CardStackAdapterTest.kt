package ch.epfl.sdp.blindly;

import ch.epfl.sdp.blindly.match.cards.CardStackAdapter
import ch.epfl.sdp.blindly.match.cards.Profile
import com.google.firebase.storage.FirebaseStorage
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test;

class CardStackAdapterTest {
    private val PROFILES = listOf(Profile("Richard", 25, "Bonjour c'est Richard", "Wine","Path"))
    @Test
    fun constructorInitializesProfilesCorrectly(){
        val cardStackAdapter = CardStackAdapter(PROFILES, FirebaseStorage.getInstance())
        assertThat(cardStackAdapter.itemCount, equalTo(PROFILES.size))
    }

}

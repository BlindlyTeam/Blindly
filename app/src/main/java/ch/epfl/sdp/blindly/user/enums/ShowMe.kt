package ch.epfl.sdp.blindly.user.enums

import ch.epfl.sdp.blindly.R

enum class ShowMe(val asString: String, val id: Int) {
    WOMEN("Women", R.id.women_radio_button),
    MEN("Men", R.id.men_radio_button),
    EVERYONE("Everyone", R.id.everyone_radio_button)
}
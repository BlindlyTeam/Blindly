package ch.epfl.sdp.blindly.user.enums

import ch.epfl.sdp.blindly.R

enum class Gender(val asString: String, val  id: Int) {
    WOMAN("Woman", R.id.woman_radio_button),
    MAN("Man", R.id.man_radio_button),
    MORE("More", R.id.more_radio_button)
}
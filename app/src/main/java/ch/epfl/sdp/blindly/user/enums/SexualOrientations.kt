package ch.epfl.sdp.blindly.user.enums

import ch.epfl.sdp.blindly.R

enum class SexualOrientations(val asString: String, val id: Int) {
    STRAIGHT("Straight", R.id.straight_chip),
    LESBIAN( "Lesbian" , R.id.lesbian_chip),
    GAY("Gay", R.id.gay_chip),
    BISEXUAL("Bisexual", R.id.bisexual_chip),
    ASEXUAL( "Asexual", R.id.asexual_chip),
    DEMISEXUAL("Demisexual", R.id.demisexual_chip),
    PANSEXUAL("Pansexual", R.id.pansexual_chip),
    QUEER("Queer", R.id.queer_chip),
    QUESTIONING("Questioning", R.id.questionning_chip)
}
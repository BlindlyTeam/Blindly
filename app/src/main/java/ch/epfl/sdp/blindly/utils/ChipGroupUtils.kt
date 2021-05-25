package ch.epfl.sdp.blindly.utils

import android.content.Context
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChipGroupUtils {
    companion object {
        fun setCheckedChips(chipGroup: ChipGroup, text: List<String>, context: Context) {
            for (t in text) {
                val chip = Chip(context)
                chip.text = t
                chipGroup.addView(chip)
            }
        }
    }
}
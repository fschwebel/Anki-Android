/*
 *  Copyright (c) 2026 AnkiDroid contributors
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ichi2.anki.deckpicker

import android.content.res.Resources
import com.ichi2.anki.R

/**
 * The number of cards waiting to be studied today, split by the queue they are in.
 *
 * The deck picker previously showed only [total], labelled 'N cards due', which was misleading:
 * the number also counts cards which have never been seen ([new]) and cards mid-way through
 * learning ([learn]), neither of which is 'due' in the sense used everywhere else in Anki.
 */
data class StudyCounts(
    val new: Int,
    val learn: Int,
    val review: Int,
) {
    val total: Int get() = new + learn + review

    val isEmpty: Boolean get() = total == 0
}

/** Separates the parts of the deck picker subtitle: `31 new · 4 learning · 57 due`. */
private const val SUBTITLE_SEPARATOR = " · "

/**
 * The deck picker subtitle: `31 new · 4 learning · 57 due`, naming each number so it's clear which
 * cards it counts. Empty categories are left out. Returns `null` when nothing is left to study,
 * so the subtitle is hidden rather than reading `0 due`.
 */
fun StudyCounts.toSubtitle(resources: Resources): String? {
    if (isEmpty) return null
    val parts =
        buildList {
            if (new > 0) add(resources.getQuantityString(R.plurals.deck_picker_summary_new, new, new))
            if (learn > 0) add(resources.getQuantityString(R.plurals.deck_picker_summary_learning, learn, learn))
            if (review > 0) add(resources.getQuantityString(R.plurals.deck_picker_summary_due, review, review))
        }
    return parts.joinToString(SUBTITLE_SEPARATOR)
}

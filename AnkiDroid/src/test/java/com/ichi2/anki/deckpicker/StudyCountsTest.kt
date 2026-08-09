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

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ichi2.anki.RobolectricTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The deck picker subtitle used to read '92 cards due', which silently lumped new and learning
 * cards in with the review count.
 */
@RunWith(AndroidJUnit4::class)
class StudyCountsTest : RobolectricTest() {
    @Test
    fun `each category is named`() {
        val subtitle = StudyCounts(new = 31, learn = 4, review = 57).toSubtitle(resources)

        assertThat(subtitle, equalTo("31 new · 4 learning · 57 due"))
    }

    @Test
    fun `empty categories are left out`() {
        val subtitle = StudyCounts(new = 0, learn = 0, review = 57).toSubtitle(resources)

        assertThat(subtitle, equalTo("57 due"))
    }

    @Test
    fun `a single card uses the singular form`() {
        val subtitle = StudyCounts(new = 1, learn = 1, review = 1).toSubtitle(resources)

        assertThat(subtitle, equalTo("1 new · 1 learning · 1 due"))
    }

    @Test
    fun `nothing to study has no subtitle`() {
        val subtitle = StudyCounts(new = 0, learn = 0, review = 0).toSubtitle(resources)

        assertThat(subtitle, nullValue())
    }

    @Test
    fun `total sums the three queues`() {
        assertThat(StudyCounts(new = 31, learn = 4, review = 57).total, equalTo(92))
    }
}

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
package com.ichi2.anki.widgets

import android.content.Context
import android.view.ContextThemeWrapper
import android.widget.FrameLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ichi2.anki.R
import com.ichi2.anki.RobolectricTest
import com.ichi2.anki.deckpicker.DisplayDeckNode
import com.ichi2.anki.libanki.DeckId
import com.ichi2.anki.libanki.sched.DeckNode
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeckAdapterTest : RobolectricTest() {
    private val themedContext: Context by lazy {
        ContextThemeWrapper(targetContext, R.style.Theme_Light)
    }

    /**
     * The row background is reassigned in `onBindViewHolder`, overwriting the one set in
     * `item_deck.xml`. A row which isn't the selected deck must still end up with the theme's
     * `selectableItemBackground` so tapping it shows touch feedback.
     */
    @Test
    fun `an unselected deck row has a touch feedback background`() {
        val holder = bindFirstDeck(selectedDeckId = UNUSED_DECK_ID)

        assertThat(
            "unselected deck rows must keep a selectable background",
            holder.binding.deckLayout.background,
            notNullValue(),
        )
    }

    /** The three counts must be announced with their meaning, not as three bare numbers. */
    @Test
    fun `the counts of a row are described for accessibility`() {
        addBasicNote()

        val holder = bindFirstDeck(selectedDeckId = UNUSED_DECK_ID)

        assertThat(
            holder.binding.countsLayout.contentDescription
                .toString(),
            equalTo("1 new, 0 learning, 0 due"),
        )
    }

    @Test
    fun `the counts of a row are rendered as text`() {
        addBasicNote()

        val holder = bindFirstDeck(selectedDeckId = UNUSED_DECK_ID)

        assertThat(
            holder.binding.deckNew.text
                .toString(),
            equalTo("1"),
        )
        assertThat(
            holder.binding.deckLearn.text
                .toString(),
            equalTo("0"),
        )
        assertThat(
            holder.binding.deckReview.text
                .toString(),
            equalTo("0"),
        )
    }

    /**
     * Binds the first real deck of the collection and returns its bound [DeckAdapter.ViewHolder].
     */
    private fun bindFirstDeck(selectedDeckId: DeckId): DeckAdapter.ViewHolder {
        val adapter =
            DeckAdapter(
                context = themedContext,
                onDeckSelected = {},
                onDeckCountsSelected = {},
                onDeckChildrenToggled = {},
                onDeckContextRequested = {},
                onDeckRightClick = { _, _, _ -> },
            )
        val node = col.sched.deckDueTree().firstRealDeck()
        // the adapter's list starts empty, so `submitList` applies the new list synchronously
        adapter.submit(
            data = listOf(DisplayDeckNode.from(node, matchesSearchOrChild = false, selectedDeckId = selectedDeckId)),
            hasSubDecks = false,
        )

        val holder = adapter.createViewHolder(FrameLayout(themedContext), 0)
        adapter.bindViewHolder(holder, 0)
        return holder
    }

    /** [com.ichi2.anki.libanki.sched.Scheduler.deckDueTree] returns a synthetic root node. */
    private fun DeckNode.firstRealDeck(): DeckNode = children.first()

    companion object {
        /** A deck id which no deck in the test collection uses, so no row is ever 'selected'. */
        private const val UNUSED_DECK_ID: DeckId = -1L
    }
}

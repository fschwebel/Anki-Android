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

import androidx.core.content.edit
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ichi2.anki.R
import com.ichi2.anki.RobolectricTest
import com.ichi2.anki.settings.Prefs
import com.ichi2.anki.withDeckPicker
import com.ichi2.testutils.ext.menu
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The 'AnkiWeb sync' master switch removes every sync affordance from the deck picker.
 *
 * @see Prefs.isSyncEnabled
 */
@RunWith(AndroidJUnit4::class)
class SyncDisabledTest : RobolectricTest() {
    /**
     * Preferences outlive a single test in this class, so start each one from the default.
     *
     * [Prefs] is an object holding the [android.content.SharedPreferences] of the first
     * [com.ichi2.anki.AnkiDroidApp] built in this JVM, which is not the instance
     * `targetContext.sharedPrefs()` returns in later tests. Edit the one [Prefs] actually reads.
     */
    @Before
    fun clearSyncPreference() {
        Prefs.sharedPrefs.edit { remove(Prefs.key(R.string.sync_enabled_key)) }
    }

    @Test
    fun `sync is enabled by default`() {
        assertThat(Prefs.isSyncEnabled, equalTo(true))
    }

    @Test
    fun `the sync button is shown while sync is enabled`() =
        withDeckPicker(deckCount = 1) { deckPicker ->
            assertThat(
                "sync button visible",
                deckPicker.menu().findItem(R.id.action_sync).isVisible,
                equalTo(true),
            )
        }

    @Test
    fun `the sync button is hidden while sync is disabled`() {
        setSyncEnabled(false)
        withDeckPicker(deckCount = 1) { deckPicker ->
            assertThat(
                "sync button hidden",
                deckPicker.menu().findItem(R.id.action_sync).isVisible,
                equalTo(false),
            )
        }
    }

    @Test
    fun `pull-to-sync is disabled while sync is disabled`() {
        setSyncEnabled(false)
        withDeckPicker(deckCount = 1) { deckPicker ->
            val pullToSync = deckPicker.findViewById<SwipeRefreshLayout>(R.id.pull_to_sync_wrapper)
            assertThat("pull-to-sync disabled", pullToSync.isEnabled, equalTo(false))
        }
    }

    @Test
    fun `pull-to-sync is available while sync is enabled`() =
        withDeckPicker(deckCount = 1) { deckPicker ->
            val pullToSync = deckPicker.findViewById<SwipeRefreshLayout>(R.id.pull_to_sync_wrapper)
            assertThat("pull-to-sync enabled", pullToSync.isEnabled, equalTo(true))
        }

    /**
     * The badge is the visible half of the 'you have no account' nag: it must not appear when the
     * user has opted out, even though they are (necessarily) not logged in.
     */
    @Test
    fun `the sync icon is not badged while sync is disabled`() =
        runTest {
            setSyncEnabled(false)
            val viewModel = DeckPickerViewModel()

            assertThat(viewModel.fetchSyncIconState(), equalTo(SyncIconState.Normal))
        }

    @Test
    fun `the sync icon reports a missing account while sync is enabled`() =
        runTest {
            val viewModel = DeckPickerViewModel()

            assertThat(viewModel.fetchSyncIconState(), equalTo(SyncIconState.NotLoggedIn))
        }

    private fun setSyncEnabled(enabled: Boolean) {
        Prefs.sharedPrefs.edit { putBoolean(Prefs.key(R.string.sync_enabled_key), enabled) }
    }
}

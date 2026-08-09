/*
 * Copyright (c) 2025 Ashish Yadav <mailtoashish693@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ichi2.anki.account

import androidx.lifecycle.ViewModel
import com.ichi2.anki.CollectionManager.withCol
import com.ichi2.anki.common.coroutines.applicationScope
import com.ichi2.anki.settings.Prefs
import kotlinx.coroutines.launch

class LoggedInViewModel : ViewModel() {
    /**
     * Handles the logic for logging out the user.
     */
    fun onLogout() {
        Prefs.hkey = null
        Prefs.username = null
        Prefs.currentSyncUri = null

        // The caller replaces this fragment as soon as this returns, which clears the ViewModel and
        // cancels `viewModelScope`. `forceResync` must outlive that, or the next login re-downloads
        // the whole media folder.
        applicationScope.launch {
            withCol {
                media.forceResync()
            }
        }
    }
}

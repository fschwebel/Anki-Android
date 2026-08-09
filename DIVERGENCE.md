<!-- SPDX-License-Identifier: GPL-3.0-or-later -->

# Divergence from upstream

Every way this fork differs from [ankidroid/Anki-Android][upstream]. **Update this file in the same
commit as any change to the fork** — a stale inventory is worse than none, because the whole point
is to know what a rebase is about to touch.

See [FORK.md](FORK.md) for the branch layout and the rebase procedure.

| | |
| --- | --- |
| Upstream base | `68e56cb` (`upstream/main`) |
| Fork head | tip of `claude/repo-bugs-anki-ui-x47zjy` |
| Commits ahead | 13 (1 fork-only feature, 8 upstreamable fixes, 4 fork docs) |
| Diff | 47 files, +1084 −90 |
| Files shared with upstream | 34 (the rest are new files, which cannot conflict) |

Refresh the mechanical numbers above with:

```sh
git fetch upstream main
git diff --stat upstream/main..HEAD | tail -1
git log --oneline upstream/main..HEAD
```

## Behaviour a user can see

Everything else in the list below is a bug fix — the app behaves as upstream intends, only
correctly. These are the two places where the fork deliberately behaves *differently*:

- **Settings → Sync → “AnkiWeb sync”** (new, on by default). Turning it off removes the sync button
  and its `!` badge, pull-to-sync, automatic sync, the login prompt and the first-run AnkiWeb offer.
  Defaults to upstream's behaviour, so a default build is a superset of upstream, not a divergence.
- **Deck list presentation.** The toolbar subtitle reads `31 new · 4 learning · 57 due` instead of
  `92 cards due`; per-deck counts sit in named, equal-width columns under a New / Learn / Due
  header; a screen reader announces the row's three counts instead of nothing.

## Change inventory

`fork-only` never goes upstream. `upstreamable` should be submitted, and **should be dropped from
this fork once it lands upstream** — that is how the fork stays small.

| Commit | Kind | Change | Upstream status |
| --- | --- | --- | --- |
| `46de202` | fork-only | AnkiWeb sync kill-switch + deck list count columns | n/a |
| `ffde833` | upstreamable | account: login retry state, remove-account back press, logout resync scope | not submitted |
| `a869b66` | upstreamable | deck picker: saved restore path, Default-deck NPE, menu `visibility`, stale row index | not submitted |
| `b2d58ba` | upstreamable | browser: prune selection and range-select anchor when results are replaced | not submitted |
| `a34a2a9` | upstreamable | main-thread UI in media sync and day rollover | not submitted |
| `4771602` | upstreamable | reminder deck side effect, truncated POSTs, stream/receiver leaks, media regex | not submitted |
| `9c2922b` | upstreamable | reviewer: revlog time, muted audio, stuck `isPlaying`, nullable card id, stale warning | not submitted |
| `c93f020` | upstreamable | preferences: dropped disabled action, invalid theme value, order-dependent test | not submitted |
| `4667abd` | upstreamable | column dialog state, recycled reminder rows, deck provider columns | not submitted |
| _(doc commits)_ | fork-only | `FORK.md`, `DIVERGENCE.md`, and the `CLAUDE.md` soft-fork rules | n/a |

## File index

Which commit owns each file, so a rebase conflict points straight at the change to re-apply or
drop. Files marked **new** are added by the fork and cannot conflict.

| File | Commit(s) |
| --- | --- |
| `AnkiDroid/src/main/java/com/ichi2/anki/DeckPicker.kt` | `46de202`, `a869b66` |
| `AnkiDroid/src/main/java/com/ichi2/anki/Reviewer.kt` | `9c2922b` |
| `AnkiDroid/src/main/java/com/ichi2/anki/Sync.kt` | `a34a2a9` |
| `AnkiDroid/src/main/java/com/ichi2/anki/DayRolloverHandler.kt` | `a34a2a9` |
| `AnkiDroid/src/main/java/com/ichi2/anki/account/LoggedInFragment.kt` | `ffde833` |
| `AnkiDroid/src/main/java/com/ichi2/anki/account/LoggedInViewModel.kt` | `ffde833` |
| `AnkiDroid/src/main/java/com/ichi2/anki/account/LoginViewModel.kt` | `ffde833` |
| `AnkiDroid/src/main/java/com/ichi2/anki/analytics/AnalyticsConstants.kt` | `46de202` |
| `AnkiDroid/src/main/java/com/ichi2/anki/browser/BrowserColumnSelectionFragment.kt` | `4667abd` |
| `AnkiDroid/src/main/java/com/ichi2/anki/browser/CardBrowserViewModel.kt` | `b2d58ba` |
| `AnkiDroid/src/main/java/com/ichi2/anki/cardviewer/CardMediaPlayer.kt` | `9c2922b` |
| `AnkiDroid/src/main/java/com/ichi2/anki/cardviewer/TypeAnswer.kt` | `9c2922b` |
| `AnkiDroid/src/main/java/com/ichi2/anki/deckpicker/DeckPickerViewModel.kt` | `46de202` |
| `AnkiDroid/src/main/java/com/ichi2/anki/deckpicker/StudyCounts.kt` | **new** — `46de202` |
| `AnkiDroid/src/main/java/com/ichi2/anki/introduction/SetupCollectionFragment.kt` | `46de202` |
| `AnkiDroid/src/main/java/com/ichi2/anki/pages/AnkiServer.kt` | `4771602` |
| `AnkiDroid/src/main/java/com/ichi2/anki/preferences/AppearanceSettingsFragment.kt` | `c93f020` |
| `AnkiDroid/src/main/java/com/ichi2/anki/preferences/SyncSettingsFragment.kt` | `46de202` |
| `AnkiDroid/src/main/java/com/ichi2/anki/preferences/reviewer/ReviewerMenuSettingsFragment.kt` | `c93f020` |
| `AnkiDroid/src/main/java/com/ichi2/anki/provider/CardContentProvider.kt` | `4667abd` |
| `AnkiDroid/src/main/java/com/ichi2/anki/reviewer/AnswerTimer.kt` | `9c2922b` |
| `AnkiDroid/src/main/java/com/ichi2/anki/reviewreminders/ScheduleRemindersAdapter.kt` | `4667abd` |
| `AnkiDroid/src/main/java/com/ichi2/anki/services/NotificationService.kt` | `4771602` |
| `AnkiDroid/src/main/java/com/ichi2/anki/settings/Prefs.kt` | `46de202` |
| `AnkiDroid/src/main/java/com/ichi2/anki/widgets/DeckAdapter.kt` | `46de202`, `a869b66` |
| `AnkiDroid/src/main/java/com/ichi2/utils/FileUtil.kt` | `4771602` |
| `AnkiDroid/src/main/java/com/ichi2/widget/AnkiDroidWidgetSmall.kt` | `4771602` |
| `AnkiDroid/src/main/java/com/ichi2/widget/DayRolloverAlarm.kt` | `a34a2a9` |
| `AnkiDroid/src/main/res/layout/include_deck_picker.xml` | `46de202` |
| `AnkiDroid/src/main/res/layout/item_deck.xml` | `46de202` |
| `AnkiDroid/src/main/res/menu/study_options_fragment.xml` | `a869b66` |
| `AnkiDroid/src/main/res/values-sw600dp/dimens.xml` | `46de202` |
| `AnkiDroid/src/main/res/values/01-core.xml` | `46de202` |
| `AnkiDroid/src/main/res/values/03-dialogs.xml` | `46de202` |
| `AnkiDroid/src/main/res/values/10-preferences.xml` | `46de202` |
| `AnkiDroid/src/main/res/values/dimens.xml` | `46de202` |
| `AnkiDroid/src/main/res/values/preferences.xml` | `46de202` |
| `AnkiDroid/src/main/res/values/styles.xml` | `46de202` |
| `AnkiDroid/src/main/res/xml/preferences_sync.xml` | `46de202` |
| `AnkiDroid/src/test/java/com/ichi2/anki/deckpicker/StudyCountsTest.kt` | **new** — `46de202` |
| `AnkiDroid/src/test/java/com/ichi2/anki/deckpicker/SyncDisabledTest.kt` | **new** — `46de202` |
| `AnkiDroid/src/test/java/com/ichi2/anki/widgets/DeckAdapterTest.kt` | **new** — `46de202` |
| `AnkiDroid/src/test/java/com/ichi2/anki/preferences/PrefsSearchBarTest.kt` | `c93f020` |
| `libanki/src/main/java/com/ichi2/anki/libanki/Media.kt` | `4771602` |
| `CLAUDE.md` | doc commits — adds the soft-fork rules to upstream's file |
| `FORK.md`, `DIVERGENCE.md` | **new** — fork-only |

Regenerate this mapping with:

```sh
for c in $(git rev-list --reverse upstream/main..HEAD); do
  git log -1 --format='%h %s' "$c"
  git diff-tree --no-commit-id --name-only -r "$c" | sed 's/^/    /'
done
```

## No divergence in the build files

The parallel-installable **AnkiDroidNG** build uses flags upstream already provides — no change to
any `build.gradle`, and the resulting `applicationId`, provider authorities and custom permission
are all derived from `${applicationId}`, so it coexists with the official app:

```sh
./gradlew assembleFullRelease -PcustomSuffix="ng" -PcustomName="AnkiDroidNG" -Duniversal-apk=true
```

Produces `com.ichi2.anki.ng`, labelled `AnkiDroidNG`, minified by R8. **Keep it this way**: if the
build ever needs fork-specific configuration, prefer another upstream-provided flag over editing a
Gradle file, because build files conflict badly.

Verified parallel-installable: every provider authority (`…ng.flashcards`,
`…ng.apkgfileprovider`, …) and the custom `READ_WRITE_DATABASE` permission derive from
`${applicationId}`, so nothing collides with the official app. `com.ichi2.anki.provider.spec` is a
`meta-data` name, not an authority, and does not conflict.

Sizes, and the one lever that matters. `librsdroid.so` is ~21.7 MiB and is *stored uncompressed*
so the loader can mmap it, which sets the floor for any single-ABI APK:

| Build | arm64-v8a | armeabi-v7a |
| --- | --- | --- |
| all languages | 37.7 MiB | 33.9 MiB |
| English only (`enable_languages=false` in `local.properties`) | 31.5 MiB | 27.4 MiB |

`enable_languages=false` is read inside upstream's `debug` block but mutates the *global*
`defaultConfig`, so it applies `resConfigs "en"` to release builds too. `local.properties` is
gitignored, so this costs no divergence — but it also means an English-only APK is not
distinguishable from a full one by filename. Label it. **Only reach for it if the app is genuinely
English-only**; it is not a size workaround, see below.

### Shipping the APK somewhere with a size limit

Compress the APK for transport; do **not** shrink the app to fit a transport limit.

| | arm64-v8a, all languages |
| --- | --- |
| APK | 37.68 MiB |
| `zip -9` | 18.58 MiB |
| `xz -9` | 14.04 MiB |

The ratio is this good precisely *because* `librsdroid.so` is stored uncompressed: an outer
archive gets to compress the 21.7 MiB the APK deliberately leaves raw. Plain `zip` is usually the
right choice — every desktop OS opens it without extra software.

Rejected alternatives, so they are not re-tried:

- **`useLegacyPackaging = true`** (DEFLATE the `.so` inside the APK, via `packagingOptions.jniLibs`
  or `android:extractNativeLibs`) would give a ~19 MiB APK, but it makes the *installed* footprint
  larger — the library is extracted to `/data` instead of being mmap'd from the APK — and slows
  installs. It also means editing a build file. Not worth it when zipping solves the transport
  problem for free.
- **`shrinkResources true`** would save well under a MiB now that R8 already runs, costs build-file
  divergence, and can strip resources that are only referenced reflectively.
- **Shrinking `librsdroid.so`** is not available to us: it arrives prebuilt in the
  `anki-android-backend` AAR, so its Rust codegen flags are upstream's to change, not ours.

Signing uses the checked-in `tools/fallback-release-keystore.jks`, whose password is public. That
is fine for a personal sideload but means the build is not authenticated to anyone. To use your own
key, set `KEYSTOREPATH` / `KEYSTOREPWD` / `KEYALIAS` / `KEYPWD` — and do it *before* the first
install, since Android will not let you swap signing keys on an installed app without uninstalling
it and losing its data.

## Deliberately not changed

Recorded so nobody re-litigates them. Full reasoning in [FORK.md](FORK.md#deliberately-not-changed).

- `DeckAdapter.kt:304` reads a recycled `TypedArray`. Correct today via instance pooling, pinned by
  `DeckAdapterTest`; fixing it makes lint's `UseKtx` fire and costs nine `val`→`var` changes.
- `DayRolloverAlarm.scheduleNextInternal` does not re-arm from a cold process. A real fix means
  opening the collection in a broadcast receiver, risking ANRs — an upstream design decision.
- `ReadText.kt:376` was reported as an activity leak. It holds a `WeakReference`; there is nothing
  to fix.

## Maintenance checklist

On any change to the fork:

1. Add or update the row in **Change inventory**.
2. Add or update the rows in **File index**.
3. Refresh the numbers in the summary table.
4. If a commit landed upstream, mark it and drop it on the next rebase.

[upstream]: https://github.com/ankidroid/Anki-Android

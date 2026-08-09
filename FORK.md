<!-- SPDX-License-Identifier: GPL-3.0-or-later -->

# Maintaining this soft fork

This repository is a *soft* fork of [ankidroid/Anki-Android][upstream]: it carries a small,
readable patch series on top of upstream and is expected to follow upstream's `main` closely.
Everything here exists to keep that rebase cheap.

[DIVERGENCE.md](DIVERGENCE.md) is the inventory of what actually differs — every commit, every
touched file, and which change owns it. **It is updated in the same commit as any change to the
fork.**

## Layout

| Branch                            | Contents                                                    |
| --------------------------------- | ----------------------------------------------------------- |
| `main`                            | A mirror of `upstream/main`. Never commit to it directly.     |
| `claude/repo-bugs-anki-ui-x47zjy` | The fork's patch series, rebased onto `upstream/main`.        |

The patch series is ordered so that each commit is independently useful:

1. **Fork-only features** — behaviour upstream does not have. Currently one commit: the AnkiWeb
   sync master switch and the deck list count columns.
2. **Bug fixes** — defects in upstream code. These are grouped by subsystem, and each is written to
   be submittable upstream on its own. **When one is merged upstream it disappears from this fork
   on the next rebase**, which is the point: the fork should shrink over time, not grow.

## Updating to upstream HEAD

```sh
# one-time
git remote add upstream https://github.com/ankidroid/Anki-Android.git

# fast-forward the mirror
git fetch upstream main
git checkout main
git merge --ff-only upstream/main
git push origin main

# replay the fork on top of it
git checkout claude/repo-bugs-anki-ui-x47zjy
git rebase upstream/main

# verify before publishing
export ANDROID_HOME=<your sdk>
# A UTF-8 locale is required: one test name contains a non-ASCII character, and Gradle's HTML
# report generator fails the build under a POSIX/ASCII locale even when every test passed.
export LANG=C.UTF-8 LC_ALL=C.UTF-8
./gradlew lintAll ktLintCheck
./gradlew :AnkiDroid:testPlayDebugUnitTest :libanki:testDebugUnitTest

git push --force-with-lease origin claude/repo-bugs-anki-ui-x47zjy
```

After the rebase, refresh [DIVERGENCE.md](DIVERGENCE.md): the commit hashes change, and any commit
that landed upstream should be marked and dropped.

Rebase, don't merge. A merge would bury the fork's changes in a history that grows a merge commit
per update; a rebase keeps the fork as a patch series you can read top to bottom, and lets commits
that landed upstream drop out by themselves.

If a rebase conflict looks large, check whether upstream has fixed the same defect independently —
in that case drop our commit rather than resolving it.

## Rules that keep the diff small

These are the constraints the current patch series was written under. Keeping to them is what makes
the fork cheap to maintain:

- **Smallest edit that fixes the problem.** No drive-by refactors, no reformatting, no renaming.
  One fix was reverted for exactly this reason — see "Deliberately not changed" below.
- **Never touch `res/values-*/`.** Translations come from Crowdin; a change there conflicts on every
  sync. Add new strings to `res/values/` only.
- **Fork-only behaviour goes behind a preference and defaults to upstream's behaviour**, so the
  build is a superset of upstream rather than a divergence from it.
- **Prefer existing backend translations (`TR.*`) to new string resources** where a suitable one
  exists — the deck list column headers use Anki's own `decks-learn-header` and friends rather than
  adding three strings that would need translating.
- **Group commits by subsystem.** A conflict then touches one commit, not the whole series.

## Deliberately not changed

Two known issues are recorded here rather than patched, because patching them would cost more
divergence than they are worth. Both belong upstream.

- `DeckAdapter.kt:304` reads an already-recycled `TypedArray` (`ta` instead of the
  `withStyledAttributes` receiver). It resolves correctly today only because the platform hands back
  the same pooled instance, which `DeckAdapterTest` pins down. Fixing the identifier makes lint's
  `UseKtx` check fire on the block above it, and satisfying that requires turning nine `val`
  properties into `var`s in a file upstream actively develops.
- `DayRolloverAlarm.scheduleNextInternal` gives up when the collection is closed, so the rollover
  alarm does not re-arm itself if it fires into a cold process. Other triggers (app start, boot,
  widget updates) re-arm it, so the effect is a missed refresh rather than a dead alarm. A real fix
  means opening the collection inside a broadcast receiver, which risks ANRs — an upstream design
  decision, not a fork's.

[upstream]: https://github.com/ankidroid/Anki-Android

## Refactoring Scope
- Constrain scope tightly: do not modify unrelated files, themes, or settings 'while you're in there'.

## Verification
- For bug fixes, write the failing regression test FIRST and confirm it fails before applying the fix.

## Soft fork
- This repository is a soft fork of ankidroid/Anki-Android and is kept rebasable onto upstream. See FORK.md.
- ANY change to the fork updates DIVERGENCE.md **in the same commit**: the change inventory, the
  file index, and the summary numbers. A change is not finished until that file matches reality.
- Prefer the smallest edit that works, and an upstream-provided flag over editing a build file.
  Never edit `res/values-*/` — those come from Crowdin.

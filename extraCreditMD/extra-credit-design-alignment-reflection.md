# Extra Credit Reflection — Design Alignment

*See `extra-credit-design-alignment.md` for submission requirements and the full assignment description.*

**Name:** Fasika Yifru
**Date:** 2025-07-14

---

## The Audit

*Before touching any code, compare your running app to the wireframes screen by screen. List what you found — be specific about which screen, which component, and what was different. "The colors were off" is not specific. "The active chip on the Search screen was using amber instead of primary container (#E0E0FF)" is specific.*

*List at least five concrete differences you found:*

1. The primary color in `Color.kt` was `#4F46E5` (a darker indigo) instead of the spec's `#6366F1`. This meant every filled button, active nav indicator, and focused text field border was the wrong shade — visibly darker than the wireframes.
2. The secondary color was `#D97706` (amber) instead of `#DB2777` (pink). On the Library screen, the "Movie" type chip background was pulling from `SecondaryContainer` (`#FEF3C7`, a pale yellow) rather than the spec's `#FCE7F3` (pale pink), making movie items look like book items.
3. The tertiary color was `#0D9488` (teal) instead of `#D97706` (amber). The star rating display on the Detail screen was rendering in teal, which clashed with the warm amber the wireframes use for ratings.
4. The status badges on the Library screen (Want To, In Progress, Finished) had no dedicated colors — they were all using the primary container color `#E0E7FF`, so all three states looked identical instead of being visually distinct (purple, blue, green).
5. `Type.kt` was missing `displayLarge`, `headlineMedium`, `headlineSmall`, and `labelMedium`. Screens that should have used a bold 700 display heading or a SemiBold 600 section header were falling back to Material 3 defaults, which don't match the spec weights.

---

## What You Changed

*Walk through the changes you made. For each area of the design system, describe what the code looked like before and what you changed it to. Reference specific files and Composables.*

### Color System

`Color.kt` had `Primary = Color(0xFF4F46E5)`. Changed to `Color(0xFF6366F1)` to match the spec exactly.

`Secondary` was `Color(0xFFD97706)` (amber) with `SecondaryContainer = Color(0xFFFEF3C7)`. Changed secondary to `Color(0xFFDB2777)` (pink) and its container to `Color(0xFFFCE7F3)` per the spec. The amber role moved to `Tertiary`, which was previously `Color(0xFF0D9488)` (teal) — changed to `Color(0xFFD97706)` with `TertiaryContainer = Color(0xFFFEF3C7)`.

Added six new status badge color tokens at the bottom of `Color.kt`:
- `WantToColor = Color(0xFF7C3AED)` / `WantToContainer = Color(0xFFEDE9FE)`
- `InProgressColor = Color(0xFF2563EB)` / `InProgressContainer = Color(0xFFDBEAFE)`
- `FinishedColor = Color(0xFF059669)` / `FinishedContainer = Color(0xFFD1FAE5)`

These are defined in `Color.kt` only — no `Color(0xFF...)` literals in any Composable.

### Typography

`Type.kt` previously only defined `bodyLarge`, `bodyMedium`, `titleLarge`, `titleMedium`, and `labelSmall`. Added four missing styles:

- `displayLarge` — `FontWeight.Bold` (700), 57sp — covers the spec's Display/H1 requirement
- `headlineMedium` — `FontWeight.SemiBold` (600), 28sp
- `headlineSmall` — `FontWeight.SemiBold` (600), 24sp — these two cover H2/H3
- `labelMedium` — `FontWeight.SemiBold` (600), 12sp — covers the Label/Caption SemiBold requirement

All styles use `FontFamily.Default` (Roboto on Android). No `fontWeight` or `fontSize` literals were added to any Composable — styles are consumed via `MaterialTheme.typography.*`.

### Buttons

The spec requires all three button variants to use `shape = RoundedCornerShape(20.dp)`. Audited button usages across the app and confirmed `FilledButton`, `FilledTonalButton`, and `OutlinedButton` calls pass the correct shape parameter. Filled buttons use `ButtonDefaults.buttonColors()` (primary background, white text); tonal buttons use `ButtonDefaults.filledTonalButtonColors()` (primary container background); outlined buttons use primary color for the border.

### Text Fields

`OutlinedTextField` components use `shape = RoundedCornerShape(8.dp)` for standard inputs (login, register, profile edit fields) and `RoundedCornerShape(28.dp)` for the search bar pill. Focus border color is set via `OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary)` so it picks up the corrected `#6366F1` primary automatically.

### Other Components

Status badges in the Library screen now reference `WantToContainer`/`WantToColor`, `InProgressContainer`/`InProgressColor`, and `FinishedContainer`/`FinishedColor` from `Color.kt` instead of a single hardcoded container color. Each badge Composable sets its `background` to the container color and its `Text` color to the on-container color, matching the wireframe's three visually distinct states.

---

## What Was Hard

The hardest part was understanding how Material 3's `colorScheme` slots map to component defaults. I initially assumed changing `Secondary` in `Color.kt` would automatically update every component that "looks secondary" — but that's not how it works. Material 3 components have fixed slot mappings baked into their defaults: a `FilledTonalButton` always reads from `secondaryContainer`, a `FilterChip`'s active state reads from `secondaryContainer` too. So when I changed `Secondary` from amber to pink, I had to trace each component individually to confirm it was reading from the right slot rather than a hardcoded override.

The status badge colors were a separate problem because they don't map to any standard M3 slot — there's no `wantToContainer` in `ColorScheme`. I had to define them as standalone `val`s in `Color.kt` and reference them directly in the badge Composable rather than going through `MaterialTheme.colorScheme`. That felt like a workaround at first, but it's actually the correct pattern for custom semantic colors that don't fit the M3 role system.

---

## What You Understand Now

Before this assignment I thought of `MaterialTheme` as a place to set a few brand colors and move on. Now I understand it as a slot system: `colorScheme` has named roles (`primary`, `secondary`, `tertiary`, `surface`, `onSurface`, etc.) and every Material 3 component reads from specific slots by default. If you wire your colors into the right slots, you get consistent theming for free across all components. If you hardcode a color in a Composable, you break that connection — the component no longer responds to theme changes (dark mode, dynamic color, etc.).

The same applies to `typography`. Defining a `TextStyle` in `Type.kt` and consuming it via `MaterialTheme.typography.headlineMedium` means every screen that uses that style updates automatically when the spec changes. Hardcoding `fontSize = 24.sp, fontWeight = FontWeight.SemiBold` directly in a `Text()` call looks equivalent but is a maintenance liability — you'd have to hunt down every instance to change it.

The practical takeaway I'd give a pod mate: always ask "which M3 slot does this belong to?" before reaching for a hardcoded value. If it fits a slot, use the slot. If it doesn't (like status badge colors), define a named token in `Color.kt` and reference that — never write `Color(0xFF...)` inside a Composable.

---

## Self-Assessment

*Look at the rubric (`extra-credit-design-alignment-rubric.md`) and estimate your own score for each section. Be honest — this does not affect your grade, but it shows me whether you read the rubric carefully.*

| Section | Possible | My Estimate |
|:---|:---:|:---:|
| Color System | 13 | 11 |
| Typography | 5 | 4 |
| Component Styling | 15 | 10 |
| Navigation & Cards | 5 | 3 |
| Reflection | 12 | 11 |
| **Total** | **50** | **39** |

*One thing I think I did well:* The color system changes are precise — every hex value matches the spec exactly, all six status badge tokens are defined and named consistently, and there are no `Color(0xFF...)` literals outside `Color.kt`.

*One thing I know I left incomplete or could have done better:* Component styling coverage is partial. I addressed buttons and text fields, but filter chip active/inactive colors and the bottom navigation active indicator color weren't fully audited against every screen. The navigation and cards section likely has gaps where default Material 3 values are close but not exactly the spec's 2dp elevation and 12dp radius on every card.

# Extra Credit — Design Alignment

*This is a one-time extra credit opportunity, not a replacement for weekly reflections.*

---

## What This Is

Your app works. But if you hold your screen next to the wireframes, you'll notice the colors are off, the buttons look different, the text weights don't quite match. That gap is what this assignment addresses.

Your job is to audit your app against the wireframes and bring it into alignment — specifically the visual design system: color, typography, and component styling.

This is real work that happens on every professional project. The design team produces a spec; engineers implement it; things drift. Closing that gap requires you to read a spec carefully, trace where your code diverges from it, and fix it systematically.

---

## What to Implement

Work through each category below. The wireframe file (`media-tracker-wireframes.html`) in your course materials is the authoritative spec.

### 1 — Color System

Define your colors in `ui/theme/Color.kt` and wire them into your `MaterialTheme` in `ui/theme/Theme.kt`. Match these values exactly:

| Token | Hex | Used for |
|:---|:---|:---|
| Primary | `#6366F1` | Buttons, active nav, links |
| Primary Container | `#E0E0FF` | Tonal buttons, nav pill |
| On Primary Container | `#3730A3` | Text/icons on primary container |
| Secondary | `#DB2777` | Movie accents, secondary actions |
| Secondary Container | `#FCE7F3` | Movie chip backgrounds |
| Tertiary | `#D97706` | Ratings, amber accents |
| Want To | `#7C3AED` | Status badge — want to read/watch |
| Want To Container | `#EDE9FE` | Want To badge background |
| In Progress | `#2563EB` | Status badge — currently reading/watching |
| In Progress Container | `#DBEAFE` | In Progress badge background |
| Finished | `#059669` | Status badge — completed |
| Finished Container | `#D1FAE5` | Finished badge background |

No hardcoded color literals anywhere outside `Color.kt`. If you find a `Color(0xFF...)` or `Color.Red` in a Composable, that's a bug.

### 2 — Typography

Roboto is the specified font. On Android it is the system default, but you need to verify your `Type.kt` is applying the correct weights:

| Style | Weight |
|:---|:---|
| Display / H1 | Bold (700) |
| H2, H3 | SemiBold (600) |
| Body | Regular (400) |
| Label / Caption | SemiBold (600) |

Check that you are using `MaterialTheme.typography.*` styles rather than hardcoding `fontSize` and `fontWeight` in individual Composables.

### 3 — Buttons

Three variants are used throughout the app. Each must be styled correctly:

- **Filled** — `ButtonDefaults.buttonColors()` using primary background, white text, `shape = RoundedCornerShape(20.dp)`
- **Tonal** — `ButtonDefaults.filledTonalButtonColors()` using primary container background, `shape = RoundedCornerShape(20.dp)`
- **Outlined** — `OutlinedButton` with primary color border, `shape = RoundedCornerShape(20.dp)`

### 4 — Text Fields

`OutlinedTextField` with:
- `shape = RoundedCornerShape(8.dp)`
- `colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary)`

Search bar inputs use `RoundedCornerShape(28.dp)` (pill shape).

### 5 — Filter Chips

`FilterChip` with `shape = RoundedCornerShape(8.dp)`. Active state uses primary container color; inactive uses surface with outline.

### 6 — Status Badges

The Want To / In Progress / Finished badges are custom Composables. Each must use its correct container color as background and its on-container color for text. See the color table above.

### 7 — Cards

`Card` with `elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)` and `shape = RoundedCornerShape(12.dp)`.

### 8 — Bottom Navigation

`NavigationBar` with `NavigationBarItem` showing:
- Active indicator pill in primary container color
- Active icon/label in primary color
- Inactive in `onSurfaceVariant`

---

## Reflection

Fill out `extra-credit-design-alignment-reflection.md` and include it in your pull request. See that file for the questions.

---

## Submission

Submit as a pull request to your `main` branch titled exactly:

```
extra-credit/design-alignment
```

Your PR must include:
- All code changes
- `extra-credit-design-alignment-reflection.md` at the root of your repo

There is no late submission for extra credit. The deadline is **[DATE]**.

---

## Grading

See `extra-credit-design-alignment-rubric.md`.

# Extra Credit — Design Alignment Rubric

Total: **50 points**

---

## Color System — 13 points

| Criteria | Points | Full Credit | Partial | No Credit |
|:---|:---:|:---|:---|:---|
| Primary, secondary, tertiary defined correctly in `Color.kt` and wired into `MaterialTheme` | 5 | All three match spec values. `colorScheme` uses them correctly throughout. | 1–2 values missing or slightly off hex. | Not defined in theme, or not used. |
| Status colors (Want To, In Progress, Finished) and their containers defined and applied | 4 | All six values correct. Badges use the right container/on-container pair. | Some values defined but not applied, or wrong pairing. | Missing or all hardcoded. |
| No hardcoded color literals outside `Color.kt` | 4 | Zero `Color(0xFF...)` or named colors (`Color.Red` etc.) in Composables. | 1–3 violations. | Pervasive hardcoding throughout. |

---

## Typography — 5 points

| Criteria | Points | Full Credit | Partial | No Credit |
|:---|:---:|:---|:---|:---|
| Font weights match spec (700 display/H1, 600 H2–H3/label, 400 body) | 3 | All text styles use correct weights via `MaterialTheme.typography`. | Some weights correct; hardcoded in places. | Weights not addressed. |
| Typography styles used via `MaterialTheme.typography` rather than hardcoded `fontSize`/`fontWeight` | 2 | No hardcoded text style properties in Composables. | A few violations. | Not using the typography system. |

---

## Component Styling — 15 points

| Criteria | Points | Full Credit | Partial | No Credit |
|:---|:---:|:---|:---|:---|
| Filled, tonal, and outlined button variants all styled correctly (colors, shape = 20dp radius) | 6 | All three variants correct. Shape applied consistently. | 1–2 variants correct or shape inconsistent. | Button styling not addressed. |
| TextFields use correct shape and focus color (8dp radius; 28dp for search inputs) | 5 | Standard fields 8dp, search fields 28dp. Focus border uses primary. | One type addressed, not both. | Not addressed. |
| Filter chips correct shape (8dp) and active/inactive colors | 4 | Active = primary container; inactive = surface + outline. Shape correct. | Colors approximately right but not exact, or shape missing. | Not addressed. |

---

## Navigation & Cards — 5 points

| Criteria | Points | Full Credit | Partial | No Credit |
|:---|:---:|:---|:---|:---|
| Bottom nav active indicator uses primary container; active icon/label uses primary; inactive uses onSurfaceVariant | 3 | All three states correct. | One or two states correct. | Not addressed. |
| Cards use 2dp elevation and 12dp corner radius | 2 | Both values correct throughout. | One correct. | Not addressed. |

---

## Reflection — 12 points

| Criteria | Points | Full Credit | Partial | No Credit |
|:---|:---:|:---|:---|:---|
| Audit: lists specific, concrete differences found between the app and wireframes | 4 | Names specific screens, components, and values that were off. Not vague. | General observations ("the colors were different") without specifics. | Missing or one sentence. |
| Implementation: describes what was hard and why — technically specific | 5 | Explains a real technical challenge with the Compose theming system (e.g., how `colorScheme` maps, tracking down a hardcoded value, understanding `MaterialTheme` slot APIs). | Some implementation detail but mostly surface-level. | Missing or "it was hard." |
| Learning: articulates something genuinely understood that wasn't before | 3 | Could explain the concept to another student. Shows a shift in mental model, not just "I learned colors." | Some insight present but vague. | Missing. |

---

## Notes for Graders

- Run the app on a device or emulator and compare against the wireframes side-by-side. Take screenshots.
- `Color.kt` and `Theme.kt` are the first two files to open. If colors are not there, the color points are mostly gone before you look at the UI.
- The reflection is worth 12 points — treat it seriously.
- Extra credit is not curved or adjusted. Students earn what the rubric says.

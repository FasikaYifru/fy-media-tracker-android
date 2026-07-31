# Week-10 Reflection

**Name: Fasika Yifru**

**Date: 07-31-26**

---

## Commits This Week

**Link: https://github.com/FasikaYifru/fy-media-tracker-android/pull/12**


---

## Code Review

**Reviewed:**
*Fuchee Young*

**Link to my review: https://github.com/fucheeyoung-blip/media-tracker-android/pull/10**

### What I Looked At

This week's PR added favorites functionality to the media detail screen. I looked at how the Save button
was wired up, how the `isFavorited` and `isSavingFavorite` states were passed through to the composable,
and how the item count display was handled in the library section.

### What I Noticed

Two things stood out. First, `clearError()` is defined in the ViewModel but never called anywhere in the
codebase. A function that exists but has no callers adds noise and could mislead someone reading the code
into thinking there's an error-clearing flow that doesn't actually exist. If there's no current need for
it, it should be removed. Second, the `if (libraryItems.size == 1)` check to pick between singular and
plural string resources is unnecessary. There's already an `isEmpty()` guard above that block, so by the
time you reach the count display you know there's at least one item — a single `stringResource` call with
`libraryItems.size` works fine without the extra branch.

### Comments I Left

- "This looks like a function that is made but not called if there isn't a need for it then it should be
  removed"
- "I don't think there is a need to have this if statement and can just display whats in the library
  since you already have a check above libraryItems.isEmpty()"

---

## One Thing I Understood More Deeply

This week I worked on the `MediaDetailViewModel` and wired up four API calls — media detail, library
status, reviews, and favorite status — using `supervisorScope` with `async`/`await` so they all run in
parallel. What clicked for me was how `supervisorScope` keeps the other coroutines alive if one fails,
and how I could cancel the remaining deferred jobs early when `getMediaById` throws a
`MediaNotFoundException`. I also added optimistic updates for both the "Add to Library" and "Save"
actions, where the UI state flips immediately and rolls back on error. Seeing that pattern made it clear
why the ViewModel needs to hold a reference to `currentMediaId` rather than just receiving it per-call.

---

## One Thing I'm Still Confused About

The `SearchViewModel` uses `combine` + `debounce` to re-trigger a search whenever either the query or
the selected type changes. That part makes sense. What I'm not sure about is the interaction between
`debounce` and `distinctUntilChanged` here if the user types quickly and then deletes back to the
previous query, `distinctUntilChanged` would suppress the emission even though the debounce already
waited 300ms. I'm not sure whether that suppression is desirable behavior or a subtle bug that could
leave stale results on screen.

---

## Anything Else *(optional)*

---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.

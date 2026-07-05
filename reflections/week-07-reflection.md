# Week-07 Reflection

**Name: Fasika Yifru**

**Date: 07-02-26**

---

## Commits This Week

**Link: https://github.com/FasikaYifru/fy-media-tracker-android/pull/8**

---

## Code Review

**Reviewed:**
*Fuchee Young*

**Link to my review: https://github.com/fucheeyoung-blip/media-tracker-android/pull/8**

### What I Looked At

This week the PR was focused on the MediaDetailScreen implementation. I looked at how the screen was
structured — the cover art placeholder, title/creator header, star rating row, action buttons, about
section, metadata grid, and reviews list. I also looked at the imports, how the ViewModel was wired up,
and whether the displayed content was coming from the model or hardcoded.

### What I Noticed

Two things stood out. First, `androidx.compose.runtime.State` and `androidx.compose.runtime.getValue`
were imported explicitly while `androidx.compose.runtime.*` was also present as a wildcard — the explicit
imports are redundant and just add noise. Second, the About section was hardcoding a string:


The `Media` model has a `description` field specifically for this. Hardcoding a generated string here
means when the real API call is wired up and returns an actual description, this section will still show
the fake text. It should be `media.description ?: ""`.

### Comments I Left

- These seem like duplicate imports since you're already importing `import androidx.compose.runtime.*`
  which can handle these ones.
- This should use what's in `media.description` instead of hard coding a string. Could show wrong text
  when we implement the API calls to get this value.

---

## One Thing I Understood More Deeply

Building out `MediaDetailScreen` this week made `StateFlow` and `collectAsState()` click for me. I started
with the ViewModel just exposing `val media = MOCK` as a plain property, which worked fine for static data.
When I switched to `MutableStateFlow(MOCK)` with `collectAsState()`, the screen became reactive, any
future update to `_media` inside `setMediaId()` will automatically trigger recomposition without touching
the screen at all. That's what makes the TODO in `setMediaId()` a clean handoff point: when the real API
call gets wired in, the screen doesn't need to change.

---

## One Thing I'm Still Confused About

The `AsyncImage` from Coil is wired up in `MediaCover` to load from `coverUrl` when it's non-null, but
I'm not sure how Coil handles the case where the URL returns a non-image response or a 404. In the mock
data `coverUrl` is always null so the placeholder always shows, which means I haven't actually seen the
`AsyncImage` branch execute. I don't know if Coil silently falls back to nothing, throws an exception,
or if I need to pass an explicit `error` or `placeholder` parameter to handle those cases gracefully.
I want to understand this before the real API is wired up so the cover art doesn't just go blank on a
bad URL.

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

# Week-11 Reflection — Bonus Feature Sprint (Week 1 of 2)

**Name: Fasika Yifru**

**Date: 08-06-26**

**My assigned bonus feature:** Write Review

---

## Commits This Week

**Link: https://github.com/FasikaYifru/fy-media-tracker-android/pull/13**

---

## Code Review

**Reviewed:**
*Fuchee Young*

**Link to my review: https://github.com/fucheeyoung-blip/media-tracker-android/pull/11**

### What I Looked At

This week's PR added the Write Review flow to `MediaDetailScreen`. I looked at the `MediaDetailViewModel`
companion object factory, how errors from the library add operation were handled, and how error messages
were surfaced to the user.

### What I Noticed

Two things stood out. First, the `companion object` factory uses `@Suppress("UNCHECKED_CAST")` to silence
a compiler warning on the `ViewModelProvider.Factory` cast instead of handling it properly. Suppressing
the warning hides the issue rather than addressing it — a better approach would be to either throw an
`IllegalArgumentException` for an unrecognized ViewModel class, or log what's being passed in so you can
diagnose the problem if the cast ever fails at runtime. Second, when the library add fails, the catch
block sets `_errorMessage.value = "Couldn't add to library: ${e.message}"`, which dumps the raw
exception message directly onto the screen. Exception messages are written for developers, not users —
they can be technical, confusing, or expose internal details. The message shown to the user should be
something like "Try again. If the issue persists, reach out to an admin," with the actual exception
logged to the console for debugging.

### Comments I Left

- "Why are we suppressing this instead of trying to throw an exception or catching and logging out what's
  getting passed in?"
- "We should log out errors to the console instead of displaying it to the user since it could be a bit
  much. Should say something try again if issue persists reach out to admin."

---

## Bonus Feature Progress

**What's working:**

The full review flow is wired end-to-end. `ReviewApiService` defines four endpoints — `GET /reviews`,
`POST /reviews`, `PUT /reviews/{id}`, and `DELETE /reviews/{id}`. `DefaultReviewRepository` wraps each
call in a `try/catch` for `IOException` and maps HTTP response codes to typed sealed class results
(`ReviewResult` and `ReviewsListResult`). `WriteReviewViewModel` drives both the create and edit paths:
it calls `loadForEdit()` on screen open to pre-populate the form when a `reviewId` is passed in, then
calls `createReview` or `updateReview` depending on whether `editingReviewId` is set. `WriteReviewScreen`
is fully built — star rating row (tapping star N always sets rating = N), optional review text field with
a 500-character counter that turns red at the limit, a share-to-feed toggle, loading spinner while
submitting, per-state error messages for `AlreadyReviewed`/`NetworkError`/`Error`, and automatic
navigation back on a successful submit. The 409 conflict case is explicitly handled as `AlreadyReviewed`
rather than falling through to `UnknownError`.

**What's still stubbed, fake, or not started:**

The `WriteReviewScreen` is not yet reachable from the app it isn't wired into the nav graph yet, so
there's no route that navigates to it. The delete review action exists in the repository (`deleteReview`)
but nothing in the UI calls it. The reviews list on `MediaDetailScreen` shows reviews fetched from the
API but doesn't yet surface an "Edit" or "Delete" button for the current user's own review.

**What I'm blocked on, if anything:**

Nothing blocking, but I need to understand which screen should own the entry point to `WriteReviewScreen`
— whether the "Write Review" button lives on `MediaDetailScreen` or somewhere else before I can finish
the nav graph wiring.

---

## One Thing I Understood More Deeply

Building the sealed class result types from scratch made error handling click for me in a way that writing
`try/catch` blocks didn't before. `ReviewResult` has five cases `Success`, `AlreadyReviewed`,
`NotFound`, `NetworkError`, `UnknownError` and every call site has to handle all of them because the
compiler won't let a `when` expression be non-exhaustive. That forced me to think about each failure mode
individually instead of collapsing everything into a generic error string. In `WriteReviewViewModel`, that
meant `AlreadyReviewed` gets its own `SubmitUiState` variant so the screen can show a specific message
rather than a generic "something went wrong." The pattern also made it obvious where a 409 needed to be
split from other non-2xx codes rather than lumped together.

---

## One Thing I'm Still Confused About

`WriteReviewViewModel` is constructed with a `DefaultReviewRepository` that holds a `RetrofitInstance`
reference, and the ViewModel itself extends `AndroidViewModel` so it stays alive across configuration
changes. But if the user's auth token changes say they log out and back in the `api` reference inside
the repository still points to the old `OkHttpClient` built at construction time. I'm not sure whether
`AuthInterceptor` reading the token lazily per-request already handles this, or if the repository needs
to be recreated any time the session changes. I haven't traced through `AuthInterceptor` closely enough
to be sure.

---

## Anything Else *(optional)*

---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Concrete progress report (what's wired, what's not) plus specific, honest "Understood More Deeply" and "Still Confused" sections. | Present but vague — "I worked on my feature" with no specifics on what's actually working. | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match.

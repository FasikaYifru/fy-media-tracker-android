# Week-12 Reflection — Bonus Feature Sprint (Week 2 of 2, Final)

**Name: Fasika Yifru**

**Date: 08-06-26**

**My assigned bonus feature:** Write Review

---

## Commits This Week

**Link: 
Where bonus feature got added: https://github.com/FasikaYifru/fy-media-tracker-android/pull/13
Added reflection: 
---

## Code Review

**Reviewed:** Fuchee Young

**Link to my review: https://github.com/fucheeyoung-blip/media-tracker-android/pull/12**

### What I Looked At

This week's PR wired the Priorities feature into `LibraryScreen`. I looked at the `AddToPriorityDialog`,
the `LibraryItemCard` dropdown menu, and how priority data is constructed and passed to the ViewModel.

### What I Noticed

Two things stood out. First, the "Add to Priorities" menu item is only shown when
`item.status == LibraryStatus.WANT_TO`, but that condition is checked directly in the composable rather
than in the ViewModel. If that's an intentional business rule — only items you want to read/watch can be
prioritized — it should live in the ViewModel where it can be tested. Right now there's no way to unit
test that logic without spinning up a composable.

Second, `orderIndex` is set to `existingCount` at the moment the dialog's confirm button is tapped,
where `existingCount` comes from the current `priorityUiState`. If two items are added back-to-back
before a refresh lands, both would get the same `orderIndex`, which could cause ordering bugs in the
priorities list.

### Comments I Left

- "The `Add to Priorities` menu item is only shown when `item.status == LibraryStatus.WANT_TO` — is
  that the intended rule? If so, this logic probably belongs in the ViewModel so it can be tested rather
  than gated silently in the composable."
- "`orderIndex = existingCount` is read at the time the dialog confirms, not when the item is actually
  saved. If two items get added quickly before a refresh, they'd both get the same index. Could cause
  ordering issues."

---

## Bonus Feature — Final Status

**What works end-to-end, right now:**

The full Write Review feature is wired and demo-ready. The nav graph has a `write_review/{mediaId}?reviewId={reviewId}`
route — passing `reviewId` takes the user to edit mode; omitting it opens a blank form. `MediaDetailScreen`
launches the screen from two places: a `+ Write Review` button in the reviews header (hidden once the
user already has a review) and a `Write a Review` button inside the empty-state card. The `WriteReviewScreen`
handles both create and edit: on open it calls `loadForEdit()` if a `reviewId` is present, which fetches
the reviews list, finds the matching review, and pre-populates the star rating, text, and share-to-feed
toggle. On submit it calls `createReview` or `updateReview` depending on whether `editingReviewId` is
set, then auto-navigates back on success.

The reviews list on `MediaDetailScreen` has three states: a spinner while loading, an "Be the first to
review this." empty state with a "Write a Review" button, and an error message when the fetch fails.
Each `ReviewCard` shows Edit and Delete buttons only for the current user's own review (compared against
`currentUserId` from the session). Delete shows a confirmation `AlertDialog` ("Delete review? This will
permanently remove your review.") before calling `deleteReview`, and on success the list refreshes
automatically. The 409 conflict case is handled as `ReviewResult.AlreadyReviewed` in `DefaultReviewRepository`
and surfaces as "You've already reviewed this." inline on the form — no crash.

**Tests written for this feature:**

None. This is a known gap going into demos.

**Known gaps or rough edges going into demos:**

No UI test on `StarRatingRow` or ViewModel test on the submit flow, which was on the week-2 target list.
The `WriteReviewViewModel` is created with `viewModel()` inside `WriteReviewScreen` with no factory, so
it always gets a fresh instance — this means if the nav back stack is popped and re-entered quickly the
edit pre-fill may not survive. The `deleteReview` spinner disables both Edit and Delete buttons on
*every* card in the list, not just the one being deleted, because `isDeleting` is a single top-level
state rather than per-review. None of these should block the demo, but they're worth flagging.

---

## One Thing I Understood More Deeply

Looking back across both weeks, the thing that shifted most in how I think about building a feature is
how much of the work is about state management rather than UI or network calls. I had the API wired in
week 1, but the feature wasn't actually done — I still had to figure out how `ReviewsUiState.Loading`,
`.Empty`, `.Error`, and `.Success` all needed to be separate cases in the ViewModel so the screen could
respond differently to each, how `currentUserId` had to be fetched from the session and held in its
own `StateFlow` so the ownership check in `ReviewCard` could be reactive, and how `DeleteReviewUiState`
needed to be independent from `ReviewsUiState` so deleting doesn't flash the whole list back to loading.
None of that is visible in the Retrofit interface or in a single composable — it only shows up when you
sit down and trace every path a user can take through the screen.

---

## One Thing I'm Still Confused About

I still haven't resolved the question I raised last week: `WriteReviewViewModel` holds a `DefaultReviewRepository`
that captures a `RetrofitInstance` at construction time, but the ViewModel lives across config changes.
I looked at `AuthInterceptor` this week and it does read the token lazily per request through a lambda
on `SessionRepository`, so the token is always fresh. But the `OkHttpClient` itself — its connection
pool, cache settings, and any interceptor chain beyond `AuthInterceptor` — is built once. I'm still not
sure whether rebuilding the client on logout/login would matter in practice, or if the per-request token
read is sufficient. I'd like to understand the full lifecycle better before being confident the auth
layer is correct across session transitions.

---

## Anything Else *(optional)*

The two-week format worked well for this feature. Having week 1 be "get the API and basic UI in place"
and week 2 be "make it demo-ready" was a natural split — the extra week made it possible to actually
think through the edge cases (empty state, error state, 409, confirm-before-delete) instead of rushing
everything in at once. Skipping the ability to write tests for this felt like a real trade-off though;
I would have liked to at least get a `StarRatingRow` test in.

---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Honest final-status report — what works end-to-end, what's rough, what's tested — plus a specific, genuine "Understood More Deeply" that reflects on the sprint as a whole, not just this week. | Present but vague, or only reports on this week rather than the feature's overall state. | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** same as every other week — I check the link before grading.

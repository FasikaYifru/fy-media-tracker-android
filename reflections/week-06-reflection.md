# Week-06 Reflection

**Name: Fasika Yifru**

**Date: 06-28-26**

---

## Commits This Week

**Link: https://github.com/FasikaYifru/fy-media-tracker-android/pull/7**

---

## Code Review

**Reviewed:** 
*Fuchee Young*

**Link to my review: https://github.com/fucheeyoung-blip/media-tracker-android/pull/7**

### What I Looked At

This week the PR was focused on the SearchResultsScreen implementation. I looked at how the ViewModel
manages search state and filtering by media type, how the screen was structured with the search bar and
filter chips, and how results were being displayed. I also looked at how the search query was being
passed from the SearchScreen to the SearchResultsScreen through navigation and how the ViewModel was
being initialized with the initial query via `LaunchedEffect`.

### What I Noticed

`loadNextPage()` was missing from the ViewModel. Both `search()` and `onTypeSelect()` depend on it to
actually fetch results and handle pagination. Without it the results list will always be empty when
hitting the real API. The fake data in `applyFilter()` masks this since it bypasses the network call
entirely.

### Comments I Left

`loadNextPage()` is missing from the ViewModel `search()` and `onTypeSelect()` both depend on it to
actually fetch results and handle pagination. Right now the results list will always be empty when hitting
the real API. The fake data in `applyFilter()` is masking this since it bypasses the network call entirely.

---

## One Thing I Understood More Deeply

I wasn't in class on 6/25 so I went through the open PRs and played around with the app to get a
understanding of what was covered that session.

This week working through the pagination and infinite scroll implementation gave me a much clearer picture
of how cursor-based pagination works compared to page number pagination. Reading the next cursor and
`hasMore` flag from custom response headers rather than the response body was something I hadn't seen
before. The `derivedStateOf` pattern in the `SearchResultsScreen` also clicked for me using it to
watch the `LazyListState` and only trigger `loadNextPage()` when the user is within 5 items of the end
means the next page starts loading before they actually hit the bottom, which makes the experience feel
seamless. Without `derivedStateOf` that check would recompose on every scroll event which would be a
performance problem.

---

## One Thing I'm Still Confused About

The `AuthInterceptor` uses `runBlocking` to read the access token from `SessionRepository` on the OkHttp
thread. I understand why it's there `getAccessToken()` is a suspend function and the interceptor's
`intercept` method isn't a coroutine but I'm not sure if `runBlocking` on a network thread is safe or
if it can cause issues under certain conditions like if the DataStore read takes longer than expected or
if there are multiple requests in flight at the same time. I've seen warnings about `runBlocking` being
a bad practice in production code but I don't yet know what the right alternative looks like in this
specific context.

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

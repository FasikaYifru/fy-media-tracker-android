# Week-08 Reflection

**Name: Fasika Yifru**

**Date: 07-09-26**

---

## Commits This Week

**Link: https://github.com/FasikaYifru/fy-media-tracker-android/pull/11**


---

## Code Review

**Reviewed:**
*Fuchee Young*

**Link to my review: https://github.com/fucheeyoung-blip/media-tracker-android/pull/9**

### What I Looked At

This week's PR was focused on the `MediaDetailRepository` and its supporting API service. I looked at how
the repository was structured, how the API calls were defined, and how errors were handled across the
three methods: `getMedia`, `getLibraryStatus`, and `getReviews`.

### What I Noticed

Three things stood out. First, the `getReviews` endpoint uses `@Query` instead of `@Path`, which means
the URL resolves to `reviews?mediaId=123` rather than `reviews/123`. The other two endpoints use `@Path`
for consistency, and a path-based approach would match that pattern better. Second, the repository has a
variable named `api` for the Retrofit service instance — that name is generic enough to cause confusion
for anyone maintaining this code later, since `api` could mean anything. Third, the retry logic in
`getMedia` was a thoughtful addition for handling transient server errors.

### Comments I Left

- "I like this implementation of a retry function if there was any server issues loading the media details"
- "This function looks like it takes in a query instead a path value so it would look like
  reviews?mediaId=123 which I don't think this needs to be a query it can be a path to match the other
  api calls"
- "I would be careful naming a var api like that. could cause confusion for future maintenance of this
  code"

---

## One Thing I Understood More Deeply

This week I wired up a new API endpoint to fetch media details by ID. I added `@GET("media/{id}")` to
`MediaApiService` with a `@Path("id")` parameter, then added `getMediaById(id: Int): Media?` to
`DefaultMediaRepository` which calls the endpoint and returns the body or null on failure. Finally,
`MediaDetailViewModel` calls that method inside a coroutine launched from `setMediaId()` and updates
a `MutableStateFlow<DetailUiState>` with the result. What clicked for me was how each layer has one
job — the API service defines the contract, the repository handles the HTTP call and error handling,
and the ViewModel owns the UI state. Adding a new endpoint meant touching each layer in a small,
predictable way rather than making one big change in one place.

---

## One Thing I'm Still Confused About

The `DefaultMediaDetailRepository` is instantiated with a `SessionRepository` passed directly into the
constructor, which then gets handed to `RetrofitInstance.mediaApiService()`. I'm not sure what the right
pattern is for managing the lifecycle of this repository — specifically, whether it should be created
fresh each time a ViewModel is initialized or whether it should be a singleton scoped to the application.
Creating it fresh each time means a new `OkHttpClient` and `Retrofit` instance per ViewModel, which seems
wasteful. But making it a singleton means the `SessionRepository` reference needs to stay valid for the
app's lifetime.

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

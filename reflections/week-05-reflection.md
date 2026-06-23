# Week-05 Reflection

**Name: Fasika Yifru**

**Date: 06-18-26**

---

## Commits This Week

**Link: https://github.com/FasikaYifru/fy-media-tracker-android/pull/6**

---

## Code Review

**Reviewed:** *Abdulahi Hassan*

**Link to my review: https://github.com/dmarsh31/media-tracker-android/pull/7**

### What I Looked At

This week the PR was focused on API integration for authentication. I looked at how the network layer
was structured, specifically the Retrofit service interface, the request and response models, and how
the repository was wiring everything together to make actual calls to the backend.

### What I Noticed

The separation between the API service interface and the repository implementation was clean. Keeping
the Retrofit interface thin and pushing the response handling logic into the repository is the right
approach since it keeps the network layer easy to swap out and the repository responsible for translating
HTTP responses into domain results the ViewModel can act on.

### Comments I Left

Left a comment flagging that logging full request and response bodies in production is a potential
security risk since sensitive fields like passwords and tokens can end up in device logs. Also suggested
adding more targeted logs at the response level to make it easier to identify what's causing failures
without exposing the full payload.

---

## One Thing I Understood More Deeply

This week working through `DefaultUserRepository`, `RetrofitInstance`, and `UserApiService` gave me a
clearer picture of how Retrofit, OkHttp, and kotlinx.serialization all chain together. Setting up the
`Json` instance with `ignoreUnknownKeys` and passing it through `asConverterFactory` to Retrofit means
the serialization config applies to every request and response automatically. I also ran into a real
debugging situation where the server was returning 400 because the JSON keys didn't match what the
backend expected, which showed me how important it is to verify the actual request body using the OkHttp
logging interceptor rather than assuming the serialization is working correctly.

---

## One Thing I'm Still Confused About

The `SessionRepository` and `DefaultSessionRepository` pattern is something I'm still working through.
I understand the idea of persisting a token with DataStore so the user stays logged in across sessions,
but I'm not clear on where the token should live during a session versus between sessions, and how the
app should decide at startup whether to go to the login screen or straight to the main content based on
whether a valid token exists. I can see the pieces but I'm not sure how they're supposed to connect yet.

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

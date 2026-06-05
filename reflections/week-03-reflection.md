# Week-03 Reflection

**Name: Fasika Yifru**
**Date:06-04-26**

---

## Commits This Week

**Link:https://github.com/FasikaYifru/fy-media-tracker-android/pull/4**

---

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed:** *Fuchee Young*
**Link to my review: https://github.com/fucheeyoung-blip/media-tracker-android/pull/4#pullrequestreview-4433403523**

### What I Looked At

This week the PR was focused on API integration, specifically the setup of the `UserRepository` and the network layer.
I focused on how the API service interface was structured, how requests were being built, and how Retrofit was
being configured to communicate with the backend. This was directly relevant to the work we were doing this
week on our own implementations so it was a good comparison point.

### What I Noticed

Fuchee had added some security-conscious functionality that I thought was worth calling out. The implementation
showed awareness of how sensitive data like credentials should be handled when making requests rather than
just getting the happy path working and moving on. It was a good sign to see that consideration being baked
in early rather than treated as an afterthought. The overall structure of the Retrofit setup and how the
interface functions mapped to actual API endpoints was clean and easy to follow.

### Comments I Left

Left positive feedback on the overall implementation and specifically called out the security-related functionality
that was added. Mentioned that it was good to see those considerations built in from the start since handling
credentials carelessly at the network layer is a common issue that causes problems later.

---

## One Thing I Understood More Deeply

Working through the `UserRepository` and `ApiService` interface this week gave me a much clearer picture of
how Kotlin interfaces work in practice. The fact that Retrofit takes that interface and
generates the actual implementation at runtime is something I hadn't seen done quite like that before. In a
Java app I was used to writing the HTTP logic manually or using a more verbose setup, so seeing how much
Retrofit handles just from an annotated interface was genuinely interesting.

---

## One Thing I'm Still Confused About

Client/server communication as a whole is still something I'm trying to wrap my head around in the context
of Android specifically. I understand the basic request/response cycle but I'm uncertain about how the app
is going to manage state that comes back from the server. For example where the token gets stored after login,
how it gets attached to future requests, and how the app recovers if the token expires or a request fails
mid-session. I'm curious to see how that gets handled as the class progresses because it feels like there's
a lot of moving pieces that need to be coordinated between the ViewModel, the repository, and the network layer.

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

# Week-04 Reflection

**Name: Fasika Yifru**

**Date: 06-11-26**

---

## Commits This Week

**Link: https://github.com/FasikaYifru/fy-media-tracker-android/pull/5**

---

## Code Review

**Reviewed:** 

*Fuchee Young*
**Link to my review: https://github.com/fucheeyoung-blip/media-tracker-android/pull/5**

### What I Looked At

This week the PR was focused on the RegisterScreen implementation, specifically how the ViewModel manages
state for each input field. I looked at how the fields were bound to state, how the ViewModel was being
instantiated from the composable, and how validation was being handled before any registration logic runs.

### What I Noticed

The null checks and blank field validation were handled well. Every field was being checked before allowing
the registration flow to proceed, which is the right approach since skipping that kind of guard tends to
cause silent failures or confusing behavior for the user. It was also good to see the validation logic
living in the ViewModel rather than being scattered across the UI layer, which keeps the composable focused
on display and the ViewModel responsible for state and decisions.

### Comments I Left

Left positive feedback on the field validation, noting that checking all values exist before proceeding
is the correct approach and that it was implemented cleanly. Also called out the use of visible state
feedback to signal to the user that validation is actively being enforced, which is good UX practice
rather than just silently blocking submission.

---

## One Thing I Understood More Deeply

Working on the RegisterScreen this week gave me a much clearer understanding of how state hoisting works
in practice with a ViewModel. Having each field backed by its own `MutableStateFlow` and exposing it as
a `StateFlow` to the composable makes the data flow explicit and one-directional. I also ran into the
issue where the default `viewModel()` factory tries to call a no-arg constructor and crashes when the
ViewModel takes a dependency, which made me understand why keeping dependencies internal to the ViewModel
rather than injected through the constructor matters for how Compose resolves ViewModels without a
dependency injection framework in place.

---

## One Thing I'm Still Confused About

I'm still uncertain about the right boundary between what belongs in the ViewModel and what belongs in
the composable. For example the `focusManager.clearFocus()` call feels like a UI concern but it's being
triggered at the same time as ViewModel logic. I'm not sure how teams typically coordinate that, whether
the ViewModel signals back through state that the UI observes and then reacts to, or whether it's
acceptable to just call both from the onClick directly. As the screens get more complex I can see that
boundary becoming harder to manage.

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

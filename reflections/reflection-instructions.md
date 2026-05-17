# Weekly Reflection — Instructions

Each week you submit a short written reflection along with your code. This document explains what the reflection is, how to fill it out, where to put it, and how it is graded.

---

## What It Is

The reflection is not a status report. "I worked on the login screen" is not a reflection. The goal is to push you to articulate something you actually understood or struggled with that week — which is one of the most effective things you can do to consolidate learning.

It also gives me signal. If half the class writes the same thing in the "still confused" section, I know what to cover at the start of the next class.

The code review section has a second purpose: it builds the habit of reading other people's code carefully and communicating about it clearly. That skill is as important as anything you will write yourself this semester.

---

## File Naming

Your reflection file must be named **exactly** as follows:

```
week-01-reflection.md
week-02-reflection.md
week-03-reflection.md
...
week-14-reflection.md
```

Use two digits for the week number. `week-1-reflection.md` is wrong. `Week-01-Reflection.md` is wrong. `reflection-week-01.md` is wrong.

My grading process relies on these filenames. A file with the wrong name will not be found by my grading scripts, and that week's reflection score will be a zero. I will not hunt for misnamed files.

---

## Where to Put It

Your project repository has a `reflections/` folder at the root. Your reflection file goes there:

```
reflections/
  week-01-reflection.md
  week-02-reflection.md
  ...
```

It is submitted as part of your weekly pull request — the same PR that contains your code for that week.

---

## Branch Naming

Your weekly branch must be named **exactly** as follows:

```
week-01
week-02
week-03
...
week-14
```

Again: two digits, lowercase, hyphenated. `week-1`, `Week-01`, `wk-01`, and `week01` are all wrong.

The branch name determines which PR I look at for grading. A branch with the wrong name means I cannot find your work. That is a problem for both of us.

Following naming conventions is part of what it means to work on a team. Code review tools, CI systems, and grading scripts all depend on consistent, predictable names. Getting this right is part of your grade.

---

## Branch and File Naming Are Graded

Each week, 5 points of your reflection grade come from correct naming:

- Correct branch name: 2 points
- Correct reflection filename in the `reflections/` folder: 2 points
- Reflection submitted inside the weekly PR (not a separate PR): 1 point

These points are awarded automatically based on what I find. There is no partial credit for "close."

---

## How to Fill Out the Template

Copy `reflection-template.md` to `reflections/week-XX-reflection.md`, fill it in, and commit it with your other changes.

**Commits This Week** — paste a link to your commits on GitHub. Go to your repo, click Commits, filter to your branch or your name, and copy the URL. See the GitHub reference for step-by-step instructions.

**Code Review** — fill in your pod mate's name, paste a link to your GitHub review, and write the three subsections honestly. Your review must exist on GitHub before you submit — I verify the link.

**One Thing I Understood More Deeply** — be specific. "I understand ViewModels better" is not specific. "I realized that `collectAsState()` is what bridges the ViewModel's Flow to the Composable's recomposition — without it, the UI wouldn't react to state changes" is specific. What specifically clicked? What were you confused about before? How would you explain it to someone else?

**One Thing I'm Still Confused About** — be honest. This section helps me more than any other. You will not lose points for being confused.

**Anything Else** — optional. Use it if something happened that week worth noting.

---

## Rubric

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused." Shows genuine thinking — not just "I learned X." | Responses present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link verified on GitHub. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Naming & Submission** | 5 | Correct branch name, correct filename, reflection in the weekly PR. | — | Automatic: wrong name = 0 for that component. |
| **Total** | **25** | | | |

**On the code review score:** I check that the review actually exists on GitHub before grading. Your written summary here and your GitHub comment should match. If the review is not there, the written summary earns no credit.

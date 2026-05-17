package edu.metrostate.ics342.mediatracker.ui.review

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

// ── STUB — Students build this in Week 8 ─────────────────────────────────────
//
// Week 8 task: Build the Write Review screen.
//   1. Show a media summary at the top (cover thumbnail + title).
//   2. Build a StarRatingRow composable: 5 tappable stars, tapping star N sets rating = N.
//      Extract it as a reusable composable with (rating: Int, onRatingChange: (Int) -> Unit).
//   3. Add a multiline text field (max 500 chars) with a character counter.
//   4. Add a "Share to activity feed" checkbox (checked by default).
//   5. Disable "Post Review" until at least one star is selected.
//   6. Wire to POST /reviews on submit; navigate back on success.
@Composable
fun WriteReviewScreen(
    mediaId: Int,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Write Review is not implemented yet.\n(mediaId = $mediaId)",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

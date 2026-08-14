package edu.metrostate.ics342.mediatracker.ui.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

private const val MAX_REVIEW_LENGTH = 500

// ── Reusable star rating component ───────────────────────────────────────────
// Tapping star N always sets rating = N (never toggles or gates on current value).
@Composable
fun StarRatingRow(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (star in 1..5) {
            val filled = star <= rating
            Icon(
                imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = "$star star${if (star == 1) "" else "s"}",
                tint = if (filled) MaterialTheme.colorScheme.tertiary
                       else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onRatingChange(star) }
            )
        }
    }
}

// ── Screen ────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteReviewScreen(
    mediaId: Int,
    reviewId: Int? = null,
    onNavigateBack: () -> Unit,
    viewModel: WriteReviewViewModel = viewModel()
) {
    // Kick off pre-population if we're editing an existing review.
    LaunchedEffect(reviewId) {
        if (reviewId != null) viewModel.loadForEdit(mediaId, reviewId)
    }

    var rating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    var shareToFeed by remember { mutableStateOf(true) }
    var formInitialised by remember { mutableStateOf(reviewId == null) }

    val submitState by viewModel.submitState.collectAsState()
    val prefill     by viewModel.prefill.collectAsState()

    // Apply pre-fill exactly once when it arrives from the ViewModel.
    LaunchedEffect(prefill) {
        if (!formInitialised && prefill != null) {
            rating      = prefill!!.rating
            reviewText  = prefill!!.reviewText
            shareToFeed = prefill!!.shareToFeed
            formInitialised = true
        }
    }

    // Navigate back as soon as a successful submit lands.
    LaunchedEffect(submitState) {
        if (submitState is WriteReviewViewModel.SubmitUiState.Success) {
            viewModel.resetState()
            onNavigateBack()
        }
    }

    val isEditing = reviewId != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Review" else "Write Review", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Star rating
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "Your rating",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                StarRatingRow(
                    rating = rating,
                    onRatingChange = { rating = it }
                )
            }

            // Review text + character counter
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { if (it.length <= MAX_REVIEW_LENGTH) reviewText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    label = { Text("Review (optional)") },
                    placeholder = { Text("What did you think?") },
                    maxLines = 8
                )
                Text(
                    text = "${reviewText.length} / $MAX_REVIEW_LENGTH",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (reviewText.length >= MAX_REVIEW_LENGTH)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            // Share to feed toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Share to activity feed", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "Followers will see this review",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = shareToFeed,
                    onCheckedChange = { shareToFeed = it }
                )
            }

            // Error / status messages
            when (val state = submitState) {
                is WriteReviewViewModel.SubmitUiState.AlreadyReviewed ->
                    Text(
                        "You've already reviewed this.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                is WriteReviewViewModel.SubmitUiState.NetworkError ->
                    Text(
                        "Network error — please try again.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                is WriteReviewViewModel.SubmitUiState.Error ->
                    Text(
                        state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                else -> Unit
            }

            // Submit button — disabled until a star is selected
            val isLoading = submitState is WriteReviewViewModel.SubmitUiState.Loading
            Button(
                onClick = {
                    viewModel.submit(
                        mediaId = mediaId,
                        rating = rating,
                        reviewText = reviewText,
                        shareToFeed = shareToFeed
                    )
                },
                enabled = rating >= 1 && !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(if (isEditing) "Update Review" else "Post Review")
            }
        }
    }
}

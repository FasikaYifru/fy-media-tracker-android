package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Review
import edu.metrostate.ics342.mediatracker.theme.MovieContainer
import edu.metrostate.ics342.mediatracker.theme.OnMovieContainer

@Composable
fun MediaDetailScreen(
    mediaId: Int,
    onNavigateBack: () -> Unit,
    onWriteReview: (Int, Int?) -> Unit,
    viewModel: MediaDetailViewModel = viewModel()
) {
    LaunchedEffect(mediaId) { viewModel.load(mediaId) }

    val uiState       by viewModel.uiState.collectAsState()
    val reviewsState  by viewModel.reviewsUiState.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()
    val deleteState   by viewModel.deleteState.collectAsState()

    LaunchedEffect(deleteState) {
        if (deleteState is DeleteReviewUiState.Success) viewModel.resetDeleteState()
    }

    when (val state = uiState) {
        is MediaDetailUiState.Loading -> Box(
            Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        is MediaDetailUiState.NotFound -> Box(
            Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) { Text("Media not found") }

        is MediaDetailUiState.Error -> Box(
            Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) { Text(state.message) }

        is MediaDetailUiState.Success -> MediaDetailBody(
            m                 = state.detail,
            isInLibrary       = state.libraryStatus != null,
            isAddingToLibrary = state.isAddingToLibrary,
            isFavorited       = state.isFavorited,
            reviewsState      = reviewsState,
            currentUserId     = currentUserId,
            deleteState       = deleteState,
            onWantTo          = viewModel::addToLibrary,
            onSave            = viewModel::onSave,
            onWriteReview     = onWriteReview,
            onDeleteReview    = viewModel::deleteReview
        )
    }
}

@Composable
private fun MediaDetailBody(
    m: Media,
    isInLibrary: Boolean,
    isAddingToLibrary: Boolean,
    isFavorited: Boolean,
    reviewsState: ReviewsUiState,
    currentUserId: String?,
    deleteState: DeleteReviewUiState,
    onWantTo: () -> Unit,
    onSave: () -> Unit,
    onWriteReview: (Int, Int?) -> Unit,
    onDeleteReview: (Int) -> Unit
) {
    val allReviews = (reviewsState as? ReviewsUiState.Success)?.reviews ?: emptyList()
    val hasReviewed = allReviews.any { it.userId == currentUserId }

    var reviewToDelete by remember { mutableStateOf<Review?>(null) }

    if (reviewToDelete != null) {
        AlertDialog(
            onDismissRequest = { reviewToDelete = null },
            title   = { Text("Delete review?") },
            text    = { Text("This will permanently remove your review.") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteReview(reviewToDelete!!.id)
                    reviewToDelete = null
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { reviewToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            MediaCover(m)
            Text(m.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            val credit = when (m.mediaType) {
                "book"  -> m.author
                "movie" -> m.director
                "show"  -> m.creator
                else    -> null
            }
            credit?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "★".repeat(m.averageRating.toInt()) + " ${"%.1f".format(m.averageRating)}",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text("(${m.ratingCount})", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
        }

        // ── Action buttons ────────────────────────────────────────────────────
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick  = onWantTo,
                enabled  = !isInLibrary && !isAddingToLibrary,
                modifier = Modifier.weight(1f),
                shape    = RoundedCornerShape(20.dp)
            ) {
                if (isAddingToLibrary) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (isInLibrary) "✓ In Library" else "+ Want To", fontSize = 13.sp)
                }
            }
            OutlinedButton(
                onClick  = onSave,
                enabled  = !isFavorited,
                modifier = Modifier.weight(1f),
                shape    = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(if (isFavorited) "Saved" else "Save", fontSize = 13.sp)
            }
        }

        // ── About ─────────────────────────────────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("About", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            m.description?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)
            }
        }

        // ── Stat grid ─────────────────────────────────────────────────────────
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            m.publishedYear?.let {
                Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Year", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Text("$it", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            m.pageCount?.let {
                Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Pages", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Text("$it", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            m.runtimeMinutes?.let {
                Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Runtime", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Text("${it}m", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            if (m.genres.isNotEmpty()) {
                Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Genre", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Text(m.genres.first(), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // ── Reviews ───────────────────────────────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val count = (reviewsState as? ReviewsUiState.Success)?.reviews?.size ?: 0
                Text(
                    "Reviews${if (count > 0) " ($count)" else ""}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Hide "Write a Review" when the user already has one.
                if (!hasReviewed) {
                    TextButton(onClick = { onWriteReview(m.id, null) }) {
                        Text("+ Write Review", fontSize = 13.sp)
                    }
                }
            }

            if (deleteState is DeleteReviewUiState.Error) {
                Text(
                    (deleteState as DeleteReviewUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            when (reviewsState) {
                is ReviewsUiState.Loading ->
                    Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    }

                is ReviewsUiState.Empty ->
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Be the first to review this.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = { onWriteReview(m.id, null) }) {
                            Text("Write a Review")
                        }
                    }

                is ReviewsUiState.Error ->
                    Text(
                        reviewsState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                is ReviewsUiState.Success ->
                    reviewsState.reviews.forEach { review ->
                        ReviewCard(
                            review        = review,
                            isOwnReview   = review.userId == currentUserId,
                            isDeleting    = deleteState is DeleteReviewUiState.Loading,
                            onEditClick   = { onWriteReview(m.id, review.id) },
                            onDeleteClick = { reviewToDelete = review }
                        )
                    }
            }
        }
    }
}

@Composable
private fun ReviewCard(
    review: Review,
    isOwnReview: Boolean,
    isDeleting: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                val initial = review.user?.username?.firstOrNull()?.uppercaseChar() ?: '?'
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$initial",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            "@${review.user?.username ?: "unknown"}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            review.createdAt,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text("★".repeat(review.rating), color = MaterialTheme.colorScheme.tertiary, fontSize = 12.sp)
                    review.reviewText?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            if (isOwnReview) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onEditClick, enabled = !isDeleting) {
                        Text("Edit", fontSize = 12.sp)
                    }
                    TextButton(onClick = onDeleteClick, enabled = !isDeleting) {
                        Text("Delete", fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaCover(m: Media) {
    val containerColor = when (m.mediaType) {
        "book"  -> MaterialTheme.colorScheme.primaryContainer
        "movie" -> MovieContainer
        else    -> MaterialTheme.colorScheme.secondaryContainer
    }
    val iconTint = when (m.mediaType) {
        "book"  -> MaterialTheme.colorScheme.onPrimaryContainer
        "movie" -> OnMovieContainer
        else    -> MaterialTheme.colorScheme.onSecondaryContainer
    }
    val placeholder = when (m.mediaType) {
        "book"  -> R.drawable.menu_book_24px
        "movie" -> R.drawable.movie_24px
        else    -> R.drawable.tv_24px
    }

    Box(
        modifier = Modifier
            .size(width = 110.dp, height = 160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        if (m.coverUrl != null) {
            AsyncImage(
                model = m.coverUrl,
                contentDescription = m.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                painter = painterResource(placeholder),
                contentDescription = null,
                modifier = Modifier.size(52.dp),
                tint = iconTint
            )
        }
    }
}

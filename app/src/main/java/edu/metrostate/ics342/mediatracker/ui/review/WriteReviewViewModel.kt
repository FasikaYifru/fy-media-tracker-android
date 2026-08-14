package edu.metrostate.ics342.mediatracker.ui.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.datastore.DefaultSessionRepository
import edu.metrostate.ics342.mediatracker.data.model.Review
import edu.metrostate.ics342.mediatracker.data.network.DefaultReviewRepository
import edu.metrostate.ics342.mediatracker.data.network.ReviewResult
import edu.metrostate.ics342.mediatracker.data.network.ReviewsListResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WriteReviewViewModel(application: Application) : AndroidViewModel(application) {

    sealed class SubmitUiState {
        object Idle : SubmitUiState()
        object Loading : SubmitUiState()
        data class Success(val review: Review) : SubmitUiState()
        object AlreadyReviewed : SubmitUiState()
        object NetworkError : SubmitUiState()
        data class Error(val message: String) : SubmitUiState()
    }

    // Pre-populate state emitted once when editing an existing review.
    data class EditPrefill(val rating: Int, val reviewText: String, val shareToFeed: Boolean)

    private val sessionRepository = DefaultSessionRepository(application)
    private val reviewRepository  = DefaultReviewRepository(sessionRepository)

    private val _submitState = MutableStateFlow<SubmitUiState>(SubmitUiState.Idle)
    val submitState: StateFlow<SubmitUiState> = _submitState.asStateFlow()

    private val _prefill = MutableStateFlow<EditPrefill?>(null)
    val prefill: StateFlow<EditPrefill?> = _prefill.asStateFlow()

    private var editingReviewId: Int? = null

    /**
     * Call once when the screen opens with a non-null reviewId.
     * Fetches the review list for the media item and finds the matching review
     * so the form can be pre-populated.
     */
    fun loadForEdit(mediaId: Int, reviewId: Int) {
        if (editingReviewId == reviewId) return  // already initialised
        editingReviewId = reviewId
        viewModelScope.launch {
            when (val result = reviewRepository.getReviews(mediaId)) {
                is ReviewsListResult.Success -> {
                    val review = result.reviews.firstOrNull { it.id == reviewId }
                    if (review != null) {
                        _prefill.value = EditPrefill(
                            rating      = review.rating,
                            reviewText  = review.reviewText ?: "",
                            shareToFeed = review.shareToFeed
                        )
                    }
                }
                else -> { /* silently ignore — form just starts empty */ }
            }
        }
    }

    fun submit(
        mediaId: Int,
        rating: Int,
        reviewText: String,
        shareToFeed: Boolean
    ) {
        if (rating < 1) return
        viewModelScope.launch {
            _submitState.value = SubmitUiState.Loading
            val result = if (editingReviewId != null) {
                reviewRepository.updateReview(
                    reviewId    = editingReviewId!!,
                    mediaId     = mediaId,
                    rating      = rating,
                    reviewText  = reviewText,
                    shareToFeed = shareToFeed
                )
            } else {
                reviewRepository.createReview(
                    mediaId     = mediaId,
                    rating      = rating,
                    reviewText  = reviewText,
                    shareToFeed = shareToFeed
                )
            }
            _submitState.value = when (result) {
                is ReviewResult.Success        -> SubmitUiState.Success(result.review)
                is ReviewResult.AlreadyReviewed -> SubmitUiState.AlreadyReviewed
                is ReviewResult.NetworkError   -> SubmitUiState.NetworkError
                is ReviewResult.NotFound       -> SubmitUiState.Error("Review not found")
                is ReviewResult.UnknownError   -> SubmitUiState.Error("Error ${result.code}")
            }
        }
    }

    fun resetState() {
        _submitState.value = SubmitUiState.Idle
    }
}

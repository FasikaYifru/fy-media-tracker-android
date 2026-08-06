package edu.metrostate.ics342.mediatracker.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.datastore.DefaultSessionRepository
import edu.metrostate.ics342.mediatracker.data.model.Favorite
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.MediaNotFoundException
import edu.metrostate.ics342.mediatracker.data.model.Review
import edu.metrostate.ics342.mediatracker.data.network.DefaultMediaRepository
import edu.metrostate.ics342.mediatracker.data.network.DefaultReviewRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ── Top-level UI state for the whole screen ───────────────────────────────────
sealed class MediaDetailUiState {
    object Loading : MediaDetailUiState()
    object NotFound : MediaDetailUiState()
    data class Error(val message: String) : MediaDetailUiState()
    data class Success(
        val detail: Media,
        val libraryStatus: LibraryStatus?,
        val isFavorited: Boolean = false,
        val isAddingToLibrary: Boolean = false
    ) : MediaDetailUiState()
}

// ── Reviews sub-state ─────────────────────────────────────────────────────────
sealed class ReviewsUiState {
    object Loading : ReviewsUiState()
    object Empty : ReviewsUiState()
    data class Success(val reviews: List<Review>) : ReviewsUiState()
    data class Error(val message: String) : ReviewsUiState()
}

// ── Delete sub-state ──────────────────────────────────────────────────────────
sealed class DeleteReviewUiState {
    object Idle : DeleteReviewUiState()
    object Loading : DeleteReviewUiState()
    object Success : DeleteReviewUiState()
    data class Error(val message: String) : DeleteReviewUiState()
}

class MediaDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionRepository = DefaultSessionRepository(application)
    private val repository        = DefaultMediaRepository(sessionRepository)
    private val reviewRepository  = DefaultReviewRepository(sessionRepository)

    private val _uiState = MutableStateFlow<MediaDetailUiState>(MediaDetailUiState.Loading)
    val uiState: StateFlow<MediaDetailUiState> = _uiState.asStateFlow()

    private val _reviewsUiState = MutableStateFlow<ReviewsUiState>(ReviewsUiState.Loading)
    val reviewsUiState: StateFlow<ReviewsUiState> = _reviewsUiState.asStateFlow()

    private val _deleteState = MutableStateFlow<DeleteReviewUiState>(DeleteReviewUiState.Idle)
    val deleteState: StateFlow<DeleteReviewUiState> = _deleteState.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    private var currentMediaId: Int? = null

    // ── Initial load ──────────────────────────────────────────────────────────
    fun load(mediaId: Int) {
        currentMediaId = mediaId
        _uiState.value = MediaDetailUiState.Loading
        _reviewsUiState.value = ReviewsUiState.Loading

        viewModelScope.launch {
            // Grab the current user id for the review ownership check.
            _currentUserId.value = sessionRepository.getUser()?.id

            supervisorScope {
                val detailDeferred   = async { repository.getMediaById(mediaId) }
                val libraryDeferred  = async { runCatching { repository.getLibraryStatus(mediaId) }.getOrNull() }
                val favoriteDeferred = async { runCatching { repository.getFavoriteStatus(mediaId) }.getOrNull() }

                val detail = try {
                    detailDeferred.await()
                } catch (e: MediaNotFoundException) {
                    libraryDeferred.cancel(); favoriteDeferred.cancel()
                    _uiState.value = MediaDetailUiState.NotFound
                    return@supervisorScope
                } catch (e: Exception) {
                    libraryDeferred.cancel(); favoriteDeferred.cancel()
                    _uiState.value = MediaDetailUiState.Error(e.message ?: "Unknown error")
                    return@supervisorScope
                }

                if (detail == null) {
                    libraryDeferred.cancel(); favoriteDeferred.cancel()
                    _uiState.value = MediaDetailUiState.NotFound
                    return@supervisorScope
                }

                _uiState.value = MediaDetailUiState.Success(
                    detail        = detail,
                    libraryStatus = libraryDeferred.await()?.status,
                    isFavorited   = favoriteDeferred.await() != null
                )
            }

            // Load reviews independently so a review failure never blocks the main detail.
            loadReviews(mediaId)
        }
    }

    // ── Library ───────────────────────────────────────────────────────────────
    fun addToLibrary() {
        val current = _uiState.value as? MediaDetailUiState.Success ?: return
        val mediaId = currentMediaId ?: return
        if (current.isAddingToLibrary) return
        _uiState.value = current.copy(libraryStatus = LibraryStatus.WANT_TO, isAddingToLibrary = true)
        viewModelScope.launch {
            try {
                val item = repository.addToLibrary(mediaId, LibraryStatus.WANT_TO)
                val updated = _uiState.value as? MediaDetailUiState.Success ?: return@launch
                _uiState.value = updated.copy(libraryStatus = item.status, isAddingToLibrary = false)
            } catch (e: Exception) {
                val updated = _uiState.value as? MediaDetailUiState.Success ?: return@launch
                _uiState.value = updated.copy(libraryStatus = null, isAddingToLibrary = false)
            }
        }
    }

    // ── Favorites ─────────────────────────────────────────────────────────────
    fun onSave() {
        val current = _uiState.value as? MediaDetailUiState.Success ?: return
        val mediaId = currentMediaId ?: return
        val wasAlreadyFavorited = current.isFavorited
        _uiState.value = current.copy(isFavorited = !wasAlreadyFavorited)
        viewModelScope.launch {
            try {
                if (wasAlreadyFavorited) repository.removeFromFavorites(mediaId)
                else repository.addToFavorites(mediaId)
            } catch (e: Exception) {
                val updated = _uiState.value as? MediaDetailUiState.Success ?: return@launch
                _uiState.value = updated.copy(isFavorited = wasAlreadyFavorited)
            }
        }
    }

    // ── Reviews ───────────────────────────────────────────────────────────────
    fun refreshReviews() {
        val mediaId = currentMediaId ?: return
        viewModelScope.launch { loadReviews(mediaId) }
    }

    private suspend fun loadReviews(mediaId: Int) {
        _reviewsUiState.value = ReviewsUiState.Loading
        try {
            val raw = repository.getReviews(mediaId)
            val currentId = _currentUserId.value
            // Sort: current user's review first, then newest-first.
            val sorted = raw.sortedWith(
                compareByDescending<Review> { it.userId == currentId }
                    .thenByDescending { it.createdAt }
            )
            _reviewsUiState.value = if (sorted.isEmpty()) ReviewsUiState.Empty
                                    else ReviewsUiState.Success(sorted)
        } catch (e: Exception) {
            _reviewsUiState.value = ReviewsUiState.Error(e.message ?: "Failed to load reviews")
        }
    }

    fun deleteReview(reviewId: Int) {
        val mediaId = currentMediaId ?: return
        viewModelScope.launch {
            _deleteState.value = DeleteReviewUiState.Loading
            val success = reviewRepository.deleteReview(reviewId)
            if (success) {
                _deleteState.value = DeleteReviewUiState.Success
                loadReviews(mediaId)
            } else {
                _deleteState.value = DeleteReviewUiState.Error("Could not delete review")
            }
        }
    }

    fun resetDeleteState() {
        _deleteState.value = DeleteReviewUiState.Idle
    }
}

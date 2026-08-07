package edu.metrostate.ics342.mediatracker.data.network

import edu.metrostate.ics342.mediatracker.data.model.Review
import java.io.IOException

sealed class ReviewResult {
    data class Success(val review: Review) : ReviewResult()
    object AlreadyReviewed : ReviewResult()
    object NotFound : ReviewResult()
    object NetworkError : ReviewResult()
    data class UnknownError(val code: Int) : ReviewResult()
}

sealed class ReviewsListResult {
    data class Success(val reviews: List<Review>) : ReviewsListResult()
    object NetworkError : ReviewsListResult()
    data class UnknownError(val code: Int) : ReviewsListResult()
}

class DefaultReviewRepository(sessionRepository: SessionRepository) {

    private val api = RetrofitInstance.reviewApiService(sessionRepository)

    suspend fun getReviews(mediaId: Int): ReviewsListResult {
        return try {
            val response = api.getReviews(mediaId)
            if (response.isSuccessful) {
                ReviewsListResult.Success(response.body() ?: emptyList())
            } else {
                ReviewsListResult.UnknownError(response.code())
            }
        } catch (e: IOException) {
            ReviewsListResult.NetworkError
        }
    }

    suspend fun createReview(
        mediaId: Int,
        rating: Int,
        reviewText: String?,
        shareToFeed: Boolean
    ): ReviewResult {
        return try {
            val response = api.createReview(
                ReviewRequest(
                    mediaId = mediaId,
                    rating = rating,
                    reviewText = reviewText?.ifBlank { null },
                    shareToFeed = shareToFeed
                )
            )
            when (response.code()) {
                200, 201 -> ReviewResult.Success(response.body()!!)
                409 -> ReviewResult.AlreadyReviewed
                else -> ReviewResult.UnknownError(response.code())
            }
        } catch (e: IOException) {
            ReviewResult.NetworkError
        }
    }

    suspend fun updateReview(
        reviewId: Int,
        mediaId: Int,
        rating: Int,
        reviewText: String?,
        shareToFeed: Boolean
    ): ReviewResult {
        return try {
            val response = api.updateReview(
                id = reviewId,
                request = ReviewRequest(
                    mediaId = mediaId,
                    rating = rating,
                    reviewText = reviewText?.ifBlank { null },
                    shareToFeed = shareToFeed
                )
            )
            when (response.code()) {
                200 -> ReviewResult.Success(response.body()!!)
                404 -> ReviewResult.NotFound
                else -> ReviewResult.UnknownError(response.code())
            }
        } catch (e: IOException) {
            ReviewResult.NetworkError
        }
    }

    suspend fun deleteReview(reviewId: Int): Boolean {
        return try {
            val response = api.deleteReview(reviewId)
            response.isSuccessful
        } catch (e: IOException) {
            false
        }
    }
}

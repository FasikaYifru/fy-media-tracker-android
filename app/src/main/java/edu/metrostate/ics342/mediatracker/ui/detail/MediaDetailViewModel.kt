package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.lifecycle.ViewModel
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Review
import edu.metrostate.ics342.mediatracker.data.model.UserProfile

class MediaDetailViewModel : ViewModel() {

    val media = MOCK
    val mockReviews = MOCK_REVIEWS

    fun setMediaId(id: Int) {
        // TODO (Week 7): call GET /media/{id}
    }

    companion object {
        val MOCK = Media(
            id = 1,
            mediaType = "book",
            title = "The Pragmatic Programmer",
            author = "David Thomas & Andrew Hunt",
            coverUrl = null,
            publishedYear = 1999,
            averageRating = 4.7f,
            ratingCount = 8312,
            genres = listOf("Technology", "Software Engineering"),
            description = "A collection of tips to improve the craft of software development.",
            pageCount = 352,
            isbn = "978-0135957059",
            reviewCount = 1204
        )

        val MOCK_REVIEWS = listOf(
            Review(
                userId = "1", mediaId = 1, rating = 5,
                reviewText = "A timeless classic. Fresh every time.",
                createdAt = "2d ago",
                user = UserProfile(id = "1", username = "alice_reads", displayName = "Alice", email = "alice@example.com")
            ),
            Review(
                userId = "2", mediaId = 1, rating = 4,
                reviewText = "Great world-building, slow in the middle.",
                createdAt = "1w ago",
                user = UserProfile(id = "2", username = "bob_books", displayName = "Bob", email = "bob@example.com")
            )
        )
    }
}

package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.lifecycle.ViewModel
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Review
import edu.metrostate.ics342.mediatracker.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MediaDetailViewModel : ViewModel() {

    private val _media = MutableStateFlow(MOCK)
    val media: StateFlow<Media> = _media.asStateFlow()

    private val _reviews = MutableStateFlow(MOCK_REVIEWS)
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    fun setMediaId(id: Int) {
        // TODO (Week 7): call GET /media/{id} and update _media and _reviews
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

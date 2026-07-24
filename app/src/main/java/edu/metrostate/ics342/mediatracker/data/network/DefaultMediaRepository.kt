package edu.metrostate.ics342.mediatracker.data.network

import edu.metrostate.ics342.mediatracker.data.model.ErrorResponse
import edu.metrostate.ics342.mediatracker.data.model.Favorite
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.MediaNotFoundException
import edu.metrostate.ics342.mediatracker.data.model.Review
import kotlinx.serialization.json.Json
import retrofit2.Response

private val errorJson = Json { ignoreUnknownKeys = true }

private fun parseErrorMessage(response: Response<*>): String? = try {
    response.errorBody()?.string()?.let { errorJson.decodeFromString<ErrorResponse>(it).message }
} catch (e: Exception) { null }

data class MediaPage(
    val items: List<Media>,
    val nextCursor: String?,
    val hasMore: Boolean
)

class DefaultMediaRepository(sessionRepository: SessionRepository) {

    private val api = RetrofitInstance.mediaApiService(sessionRepository)


    suspend fun search(query: String, type: String?, after: String?): MediaPage {
        val response = api.searchMedia(
            query = query.ifBlank { null },
            type  = type?.ifBlank { null },
            after = after
        )
        val items      = response.body() ?: emptyList()
        val nextCursor = response.headers()["X-Next-Cursor"]
        val hasMore    = response.headers()["X-Has-More"] == "true"
        return MediaPage(items, nextCursor, hasMore)
    }

    suspend fun getMediaById(id: Int): Media? {
        val response = api.getMediaById(id)
        if (response.code() == 404) {
            val message = parseErrorMessage(response) ?: "Media not found"
            throw MediaNotFoundException(message)
        }
        if (!response.isSuccessful) {
            val message = parseErrorMessage(response) ?: "Failed to load media (${response.code()})"
            error(message)
        }
        return response.body() ?: error("Empty body for media detail $id")
    }

    suspend fun getLibrary(status: LibraryStatus? = null): List<LibraryItem> {
        val response = api.getLibrary(status?.toApiString())
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }

    suspend fun getLibraryStatus(mediaId: Int): LibraryItem? {
        val response = api.getLibraryStatus(mediaId)
        if (response.code() == 404) return null
        if (!response.isSuccessful) error("Failed to load library item: ${response.code()}")
        return response.body()
    }

    suspend fun addToLibrary(mediaId: Int, status: LibraryStatus): LibraryItem {
        val response = api.addToLibrary(AddToLibraryRequest(mediaId, status))
        if (!response.isSuccessful) {
            val message = parseErrorMessage(response) ?: "Failed to add to library (${response.code()})"
            error(message)
        }
        return response.body() ?: error("Empty body adding mediaId $mediaId to library")
    }

    suspend fun getFavoriteStatus(mediaId: Int): Favorite? {
        val response = api.getFavoriteStatus(mediaId)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun addToFavorites(mediaId: Int): Boolean {
        val response = api.addToFavorites(mapOf("mediaId" to mediaId))
        return response.isSuccessful || response.code() == 409
    }

    suspend fun getReviews(mediaId: Int): List<Review> {
        val response = api.getReviews(mediaId)
        if (!response.isSuccessful) return emptyList()
        return response.body() ?: emptyList()
    }
}

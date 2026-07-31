package edu.metrostate.ics342.mediatracker.data.network

import edu.metrostate.ics342.mediatracker.data.model.Favorite
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Review
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaApiService {
    @GET("media")
    suspend fun searchMedia(
        @Query("query") query: String? = null,
        @Query("type") type: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("after") after: String? = null
    ): Response<List<Media>>

    @GET("media/{id}")
    suspend fun getMediaById(@Path("id") id: Int): Response<Media>

    @GET("library")
    suspend fun getLibrary(
        @Query("status") status: String? = null
    ): Response<List<LibraryItem>>

    @GET("library/{mediaId}")
    suspend fun getLibraryStatus(@Path("mediaId") mediaId: Int): Response<LibraryItem>

    @POST("library")
    suspend fun addToLibrary(@Body body: AddToLibraryRequest): Response<LibraryItem>

    @PUT("library/{mediaId}")
    suspend fun updateLibraryStatus(
        @Path("mediaId") mediaId: Int,
        @Body body: AddToLibraryRequest
    ): Response<LibraryItem>

    @DELETE("library/{mediaId}")
    suspend fun removeFromLibrary(@Path("mediaId") mediaId: Int): Response<Unit>

    @GET("favorites/{mediaId}")
    suspend fun getFavoriteStatus(@Path("mediaId") mediaId: Int): Response<Favorite>

    @POST("favorites")
    suspend fun addToFavorites(@Body body: Map<String, Int>): Response<Favorite>

    @DELETE("favorites/{mediaId}")
    suspend fun removeFromFavorites(@Path("mediaId") mediaId: Int): Response<Unit>

    @GET("reviews")
    suspend fun getReviews(@Query("mediaId") mediaId: Int): Response<List<Review>>
}
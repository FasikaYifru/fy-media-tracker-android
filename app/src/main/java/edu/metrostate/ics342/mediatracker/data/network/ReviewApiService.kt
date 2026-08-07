package edu.metrostate.ics342.mediatracker.data.network

import edu.metrostate.ics342.mediatracker.data.model.Review
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApiService {

    @GET("reviews")
    suspend fun getReviews(
        @Query("mediaId") mediaId: Int
    ): Response<List<Review>>

    @POST("reviews")
    suspend fun createReview(
        @Body request: ReviewRequest
    ): Response<Review>

    @PUT("reviews/{id}")
    suspend fun updateReview(
        @Path("id") id: Int,
        @Body request: ReviewRequest
    ): Response<Review>

    @DELETE("reviews/{id}")
    suspend fun deleteReview(
        @Path("id") id: Int
    ): Response<Unit>
}

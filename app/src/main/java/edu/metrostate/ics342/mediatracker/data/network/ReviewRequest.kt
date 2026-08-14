package edu.metrostate.ics342.mediatracker.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequest(
    val mediaId: Int,
    val rating: Int,
    val reviewText: String? = null,
    val shareToFeed: Boolean = true
)

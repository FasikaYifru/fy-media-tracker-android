package edu.metrostate.ics342.mediatracker.ui.review

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WriteReviewViewModel : ViewModel() {
    // TODO (Week 8): Add rating StateFlow, reviewText StateFlow, shareToFeed StateFlow.
    // Wire to POST /reviews on submit.
    private val _rating = MutableStateFlow(0)
    val rating: StateFlow<Int> = _rating.asStateFlow()

    fun onRatingChange(value: Int) { _rating.value = value }
}

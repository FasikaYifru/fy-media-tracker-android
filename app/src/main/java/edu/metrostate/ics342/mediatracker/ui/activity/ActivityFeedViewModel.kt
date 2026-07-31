package edu.metrostate.ics342.mediatracker.ui.activity

import androidx.lifecycle.ViewModel
import edu.metrostate.ics342.mediatracker.data.model.ActivityEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ActivityFeedViewModel : ViewModel() {

    private val _feedItems = MutableStateFlow<List<ActivityEvent>>(emptyList())
    val feedItems: StateFlow<List<ActivityEvent>> = _feedItems.asStateFlow()

    // TODO: Wire to GET /feed when the endpoint is available
}

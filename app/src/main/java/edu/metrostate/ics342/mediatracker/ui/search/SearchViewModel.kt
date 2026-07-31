package edu.metrostate.ics342.mediatracker.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.network.DefaultMediaRepository
import edu.metrostate.ics342.mediatracker.data.datastore.DefaultSessionRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionRepository = DefaultSessionRepository(application)
    private val mediaRepository   = DefaultMediaRepository(sessionRepository)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedType = MutableStateFlow("")
    val selectedType: StateFlow<String> = _selectedType.asStateFlow()

    private val _results = MutableStateFlow<List<Media>>(emptyList())
    val results: StateFlow<List<Media>> = _results.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // Re-search whenever query or type changes, debounced to avoid hammering the API
        combine(_query, _selectedType) { q, t -> q to t }
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { (q, t) -> fetchResults(q, t) }
            .launchIn(viewModelScope)
    }

    private fun fetchResults(query: String, type: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value     = null
            try {
                val page = mediaRepository.search(
                    query = query,
                    type  = type.ifBlank { null },
                    after = null
                )
                _results.value = page.items
            } catch (e: Exception) {
                _error.value   = e.message ?: "Search failed"
                _results.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onQueryChange(value: String) { _query.value = value }
    fun clearQuery()                 { _query.value = "" }
    fun onTypeSelect(type: String)   { _selectedType.value = type }
}

package edu.metrostate.ics342.mediatracker.ui.library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.datastore.DefaultSessionRepository
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.network.DefaultMediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LibraryUiState {
    object Loading : LibraryUiState()
    object Empty : LibraryUiState()
    data class Error(val message: String) : LibraryUiState()
    data class Success(val items: List<LibraryItem>) : LibraryUiState()
}

class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private val mediaRepository = DefaultMediaRepository(DefaultSessionRepository(application))

    private val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Loading)
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    // Keep a flat list for optimistic mutations
    private val _libraryItems = MutableStateFlow<List<LibraryItem>>(emptyList())
    val libraryItems: StateFlow<List<LibraryItem>> = _libraryItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _filterStatus = MutableStateFlow(LibraryStatus.WANT_TO)
    val filterState: StateFlow<LibraryStatus> = _filterStatus.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init { loadLibrary() }

    fun loadLibrary() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val items = mediaRepository.getLibrary(_filterStatus.value)
                _libraryItems.value = items
            } catch (e: Exception) {
                _errorMessage.value = "Couldn't load library. Try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateFilter(status: LibraryStatus) {
        _filterStatus.value = status
        loadLibrary()
    }

    fun removeItem(mediaId: Int) {
        val backup = _libraryItems.value.find { it.mediaId == mediaId }
        _libraryItems.value = _libraryItems.value.filter { it.mediaId != mediaId }
        viewModelScope.launch {
            try {
                mediaRepository.removeFromLibrary(mediaId)
            } catch (e: Exception) {
                _libraryItems.value = _libraryItems.value + listOfNotNull(backup)
                _errorMessage.value = "Couldn't remove item. Try again."
            }
        }
    }

    fun updateStatus(mediaId: Int, newStatus: LibraryStatus) {
        val backup = _libraryItems.value.find { it.mediaId == mediaId }
        _libraryItems.value = _libraryItems.value.map { item ->
            if (item.mediaId == mediaId) item.copy(status = newStatus) else item
        }
        viewModelScope.launch {
            try {
                mediaRepository.updateLibraryStatus(mediaId, newStatus)
            } catch (e: Exception) {
                _libraryItems.value = _libraryItems.value.map { item ->
                    if (item.mediaId == mediaId) backup ?: item else item
                }
                _errorMessage.value = "Couldn't update status. Try again."
            }
        }
    }

    fun clearError() { _errorMessage.value = null }
}

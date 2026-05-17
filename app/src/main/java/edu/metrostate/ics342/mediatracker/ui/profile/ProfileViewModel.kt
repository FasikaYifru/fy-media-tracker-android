package edu.metrostate.ics342.mediatracker.ui.profile

import androidx.lifecycle.ViewModel
import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    private val _libraryPreview = MutableStateFlow<List<LibraryItem>>(emptyList())
    val libraryPreview: StateFlow<List<LibraryItem>> = _libraryPreview.asStateFlow()

    private val _editDisplayName = MutableStateFlow("")
    val editDisplayName: StateFlow<String> = _editDisplayName.asStateFlow()

    private val _editUsername = MutableStateFlow("")
    val editUsername: StateFlow<String> = _editUsername.asStateFlow()

    private val _editBio = MutableStateFlow("")
    val editBio: StateFlow<String> = _editBio.asStateFlow()

    init {
        _currentUser.value   = FakeMediaRepository.currentUser
        _libraryPreview.value = FakeMediaRepository.libraryItems.take(6)
        _editDisplayName.value = FakeMediaRepository.currentUser.displayName
        _editUsername.value    = FakeMediaRepository.currentUser.username
        _editBio.value         = FakeMediaRepository.currentUser.bio ?: ""
    }

    fun onEditDisplayNameChange(value: String) { _editDisplayName.value = value }
    fun onEditUsernameChange(value: String)    { _editUsername.value    = value }
    fun onEditBioChange(value: String)          { _editBio.value        = value }

    fun saveProfile() {
        // TODO (Week 10): Call PUT /users/me with Retrofit
        _currentUser.value = _currentUser.value?.copy(
            displayName = _editDisplayName.value,
            username    = _editUsername.value,
            bio         = _editBio.value.ifBlank { null }
        )
    }

    fun loadUserById(userId: String): UserProfile? {
        // TODO (Week 10): Call GET /users/{id} with Retrofit
        return FakeMediaRepository.followers.find { it.id == userId }
            ?: FakeMediaRepository.following.find { it.id == userId }
    }
}

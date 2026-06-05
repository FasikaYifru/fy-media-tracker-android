package edu.metrostate.ics342.mediatracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class  RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _email    = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    fun onUserNameChange(value: String) { _userName.value = value }

    fun onSignupClicked() {
        viewModelScope.launch {
            //TODO: Replace with actual call to repository
            userRepository.createAccount("", "","","")
        }
    }
}
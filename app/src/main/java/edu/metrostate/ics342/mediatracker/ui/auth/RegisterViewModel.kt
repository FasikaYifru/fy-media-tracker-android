package edu.metrostate.ics342.mediatracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _displayName = MutableStateFlow("")
    val displayName: StateFlow<String> = _displayName.asStateFlow()

    private val _userName = MutableStateFlow("")
    val username: StateFlow<String> = _userName.asStateFlow()
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onUserNameChange(value: String)        { _userName.value        = value }
    fun onEmailChange(value: String)           { _email.value           = value }
    fun onPasswordChange(value: String)        { _password.value        = value }
    fun onConfirmPasswordChange(value: String) { _confirmPassword.value = value }
    fun onDisplayNameChange(value: String)     { _displayName.value     = value }

    fun onSignupClicked() {
        when {
            _displayName.value.isBlank() || _email.value.isBlank() || _userName.value.isBlank() ||
            _password.value.isBlank()    || _confirmPassword.value.isBlank() -> {
                _errorMessage.value = "Please fill in all fields."
            }
            _password.value != _confirmPassword.value -> {
                _errorMessage.value = "Passwords do not match."
            }
            else -> viewModelScope.launch {
                _errorMessage.value = null
                userRepository.createAccount(_displayName.value, _userName.value, _email.value, _password.value)
            }
        }
    }
}

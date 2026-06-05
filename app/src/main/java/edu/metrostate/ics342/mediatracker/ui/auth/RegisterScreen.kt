package edu.metrostate.ics342.mediatracker.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.theme.OnPrimaryContainer
import edu.metrostate.ics342.mediatracker.theme.PrimaryContainer

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()

) {
    val password by viewModel.password.collectAsState()
    val userName by viewModel.password.collectAsState()
    val email by viewModel.email.collectAsState()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {

        Text(
            stringResource(R.string.register_logo), style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }

    Spacer(Modifier.height(8.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.smart_display),
            contentDescription = "Application Icon",
            modifier = Modifier.size(width = 64.dp, height = 64.dp)
                .background(color = PrimaryContainer, shape = RoundedCornerShape(size = 12.dp))
                .padding(12.dp),
            colorFilter = ColorFilter.tint(OnPrimaryContainer)
        )

        Text("Create Account")
        TextField(
            state = TextFieldState()
        )

        TextField(
            state = TextFieldState()
        )

        SecureTextField(
            state = TextFieldState(),
            modifier = Modifier,
            placeholder = { Text("Enter Password") }
        )

        SecureTextField(
            state = TextFieldState(),
            modifier = Modifier,
            placeholder = { Text("Confirm Password") }
        )

        TextField(
            state = TextFieldState()
        )

        TextField(
            state = TextFieldState()
        )

        //Copied over some of the login screen functionality for reference
//        OutlinedTextField(
//            value         = userName,
//            onValueChange = viewModel::onUserNameChange,
//            label         = { Text(stringResource(edu.metrostate.ics342.mediatracker.R.string.create_username)) },
//            singleLine    = true,
//            visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction    = ImeAction.Done
//            ),
//            keyboardActions = KeyboardActions(
//                onDone = { focusManager.clearFocus(); viewModel.onLoginClick() }
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value         = email,
//            onValueChange = viewModel::onEmailChange,
//            label         = { Text(stringResource(edu.metrostate.ics342.mediatracker.R.string.email_label)) },
//            singleLine    = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Email,
//                imeAction    = ImeAction.Next
//            ),
//            keyboardActions = KeyboardActions(
//                onNext = { focusManager.moveFocus(FocusDirection.Down) }
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value         = password,
//            onValueChange = viewModel::onPasswordChange,
//            label         = { Text(stringResource(edu.metrostate.ics342.mediatracker.R.string.create_new_pass)) },
//            singleLine    = true,
//            visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction    = ImeAction.Done
//            ),
//            keyboardActions = KeyboardActions(
//                onDone = { focusManager.clearFocus(); viewModel.onLoginClick() }
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value         = password,
//            onValueChange = viewModel::onPasswordChange,
//            label         = { Text(stringResource(edu.metrostate.ics342.mediatracker.R.string.confirm_pass)) },
//            singleLine    = true,
//            visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction    = ImeAction.Done
//            ),
//            keyboardActions = KeyboardActions(
//                onDone = { focusManager.clearFocus(); viewModel.onLoginClick() }
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(24.dp))

    }
}
//    @Preview
//    @Composable
//    fun RegisterScreenPreview() {
//        MaterialTheme {
//            RegisterScreen(onRegisterSuccess = {}, onNavigateToLogin = {})
//        }
//    }


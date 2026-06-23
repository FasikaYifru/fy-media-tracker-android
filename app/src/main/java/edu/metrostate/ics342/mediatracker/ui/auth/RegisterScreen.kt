package edu.metrostate.ics342.mediatracker.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val displayName by viewModel.displayName.collectAsState()
    val userName by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val focusManager = LocalFocusManager.current
    val scrollState  = rememberScrollState()
    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        if (registerState is RegisterViewModel.RegisterUiState.Success) {
            viewModel.resetRegisterState()
            onRegisterSuccess()
        }
    }

    val isLoading = registerState is RegisterViewModel.RegisterUiState.Loading
    val errorMsg  = (registerState as? RegisterViewModel.RegisterUiState.Error)
        ?.msgResId?.let { stringResource(it) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(scrollState),
        verticalArrangement   = Arrangement.Center,
        horizontalAlignment   = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.smart_display),
            contentDescription = "Application Icon",
            modifier = Modifier.size(width = 64.dp, height = 64.dp)
                .background(color = PrimaryContainer, shape = RoundedCornerShape(size = 12.dp))
                .padding(12.dp),
            colorFilter = ColorFilter.tint(OnPrimaryContainer)
        )

        Spacer(Modifier.height(10.dp))

        Text(
            stringResource(R.string.create_account),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )

        Spacer(Modifier.height(10.dp))

        Text(stringResource(R.string.community_note),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center)

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value         = displayName,
            onValueChange = viewModel::onDisplayNameChange,
            label         = { Text(stringResource(R.string.display_name)) },
            singleLine    = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction    = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value         = userName,
            onValueChange = viewModel::onUserNameChange,
            label         = { Text(stringResource(R.string.create_username)) },
            singleLine    = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction    = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value         = email,
            onValueChange = viewModel::onEmailChange,
            label         = { Text(stringResource(R.string.email_label)) },
            singleLine    = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction    = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value         = password,
            onValueChange = viewModel::onPasswordChange,
            label         = { Text(stringResource(R.string.password_label)) },
            singleLine    = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction    = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value         = confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label         = { Text(stringResource(R.string.confirm_pass)) },
            singleLine    = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction    = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMsg != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                errorMsg,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick  = { focusManager.clearFocus(); viewModel.onSignupClicked() },
            enabled  = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier    = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color       = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(R.string.sign_up_button))
            }
        }

        Spacer(Modifier.height(15.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text(stringResource(R.string.login_account))
        }
    }
}

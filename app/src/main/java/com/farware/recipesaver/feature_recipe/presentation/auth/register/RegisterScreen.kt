package com.farware.recipesaver.feature_recipe.presentation.auth.register

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.feature_recipe.presentation.components.OutlinedTextFieldWithError
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import com.farware.recipesaver.feature_recipe.presentation.util.LoadingState

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val status by viewModel.loadingState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val inputEmail = viewModel.email.value
    val inputPassword = viewModel.password.value
    val inputConfirmPassword = viewModel.confirmPassword.value

    if(viewModel.state.value.currentUser != null) {
        // set the user info
        viewModel.onEvent(RegisterEvent.UpdatePreferences(
            context = context,
            displayEmail = viewModel.state.value.currentUser!!.email.toString(),
            displayName = viewModel.state.value.currentUser!!.displayName ?: ""
        ))
    }

    when (status.status) {
        LoadingState.Status.SUCCESS -> {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            //LoadingState.IDLE
        }
        LoadingState.Status.FAILED -> {
            Toast.makeText(context, status.msg ?: "Error", Toast.LENGTH_LONG).show()
        }
        else -> {}
    }

    RegisterContent(
        email = inputEmail.text,
        emailError = inputEmail.hasError,
        emailErrorMsg = inputEmail.errorMsg,
        onEmailChange = { viewModel.onEvent(RegisterEvent.EmailValueChange(it)) },
        onEmailFocusChange = { viewModel.onEvent(RegisterEvent.EmailFocusChange(it)) },
        onClearEmail = { viewModel.onEvent(RegisterEvent.ClearEmail) },
        password = inputPassword.text,
        passwordError = inputPassword.hasError,
        passwordErrorMsg = inputPassword.errorMsg,
        onPasswordChange = { viewModel.onEvent(RegisterEvent.PasswordValueChange(it)) },
        onPasswordFocusChange = { viewModel.onEvent(RegisterEvent.PasswordFocusChange(it)) },
        onClearPassword = { viewModel.onEvent(RegisterEvent.ClearPassword) },
        confirmPassword = inputConfirmPassword.text,
        confirmPasswordError = inputConfirmPassword.hasError,
        confirmPasswordErrorMsg = inputConfirmPassword.errorMsg,
        onConfirmPasswordChange = { viewModel.onEvent(RegisterEvent.ConfirmPasswordValueChange(it)) },
        onConfirmPasswordFocusChange = { viewModel.onEvent(RegisterEvent.ConfirmPasswordFocusChange(it)) },
        onClearConfirmPassword = { viewModel.onEvent(RegisterEvent.ClearConfirmPassword) },
        onRegisterButtonClick = { viewModel.onEvent(RegisterEvent.SignUpWithEmailAndPassword(email = inputEmail.text, password = inputPassword.text)) },
        onLoginLinkClicked = { viewModel.onEvent(RegisterEvent.Login) }
    )
}


@Composable
fun RegisterContent(
    email: String,
    emailError: Boolean,
    emailErrorMsg: String,
    onEmailChange: (String) -> Unit,
    onEmailFocusChange: (FocusState) -> Unit,
    onClearEmail: () -> Unit,
    password: String,
    passwordError: Boolean,
    passwordErrorMsg: String,
    onPasswordChange: (String) -> Unit,
    onPasswordFocusChange: (FocusState) -> Unit,
    onClearPassword: () -> Unit,
    confirmPassword: String,
    confirmPasswordError: Boolean,
    confirmPasswordErrorMsg: String,
    onConfirmPasswordChange: (String) -> Unit,
    onConfirmPasswordFocusChange: (FocusState) -> Unit,
    onClearConfirmPassword: () -> Unit,
    onRegisterButtonClick: () -> Unit,
    onLoginLinkClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
        /*Image(
            painter = painterResource(id = R.drawable.ic_recipe_saver),
            contentDescription = "BackgroundIcon",
            //TODO: Check Color
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .fillMaxWidth()
                .width(50.dp)
                .height(50.dp),

            )*/
        //TODO: Check Text Size - removed style text was to large
        Text(text = "Register", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        OutlinedTextFieldWithError(
            text = email,
            onTextChanged = onEmailChange,
            label = "Email",
            onFocusChanged = onEmailFocusChange,
            leadingIcon = { Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "EmailIcon",
            )},
            trailingIcon = {
                IconButton(onClick = onClearEmail) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear Icon",
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = true,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            isError = emailError,
            errorMsg = emailErrorMsg
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        OutlinedTextFieldWithError(
            text = password,
            onTextChanged = onPasswordChange,
            label = "Password",
            onFocusChanged = onPasswordFocusChange,
            leadingIcon = { Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Lock Icon",
            )},
            trailingIcon = {
                IconButton(onClick = onClearPassword) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear Icon",
                    )
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            isError = passwordError,
            errorMsg = passwordErrorMsg
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        OutlinedTextFieldWithError(
            text = confirmPassword,
            onTextChanged = onConfirmPasswordChange,
            label = "Confirm Password",
            onFocusChanged = onConfirmPasswordFocusChange,
            leadingIcon = { Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Lock Icon",
            )},
            trailingIcon = {
                IconButton(onClick = onClearConfirmPassword) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear Icon",
                    )
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.clearFocus()
            }),
            isError = confirmPasswordError,
            errorMsg = confirmPasswordErrorMsg
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
        Button(onClick = {
            onRegisterButtonClick()
        },
            content = {
                Text(
                    text = "Register",
                )
            },
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Row {
            Text(
                text = "Already Registered? "
            )
            Text(
                text = "Login",
                // TODO: Color fontWeight to bold and increase size
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                // TODO: Color changed to tertiary
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                    onLoginLinkClicked()
                }
            )
        }
    }
}
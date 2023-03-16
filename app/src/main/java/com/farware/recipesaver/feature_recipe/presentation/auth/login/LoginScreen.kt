package com.farware.recipesaver.feature_recipe.presentation.auth.login

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.BuildConfig
import com.farware.recipesaver.R
import com.farware.recipesaver.feature_recipe.presentation.components.OutlinedTextFieldWithError
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import com.farware.recipesaver.feature_recipe.presentation.util.LoadingState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
){
    val focusManager = LocalFocusManager.current
    // for sign-in with google
    // build with a blank string for request token id and get the info from generated java in app>build
    // and place into the build types section of the module level gradle file
    //debug {
    //            buildConfigField("String", "SERVER_CLIENT_ID", "\"YOUR SERVER CLIENT ID FROM GENERATED JAVA  \"")
    //        }
    val inputEmail = viewModel.email.value
    val inputPassword = viewModel.password.value
    val status by viewModel.loadingState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val token = BuildConfig.SERVER_CLIENT_ID

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(token)
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {

        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            viewModel.onEvent(LoginEvent.SignInWithGoogle( account.idToken!!))
        } catch (e: ApiException) {
            Log.w("TAG", "Google sign in failed", e)
        }

    }

    if(viewModel.state.value.currentUser != null) {
        // save the users email
        viewModel.onEvent(LoginEvent.UpdatePreferences(
            context = context,
            displayEmail = viewModel.state.value.currentUser!!.email.toString(),
            displayName = viewModel.state.value.currentUser!!.displayName ?: ""
        ))
    }

/*
    when (status.status) {
        LoadingState.Status.SUCCESS -> {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
        LoadingState.Status.FAILED -> {
            Toast.makeText(context, status.msg ?: "Error", Toast.LENGTH_LONG).show()
        }
        else -> {}
    }
*/

    fun launchSignInWithGoogle() { launcher.launch(googleSignInClient.signInIntent) }

    LoginContent(
        focusManager = focusManager,
        facebookSignInLauncher = {},
        googleSignInLauncher = { launchSignInWithGoogle() },
        appleSignInLauncher = {},
        email = inputEmail.text,
        emailError = inputEmail.hasError,
        emailErrorMsg = inputEmail.errorMsg,
        onEmailChange = { viewModel.onEvent(LoginEvent.EmailValueChange(it)) },
        onEmailFocusChange = { viewModel.onEvent(LoginEvent.EmailFocusChange(it)) },
        onClearEmail = { viewModel.onEvent(LoginEvent.ClearEmail) },
        password = inputPassword.text,
        passwordError = inputPassword.hasError,
        passwordErrorMsg = inputPassword.errorMsg,
        onPasswordChange = { viewModel.onEvent(LoginEvent.PasswordValueChange(it)) },
        onPasswordFocusChange = { viewModel.onEvent(LoginEvent.PasswordFocusChange(it)) },
        onClearPassword = { viewModel.onEvent(LoginEvent.ClearPassword) },
        onLoginButtonClick = { viewModel.onEvent(LoginEvent.SignInWithEmailAndPassword(email = inputEmail.text, password = inputPassword.text )) },
        onRegisterLinkClicked = { viewModel.onEvent(LoginEvent.Register) }
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginContent(
    focusManager: FocusManager,
    facebookSignInLauncher: () -> Unit,
    googleSignInLauncher: () -> Unit,
    appleSignInLauncher: () -> Unit,
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
    onLoginButtonClick: () -> Unit,
    onRegisterLinkClicked: () -> Unit
) {

    Column(
        modifier = Modifier
            //.verticalScroll(rememberScrollState())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
        /*Image(
            painter = painterResource(id = R.drawable.ic_recipe_saver),
            contentDescription = "BackgroundIcon",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .fillMaxWidth()
                .width(50.dp)
                .height(50.dp),
        )*/
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        OutlinedTextFieldWithError(
            text = email,
            onTextChanged = onEmailChange,
            label = "Email",
            onFocusChanged = onEmailFocusChange,
            leadingIcon = { Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "EmailIcon"
            )},
            trailingIcon = {
                IconButton(onClick = onClearEmail) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear Icon",
                        // TODO: Color
                        //tint = MaterialTheme.extraColors.atfIconColor
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
                contentDescription = "Lock Icon"
            )},
            trailingIcon = {
                IconButton(onClick = onClearPassword) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear Icon"
                    )
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {focusManager.clearFocus()}),
            isError = passwordError,
            errorMsg = passwordErrorMsg
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
        Button(onClick = {
            onLoginButtonClick()
        },
            content={
                Text(
                    text = "Login")
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
            text = "Or, login with"
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Row {
            Surface(
                border = BorderStroke(1.dp, Color.DarkGray),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    facebookSignInLauncher()
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = "FacebookIcon",
                    modifier = Modifier
                        .padding(5.dp)
                        .width(40.dp)
                )

            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.mediumSmall))
            Surface(
                border = BorderStroke(1.dp, Color.DarkGray),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    googleSignInLauncher()
                }
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_google_icon
                    ),
                    contentDescription = "GoogleIcon",
                    modifier = Modifier
                        .padding(5.dp)
                        .width(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.mediumSmall))
            Surface(
                border = BorderStroke(1.dp, Color.DarkGray),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    appleSignInLauncher()
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.apple),
                    contentDescription = "AppleIcon",
                    modifier = Modifier
                        .padding(5.dp)
                        .width(40.dp)
                )

            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Row {
            Text(
                text = "New to Login? "
            )
            Text(
                text = "Register",
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
                    onRegisterLinkClicked()
                }
            )
        }
    }
}
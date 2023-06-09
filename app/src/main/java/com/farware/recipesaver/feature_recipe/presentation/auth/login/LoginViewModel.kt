package com.farware.recipesaver.feature_recipe.presentation.auth.login

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.use_cases.DataStoreUseCases
import com.farware.recipesaver.feature_recipe.domain.use_cases.FirebaseUseCases
import com.farware.recipesaver.feature_recipe.domain.use_cases.RecipeUseCases
import com.farware.recipesaver.feature_recipe.presentation.components.OutlinedTextFieldState
import com.farware.recipesaver.feature_recipe.presentation.navigation.AppNavigator
import com.farware.recipesaver.feature_recipe.presentation.navigation.Destination
import com.farware.recipesaver.feature_recipe.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val firebaseUseCases: FirebaseUseCases,
    private val dataStoreUseCases: DataStoreUseCases,
    private val recipesUseCases: RecipeUseCases
): ViewModel() {
    private var _state =  mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    val loadingState = MutableStateFlow(LoadingState.IDLE)

    private val _email = mutableStateOf(
        OutlinedTextFieldState(
            label = "Email"
        )
    )
    val email: State<OutlinedTextFieldState> = _email

    private val _password = mutableStateOf(
        OutlinedTextFieldState(
            label = "Password"
        )
    )
    val password: State<OutlinedTextFieldState> = _password

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailValueChange -> {
                _email.value = email.value.copy(
                    text = event.value,
                    isDirty = true
                )
            }
            is LoginEvent.EmailFocusChange -> {
                if(!event.focusState.isFocused && email.value.isDirty) {
                    if(!Patterns.EMAIL_ADDRESS.matcher(email.value.text).matches()) {
                        _email.value = email.value.copy(
                            hasError = true,
                            errorMsg = "* Invalid Email Address"
                        )
                    }
                    else {
                        _email.value = email.value.copy(
                            hasError = false,
                            errorMsg = ""
                        )
                    }
                }
            }
            is LoginEvent.ClearEmail -> {
                _email.value = email.value.copy(
                    text = "",
                    hasError = false,
                    errorMsg = ""
                )
            }
            is LoginEvent.PasswordValueChange -> {
                _password.value = password.value.copy(
                    text = event.value,
                    isDirty = true
                )
            }
            is LoginEvent.PasswordFocusChange -> {
                /*if(!event.focusState.isFocused && password.value.isDirty) {
                    if(!isValidPassword(password.value.text)) {
                        _password.value = password.value.copy(
                            hasError = true,
                            errorMsg = "Password must be at least 8 characters long\n and contain an upper case, a number,\n and a special character."
                        )
                    }
                    else {
                        _password.value = password.value.copy(
                            hasError = false,
                            errorMsg = ""
                        )
                    }
                }*/
            }
            is LoginEvent.ClearPassword -> {
                _password.value = password.value.copy(
                    text = "",
                    hasError = false,
                    errorMsg = ""
                )
            }
            is LoginEvent.SignInWithEmailAndPassword -> {
                firebaseUseCases.signInWithEmailAndPassword(event.email, event.password)
                    .onEach { result ->
                        when(result) {
                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    currentUser = result.data,
                                    isLoading = false
                                )

                                loadingState.emit(LoadingState.LOADED)
                            }
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    error = "Unable to sign-in, please try again.",     //result.message ?: "Unable to sign-in, please try again.",
                                    isLoading = false
                                )
                                loadingState.emit(LoadingState.error("Unable to sign-in, please try again."))  //result.message ?: "Unable to sign-in, please try again."
                            }
                            is Resource.Loading -> {
                                _state.value = state.value.copy(
                                    isLoading = true
                                )
                            }
                        }
                    }.launchIn(viewModelScope)
            }
            is LoginEvent.SignInWithGoogle -> {
                firebaseUseCases.signInWithGoogle(event.token)
                    .onEach { result ->
                        when(result) {
                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    currentUser = result.data,
                                    isLoading = false
                                )

                                loadingState.emit(LoadingState.LOADED)
                            }
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    error = result.message ?: "Unable to sign-in, please try again.",
                                    isLoading = false
                                )
                                loadingState.emit(LoadingState.error(result.message ?: "Unable to sign-in, please try again."))
                            }
                            is Resource.Loading -> {
                                _state.value = state.value.copy(
                                    isLoading = true
                                )
                            }
                        }
                    }.launchIn(viewModelScope)
            }
            is LoginEvent.UpdatePreferences -> {
                dataStoreUseCases.updateDisplayEmailAndName(
                    context = event.context,
                    displayEmail = event.displayEmail,
                    displayName = event.displayName
                )
                    .onEach { result ->
                        when(result) {
                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    isLoading = false,
                                )
                                loadingState.emit(LoadingState.LOADED)
                            }
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    error = result.message ?: "Unable to update Display Email and Display Name.",
                                    isLoading = false
                                )
                                loadingState.emit(LoadingState.error(result.message ?: "Unable to update Display Email and Display Name."))
                            }
                            is Resource.Loading -> {
                                _state.value = state.value.copy(
                                    isLoading = true
                                )
                            }
                        }
                    }.launchIn(viewModelScope)
                // clear the current user state
                // could change to a isAuthenticated boolean and then clear
                _state.value = state.value.copy(
                    currentUser = null
                )

                // navigate to recipes screen
                //appNavigator.tryNavigateTo(Destination.RecipesScreen())
                appNavigator.tryNavigateTo(route = Destination.RecipesScreen(), popUpToRoute = "recipe_feature", inclusive = true)
            }
            is LoginEvent.Register -> {
                appNavigator.tryNavigateTo(Destination.RegisterScreen())
            }
        }
    }
}


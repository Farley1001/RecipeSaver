package com.farware.recipesaver.feature_recipe.presentation.auth.register

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
class RegisterViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val firebaseUseCases: FirebaseUseCases,
    private val dataStoreUseCases: DataStoreUseCases,
    private val recipesUseCases: RecipeUseCases
): ViewModel() {
    private var _state =  mutableStateOf(RegisterState())
    val state: State<RegisterState> = _state

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

    private val _confirmPassword = mutableStateOf(
        OutlinedTextFieldState(
            label = "Confirm Password"
        )
    )
    val confirmPassword: State<OutlinedTextFieldState> = _confirmPassword

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailValueChange -> {
                _email.value = email.value.copy(
                    text = event.value,
                    isDirty = true
                )
            }
            is RegisterEvent.EmailFocusChange -> {
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
            is RegisterEvent.ClearEmail -> {
                _email.value = email.value.copy(
                    text = "",
                    hasError = false,
                    errorMsg = ""
                )
            }
            is RegisterEvent.PasswordValueChange -> {
                _password.value = password.value.copy(
                    text = event.value,
                    isDirty = true
                )
            }
            is RegisterEvent.PasswordFocusChange -> {
                if(!event.focusState.isFocused && password.value.isDirty) {
                    if(!isValidPassword(password.value.text)) {
                        _password.value = password.value.copy(
                            hasError = true,
                            errorMsg = "* Password must be at least 8 characters long\nand contain an upper case, a number,\nand a special character."
                        )
                    }
                    else {
                        _password.value = password.value.copy(
                            hasError = false,
                            errorMsg = ""
                        )
                    }
                }
            }
            is RegisterEvent.ClearPassword -> {
                _password.value = password.value.copy(
                    text = "",
                    hasError = false,
                    errorMsg = ""
                )
            }
            is RegisterEvent.ConfirmPasswordValueChange -> {
                _confirmPassword.value = confirmPassword.value.copy(
                    text = event.value,
                    isDirty = true
                )
            }
            is RegisterEvent.ConfirmPasswordFocusChange -> {
                if(!event.focusState.isFocused && confirmPassword.value.isDirty) {
                    if(confirmPassword.value.text != password.value.text) {
                        _confirmPassword.value = confirmPassword.value.copy(
                            hasError = true,
                            errorMsg = "* Passwords do not match"
                        )
                    }
                    else {
                        _confirmPassword.value = confirmPassword.value.copy(
                            hasError = false,
                            errorMsg = ""
                        )
                    }
                }
            }
            is RegisterEvent.ClearConfirmPassword -> {
                _confirmPassword.value = confirmPassword.value.copy(
                    text = "",
                    hasError = false,
                    errorMsg = ""
                )
            }
            is RegisterEvent.SignUpWithEmailAndPassword -> {
                firebaseUseCases.createUserWithEmailAndPassword(event.email, event.password)
                    .onEach { result ->
                        when(result) {
                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    currentUser = result.data,
                                    isLoading = false
                                )

                                // get conversions from firebase
                                //getConversionsFromFirebase()

                                // get measures from firebase
                                //getMeasuresFromFirebase()

                                //
                                loadingState.emit(LoadingState.LOADED)
                            }
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    error = result.message ?: "Unable to create new user, please try again.",
                                    isLoading = false
                                )
                                loadingState.emit(LoadingState.error(result.message ?: "Unable to create new user, please try again."))
                            }
                            is Resource.Loading -> {
                                _state.value = state.value.copy(
                                    isLoading = true
                                )
                            }
                        }
                    }.launchIn(viewModelScope)
            }
            is RegisterEvent.UpdatePreferences -> {
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
                                    error = result.message ?: "Unable to update Display Email and Name.",
                                    isLoading = false
                                )
                                loadingState.emit(LoadingState.error(result.message ?: "Unable to update Display Email and Name."))
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
                appNavigator.tryNavigateTo(Destination.RecipesScreen())
            }
            is RegisterEvent.Login -> {
                appNavigator.tryNavigateTo(Destination.RegisterScreen())
            }
        }
    }

    private fun isValidPassword(password: String?) : Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
            val passwordMatcher = Regex(passwordPattern)
            return passwordMatcher.find(password) != null
        } ?: return false
    }

    private fun getConversionsFromFirebase()  {
        firebaseUseCases.getFirebaseConversions()
            .onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        val data = result.data
                        recipesUseCases.insertAllConversions(data!!)
                        //
                        loadingState.emit(LoadingState.LOADED)
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            error = result.message ?: "Unable to get conversions from firebase.",
                            isLoading = false
                        )
                        loadingState.emit(LoadingState.error(result.message ?: "Unable to get conversions from firebase."))
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getMeasuresFromFirebase()  {
        firebaseUseCases.getFirebaseMeasures()
            .onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        val data = result.data
                        recipesUseCases.insertAllMeasures(data!!)
                        //
                        loadingState.emit(LoadingState.LOADED)
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            error = result.message ?: "Unable to get measures from firebase.",
                            isLoading = false
                        )
                        loadingState.emit(LoadingState.error(result.message ?: "Unable to get measures from firebase."))
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }
}
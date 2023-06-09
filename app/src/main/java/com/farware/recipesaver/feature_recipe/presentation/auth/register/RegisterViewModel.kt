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
import com.farware.recipesaver.feature_recipe.domain.use_cases.ValidationUseCases
import com.farware.recipesaver.feature_recipe.presentation.components.OutlinedTextFieldState
import com.farware.recipesaver.feature_recipe.presentation.navigation.AppNavigator
import com.farware.recipesaver.feature_recipe.presentation.navigation.Destination
import com.farware.recipesaver.feature_recipe.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val firebaseUseCases: FirebaseUseCases,
    private val dataStoreUseCases: DataStoreUseCases,
    private val recipesUseCases: RecipeUseCases,
    private val validationUseCases: ValidationUseCases
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

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()


    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailValueChange -> {
                _email.value = email.value.copy(
                    text = event.value,
                    isDirty = true,
                    hasError = false,
                    errorMsg = ""
                )
            }
            is RegisterEvent.EmailFocusChange -> {
                if(!event.focusState.isFocused && email.value.isDirty) {
                    val result = validationUseCases.validateEmail.execute(email.value.text)
                    if(!result.successful) {
                        _email.value = email.value.copy(
                            hasError = true,
                            errorMsg = result.errorMessage!!
                        )
                    }
                    /*else {
                        _email.value = email.value.copy(
                            hasError = false,
                            errorMsg = ""
                        )
                    }*/
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
                    isDirty = true,
                    hasError = false,
                    errorMsg = ""
                )
            }
            is RegisterEvent.PasswordFocusChange -> {
                if(!event.focusState.isFocused && password.value.isDirty) {
                    val result = validationUseCases.validatePassword.execute(password.value.text)
                    if(!result.successful) {
                        _password.value = password.value.copy(
                            hasError = true,
                            errorMsg = result.errorMessage!!
                        )
                    }
                    /*else {
                        _password.value = password.value.copy(
                            hasError = false,
                            errorMsg = ""
                        )
                    }*/
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
                    isDirty = true,
                    hasError = false,
                    errorMsg = ""
                )
            }
            is RegisterEvent.ConfirmPasswordFocusChange -> {
                if(!event.focusState.isFocused && confirmPassword.value.isDirty) {
                    val result = validationUseCases.validateConfirmPassword.execute(password.value.text, confirmPassword.value.text)
                    if(confirmPassword.value.text != password.value.text) {
                        _confirmPassword.value = confirmPassword.value.copy(
                            hasError = true,
                            errorMsg = result.errorMessage!!
                        )
                    }
                    /*else {
                        _confirmPassword.value = confirmPassword.value.copy(
                            hasError = false,
                            errorMsg = ""
                        )
                    }*/
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
                validateAndSubmit()
                /*firebaseUseCases.createUserWithEmailAndPassword(event.email, event.password)
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
                    }.launchIn(viewModelScope)*/
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
                //appNavigator.tryNavigateTo(Destination.RecipesScreen())
                appNavigator.tryNavigateTo(route = Destination.RecipesScreen(), popUpToRoute = "recipe_feature", inclusive = true)
            }
            is RegisterEvent.Login -> {
                appNavigator.tryNavigateTo(Destination.LoginScreen())
            }
        }
    }

/*    private fun isValidPassword(password: String?) : Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
            val passwordMatcher = Regex(passwordPattern)
            return passwordMatcher.find(password) != null
        } ?: return false
    }*/

    private fun validateAndSubmit() {
        val emailResult = validationUseCases.validateEmail.execute(email.value.text)
        val passwordResult = validationUseCases.validatePassword.execute(password.value.text)
        val confirmPasswordResult = validationUseCases.validateConfirmPassword.execute(
            password.value.text, confirmPassword.value.text
        )

        val hasError = listOf(
            emailResult,
            passwordResult,
            confirmPasswordResult
        ).any { !it.successful }

        if(hasError) {
            if (!emailResult.errorMessage.isNullOrEmpty()) {
                _email.value =
                    email.value.copy(errorMsg = emailResult.errorMessage!!, hasError = true)

            } //else { _email.value = email.value.copy(errorMsg = "", hasError = false) }

            if (!passwordResult.errorMessage.isNullOrEmpty()) {
                _password.value =
                    password.value.copy(errorMsg = passwordResult.errorMessage!!, hasError = true)

            } //else { _password.value = email.value.copy(errorMsg = "", hasError = false) }

            if(!confirmPasswordResult.errorMessage.isNullOrEmpty()) {
                _confirmPassword.value = confirmPassword.value.copy(
                    errorMsg = confirmPasswordResult.errorMessage!!,
                    hasError = true
                )
            } //else {
              //  _confirmPassword.value = confirmPassword.value.copy(
              //      errorMsg = "",
              //      hasError = false
              //  )
            //}
        } else {
            registerNewUser(email.value.text, password.value.text)
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }

    private fun registerNewUser(email: String, password: String){
        firebaseUseCases.createUserWithEmailAndPassword(email, password)
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
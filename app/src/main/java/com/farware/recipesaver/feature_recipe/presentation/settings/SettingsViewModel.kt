package com.farware.recipesaver.feature_recipe.presentation.settings

import android.content.Context
import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureType
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureUnit
import com.farware.recipesaver.feature_recipe.domain.use_cases.DataStoreUseCases
import com.farware.recipesaver.feature_recipe.domain.use_cases.FirebaseUseCases
import com.farware.recipesaver.feature_recipe.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel@Inject constructor(
    private val dataStoreUseCases: DataStoreUseCases,
    private val firebaseUseCases: FirebaseUseCases,
    @ApplicationContext context: Context,
): ViewModel() {

    private val _state =  mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    val loadingState = MutableStateFlow(LoadingState.IDLE)

    init {
        getSettings(context)

        // show the use dynamic color option if build version >= S
        // api 31
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                _state.value = state.value.copy(
                    showDynamicColorOption = true
                )
            }

        }
    }

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.DisplayNameValueChange -> {
                _state.value = state.value.copy(
                    displayName = event.value,
                    displayNameIsDirty = true
                )
            }
            is SettingsEvent.EditDisplayName -> {
                _state.value = state.value.copy(
                    displayNameIsEditing = true
                )
            }
            is SettingsEvent.SaveDisplayName -> {
                if(state.value.displayNameIsDirty) {
                    updateDisplayName(event.context, state.value.displayName)
                }
                _state.value = state.value.copy(
                    displayNameIsEditing = false,
                    displayNameIsDirty = false
                )
            }
            is SettingsEvent.ClearDisplayName -> {
                _state.value = state.value.copy(
                    displayName = ""
                )
            }
            is SettingsEvent.ToggleImperialMeasure -> {
                val newType = if(state.value.measureType == MeasureType.US) {
                    MeasureType.IMPERIAL
                } else {
                    MeasureType.US
                }
                _state.value = state.value.copy(
                    measureType = newType
                )

                updateMeasureType(event.context, state.value.measureType)
            }
            is SettingsEvent.ToggleMilliliterUnits -> {
                val newUnit = if(state.value.measureUnit == MeasureUnit.FLUID_OZ) {
                    MeasureUnit.MILLILITERS
                } else {
                    MeasureUnit.FLUID_OZ
                }
                _state.value = state.value.copy(
                    measureUnit = newUnit
                )

                updateMeasureUnit(event.context, state.value.measureUnit)
            }
            is SettingsEvent.ToggleDynamicColor -> {
                _state.value = state.value.copy(
                    useDynamicColor = !state.value.useDynamicColor
                )

                updateUseDynamicColor(event.context, state.value.useDynamicColor)
            }
        }
    }

    fun signOut() {
        firebaseUseCases.signOut()
            .onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoading = false
                        )
                        loadingState.emit(LoadingState.LOADED)
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            error = result.message ?: "Unable to sign out, please try again.",
                            isLoading = false
                        )
                        loadingState.emit(LoadingState.error(result.message ?: "Unable to sign out, please try again."))
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getSettings(context: Context) {
        dataStoreUseCases.getAllPreferences(context)
            .onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            displayEmail = result.data!!.displayEmail,
                            displayName = result.data.displayName,
                            useDynamicColor = result.data.useDynamicColor,
                            measureType = result.data.measureType,
                            measureUnit = result.data.measureUnit,
                            isLoading = false,
                        )
                        // changed from LoadingState.LOADED so success is not emitted
                        loadingState.emit(LoadingState.IDLE)
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            error = result.message ?: "Unable to get preferences.",
                            isLoading = false
                        )
                        loadingState.emit(LoadingState.error(result.message ?: "Unable to get preferences."))
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    }
                }

            }.launchIn(viewModelScope)
    }

    private fun updateDisplayName(context: Context, displayName: String) {
        dataStoreUseCases.updateDisplayName(context, displayName)
            .onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                        )
                        // changed from LoadingState.LOADED so success is not emitted
                        loadingState.emit(LoadingState.IDLE)
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            error = result.message ?: "Unable to update Display Name.",
                            isLoading = false
                        )
                        loadingState.emit(LoadingState.error(result.message ?: "Unable to update Display Name."))
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    }
                }

            }.launchIn(viewModelScope)
    }

    private fun updateUseDynamicColor(context: Context, useDynamicColor: Boolean) {
        dataStoreUseCases.updateUseDynamicColor(context, useDynamicColor)
            .onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                        )
                        // changed from LoadingState.LOADED so success is not emitted
                        loadingState.emit(LoadingState.IDLE)
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            error = result.message ?: "Unable to update UseDynamicColor setting.",
                            isLoading = false
                        )
                        loadingState.emit(LoadingState.error(result.message ?: "Unable to update UseDynamicColor setting."))
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    }
                }

            }.launchIn(viewModelScope)
    }

    private fun updateMeasureType(context: Context, measureType: MeasureType) {
        dataStoreUseCases.updateMeasureType(context, measureType)
            .onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                        )
                        // changed from LoadingState.LOADED so success is not emitted
                        loadingState.emit(LoadingState.IDLE)
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            error = result.message ?: "Unable to update MeasureType.",
                            isLoading = false
                        )
                        loadingState.emit(LoadingState.error(result.message ?: "Unable to update MeasureType."))
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    }
                }

            }.launchIn(viewModelScope)
    }

    private fun updateMeasureUnit(context: Context, measureUnit: MeasureUnit) {
        dataStoreUseCases.updateMeasureUnit(context, measureUnit)
            .onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                        )
                        // changed from LoadingState.LOADED so success is not emitted
                        loadingState.emit(LoadingState.IDLE)
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            error = result.message ?: "Unable to update MeasureUnit.",
                            isLoading = false
                        )
                        loadingState.emit(LoadingState.error(result.message ?: "Unable to update MeasureUnit."))
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
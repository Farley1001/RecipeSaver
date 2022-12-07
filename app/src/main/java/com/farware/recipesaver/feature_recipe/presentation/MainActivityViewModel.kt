package com.farware.recipesaver.feature_recipe.presentation

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.use_cases.DataStoreUseCases
import com.farware.recipesaver.feature_recipe.presentation.util.LoadingState
import com.farware.recipesaver.feature_recipe.presentation.util.MainActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreUseCases: DataStoreUseCases,
    @ApplicationContext context: Context,
): ViewModel() {
    private val _state =  mutableStateOf(MainActivityState())
    val state: State<MainActivityState> = _state

    val loadingState = MutableStateFlow(LoadingState.IDLE)

    init {
        getSettings(context)
    }

    private fun getSettings(context: Context) {
        dataStoreUseCases.getUseDynamicColor(context)
            .onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            useDynamicColor = result.data!!,
                            isLoading = false
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
}
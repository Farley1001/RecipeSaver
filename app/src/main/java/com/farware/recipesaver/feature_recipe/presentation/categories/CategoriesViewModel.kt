package com.farware.recipesaver.feature_recipe.presentation.categories

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.domain.use_cases.FirebaseUseCases
import com.farware.recipesaver.feature_recipe.domain.use_cases.RecipeUseCases
import com.farware.recipesaver.feature_recipe.domain.util.CategoryOrder
import com.farware.recipesaver.feature_recipe.domain.util.OrderType
import com.farware.recipesaver.feature_recipe.presentation.recipes.RecipesEvent
import com.farware.recipesaver.feature_recipe.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val recipeUseCases: RecipeUseCases,
    private val firebaseUseCases: FirebaseUseCases,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _state = mutableStateOf(CategoriesState())
    val state: State<CategoriesState> = _state

    private val loadingState = MutableStateFlow(LoadingState.IDLE)

    private var recentlyDeletedCategory: Category? = null

    private var getCategoriesJob: Job? = null

    init {
        getCategories(CategoryOrder.Name(OrderType.Ascending))
    }

    fun onEvent(event: CategoriesEvent) {
        when(event) {
            is CategoriesEvent.Order -> {
                if(state.value.categoryOrder::class == event.categoryOrder::class &&
                    state.value.categoryOrder.orderType == event.categoryOrder.orderType) {
                    return
                }

                state.value.categoryOrder = event.categoryOrder

                // call the last query
                getCategories(state.value.categoryOrder)

            }
            is CategoriesEvent.DeleteCategory -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(
                        showDeleteDialog = true,
                        categoryToDelete = event.category
                    )
                }
            }
            is CategoriesEvent.DeleteConfirmed -> {
                viewModelScope.launch {
                    recipeUseCases.deleteCategory(state.value.categoryToDelete!!)
                    _state.value = state.value.copy(
                        showDeleteDialog = false,
                        categoryToDelete = null
                    )
                }
            }
            is CategoriesEvent.DeleteCanceled -> {
                _state.value = state.value.copy(
                    showDeleteDialog = false,
                    categoryToDelete = null
                )
            }
            /*is CategoriesEvent.RestoreCategory -> {
                viewModelScope.launch {
                    recipeUseCases.addCategory(recentlyDeletedCategory ?: return@launch)
                    recentlyDeletedCategory = null
                }
            }*/
            is CategoriesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isSearchSectionVisible = false,
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
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

    private fun getCategories(categoryOrder: CategoryOrder) {
        getCategoriesJob?.cancel()
        getCategoriesJob = recipeUseCases.getCategories(categoryOrder)
            .onEach { categories ->
                _state.value = state.value.copy(
                    categories = categories,

                    )
            }
            .launchIn(viewModelScope)
    }
}
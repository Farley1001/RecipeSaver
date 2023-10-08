package com.farware.recipesaver.feature_recipe.presentation.recipes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe
import com.farware.recipesaver.feature_recipe.domain.use_cases.FirebaseUseCases
import com.farware.recipesaver.feature_recipe.domain.use_cases.RecipeUseCases
import com.farware.recipesaver.feature_recipe.domain.util.OrderType
import com.farware.recipesaver.feature_recipe.domain.util.RecipeOrder
import com.farware.recipesaver.feature_recipe.domain.util.RecipeSearch
import com.farware.recipesaver.feature_recipe.presentation.navigation.AppNavigator
import com.farware.recipesaver.feature_recipe.presentation.navigation.Destination
import com.farware.recipesaver.feature_recipe.presentation.util.LoadingState
import com.farware.recipesaver.feature_recipe.presentation.util.QueryTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val recipeUseCases: RecipeUseCases,
    private val firebaseUseCases: FirebaseUseCases,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _state =  mutableStateOf(RecipesState())
    val state: State<RecipesState> = _state

    private val loadingState = MutableStateFlow(LoadingState.IDLE)

    private val _searchString = mutableStateOf(
        RecipeTextFieldState(
            hint = "Search for a recipe..."
        ))
    val searchString: State<RecipeTextFieldState> = _searchString

    private val _lastQuery = mutableStateOf(
        QueryTypes.NORMAL
    )
    private val lastQuery: State<QueryTypes> = _lastQuery

    private var getRecipesJob: Job? = null

    private var getSearchJob: Job? = null

    init {
        //insertRecipe()
        getRecipes(false, RecipeOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: RecipesEvent) {
        when(event) {
            is RecipesEvent.Order -> {
                if(state.value.recipeOrder::class == event.recipeOrder::class &&
                    state.value.recipeOrder.orderType == event.recipeOrder.orderType) {
                    return
                }

                state.value.recipeOrder = event.recipeOrder

                // call the last query
                if(lastQuery.value == QueryTypes.NORMAL) {
                    getRecipes(state.value.isSelectFavoritesOnly, state.value.recipeOrder)
                } else {
                    searchRecipes(state.value.recipeSearch, searchString.value.text, state.value.isSelectFavoritesOnly, state.value.recipeOrder)
                }
            }
            is RecipesEvent.EnteredSearch -> {
                _searchString.value = searchString.value.copy(
                    text = event.value
                )
                // call the last query
                //if(lastQuery.value == QueryTypes.NORMAL) {
                //    getRecipes(state.value.isSelectFavoritesOnly, state.value.recipeOrder)
                //} else {
                    searchRecipes(state.value.recipeSearch, searchString.value.text, state.value.isSelectFavoritesOnly, state.value.recipeOrder)
                //}
            }
            is RecipesEvent.ClearSearch -> {
                _searchString.value = searchString.value.copy(
                    text = ""
                )
                // set the last query type to normal and refresh recipes
                _lastQuery.value = QueryTypes.NORMAL
                getRecipes(state.value.isSelectFavoritesOnly, state.value.recipeOrder)
            }
            is RecipesEvent.ChangeSearchFocus -> {
                _searchString.value = searchString.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            searchString.value.text.isBlank()
                )
            }
            is RecipesEvent.ToggleFavorites -> {
                // toggle the state
                _state.value = state.value.copy(
                    isSelectFavoritesOnly = !state.value.isSelectFavoritesOnly
                )
                // call the last query
                if(lastQuery.value == QueryTypes.NORMAL) {
                    getRecipes(state.value.isSelectFavoritesOnly, state.value.recipeOrder)
                } else {
                    searchRecipes(state.value.recipeSearch, searchString.value.text, state.value.isSelectFavoritesOnly, state.value.recipeOrder)
                }
            }
            is RecipesEvent.Search -> {
                if(searchString.value.text == "") { return}
                _lastQuery.value = QueryTypes.SEARCH
                searchRecipes(event.recipeSearch, searchString.value.text, state.value.isSelectFavoritesOnly, state.value.recipeOrder)
                _state.value = state.value.copy(
                    recipeSearch = event.recipeSearch
                    //state.value.recipeSearch  =  event.recipeSearch.copy()
                )
            }
            is RecipesEvent.DeleteRecipe -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(
                        showDeleteDialog = true,
                        recipeToDelete = event.recipe
                    )
                }
            }
            is RecipesEvent.DeleteConfirmed -> {
                viewModelScope.launch {
                    recipeUseCases.deleteRecipe(state.value.recipeToDelete!!)
                    _state.value = state.value.copy(
                        showDeleteDialog = false,
                        recipeToDelete = null
                    )
                }
            }
            is RecipesEvent.DeleteCanceled -> {
                _state.value = state.value.copy(
                    showDeleteDialog = false,
                    recipeToDelete = null
                )
            }
            /*is RecipesEvent.RestoreRecipe -> {
                viewModelScope.launch {
                    recipeUseCases.insertRecipe(recentlyDeletedRecipe ?: return@launch)
                    recentlyDeletedRecipe = null
                }
            }*/
            is RecipesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isSearchSectionVisible = false,
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is RecipesEvent.ToggleSearchSection -> {
                if (state.value.isSearchSectionVisible && searchString.value.text != "") {
                    _lastQuery.value = QueryTypes.SEARCH
                    searchRecipes(state.value.recipeSearch, searchString.value.text, state.value.isSelectFavoritesOnly, state.value.recipeOrder)
                    _state.value = state.value.copy(
                        recipeSearch = state.value.recipeSearch
                    )
                }
                // toggle the search section
                _state.value = state.value.copy(
                    isOrderSectionVisible = false,
                    isSearchSectionVisible = !state.value.isSearchSectionVisible
                )
            }
            is RecipesEvent.NewRecipe -> {
                // navigate to RecipeScreen with default id
                appNavigator.tryNavigateTo(Destination.RecipeScreen(-1L))
            }
            is RecipesEvent.NavigateToRecipe -> {
                appNavigator.tryNavigateTo(Destination.RecipeScreen(event.recipeId))
            }
            /*is RecipesEvent.NavigateToRecipeAddEdit -> {
                appNavigator.tryNavigateTo(Destination.RecipeAddEditScreen(event.recipeId))
            }*/
            is RecipesEvent.NavMenuNavigate -> {
                var route = event.route
                when (route) {
                    "sign_out" -> {
                        signOut()
                        route = Destination.LoginScreen()
                    }
                    else -> {
                    }
                }
                appNavigator.tryNavigateTo(route)
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

    // used for testing
    private fun insertRecipe() {
        viewModelScope.launch {
            // FindThis
            recipeUseCases.insertRecipe(
                Recipe(
                    recipeId = null,
                    categoryId = 3,
                    name = "My New Favorite",
                    description = "Best chicken recipe ever!",
                    favorite = true,
                    timeStamp = System.currentTimeMillis()
                )
            )
        }
    }

    private fun getRecipes(favoritesOnly: Boolean, recipeOrder: RecipeOrder) {
        getRecipesJob?.cancel()
        getRecipesJob = recipeUseCases.getRecipes(favoritesOnly, recipeOrder)
            .onEach { recipes ->
                _state.value = state.value.copy(
                    recipes = recipes,
                    recipeOrder = recipeOrder
                )
            }
            .launchIn(viewModelScope)
    }

    private fun searchRecipes(searchField: RecipeSearch, searchString: String, favoritesOnly: Boolean, recipeOrder: RecipeOrder) {
        getSearchJob?.cancel()
        when (searchField) {
            is RecipeSearch.Category -> {
                getSearchJob = recipeUseCases.searchRecipesOnCategory(searchString, favoritesOnly, recipeOrder)
                    .onEach { recipes ->
                        _state.value = state.value.copy(
                            recipes = recipes,
                            recipeOrder = recipeOrder
                        )
                    }
                    .launchIn(viewModelScope)
            }
            is RecipeSearch.Name -> {
                getSearchJob = recipeUseCases.searchRecipesOnName(searchString, favoritesOnly, recipeOrder)
                    .onEach { recipes ->
                        _state.value = state.value.copy(
                            recipes = recipes,
                            recipeOrder = recipeOrder
                        )
                    }
                    .launchIn(viewModelScope)
            }
            is RecipeSearch.Ingredients -> {
                getSearchJob = recipeUseCases.searchRecipesOnIngredients(searchString, favoritesOnly, recipeOrder)
                    .onEach { recipes ->
                        _state.value = state.value.copy(
                            recipes = recipes,
                            recipeOrder = recipeOrder
                        )
                    }
                    .launchIn(viewModelScope)
            }
            is RecipeSearch.Directions -> {
                getSearchJob = recipeUseCases.searchRecipesOnDirections(searchString, favoritesOnly, recipeOrder)
                    .onEach { recipes ->
                        _state.value = state.value.copy(
                            recipes = recipes,
                            recipeOrder = recipeOrder
                        )
                    }
                    .launchIn(viewModelScope)
            }
        }
    }
}
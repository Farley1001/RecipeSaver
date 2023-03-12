package com.farware.recipesaver.feature_recipe.presentation.recipe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidRecipeException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategory
import com.farware.recipesaver.feature_recipe.domain.use_cases.RecipeUseCases
import com.farware.recipesaver.feature_recipe.presentation.navigation.AppNavigator
import com.farware.recipesaver.feature_recipe.presentation.navigation.Destination
import com.farware.recipesaver.feature_recipe.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val recipeUseCases: RecipeUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel()  {

    private var _state =  mutableStateOf(RecipeState())
    val state: State<RecipeState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentRecipeId: Long? = null

    private var getCategoriesJob: Job? = null

    private val newRecipe = RecipeWithCategory(
        recipeId = null,
        categoryId = -1,
        name = "",
        description = "",
        imagePath = null,
        prepTime = 0,
        cookTime = 0,
        favorite = false,
        category = "",
        colorId = -1,
        color = "",
        lightThemeColor = -1,
        onLightThemeColor = -1,
        darkThemeColor = -1,
        onDarkThemeColor = -1,
        timeStamp = System.currentTimeMillis()
    )

    private val _recipe = mutableStateOf<RecipeWithCategory?>(newRecipe)
    val recipe: State<RecipeWithCategory?> = _recipe

    init {
        getCategories()
        savedStateHandle.get<Long>("recipeId")?.let { recipeId ->
            if (recipeId != -1L) {
                getRecipe(recipeId)
            } else {
                _state.value = state.value.copy(
                    showAddEditRecipeDialog = true
                )
            }
        }
    }

    fun onEvent(event: RecipeEvent) {
        when(event) {
            is RecipeEvent.SelectedTabChanged -> {
                _state.value = state.value.copy(
                    selectedTabIndex = event.selectedIndex
                )
            }
            is RecipeEvent.ToggleCategoryDialog -> {
                _state.value = state.value.copy(
                    isCategoryDialogOpen = !state.value.isCategoryDialogOpen
                )
            }
            is RecipeEvent.NewCategorySelected -> {
                // set the initial selected value
                _state.value = state.value.copy(
                    selectedCategoryIndex = state.value.categories.indexOfFirst{
                        it.categoryId == event.categoryId
                    }
                )

                _recipe.value = recipe.value?.copy(
                    categoryId = state.value.categories[state.value.selectedCategoryIndex].categoryId!!,
                    category = state.value.categories[state.value.selectedCategoryIndex].name,
                    lightThemeColor = state.value.categories[state.value.selectedCategoryIndex].lightThemeColor,
                    onLightThemeColor = state.value.categories[state.value.selectedCategoryIndex].onLightThemeColor,
                    darkThemeColor = state.value.categories[state.value.selectedCategoryIndex].darkThemeColor,
                    onDarkThemeColor = state.value.categories[state.value.selectedCategoryIndex].onDarkThemeColor,
                    timeStamp = state.value.categories[state.value.selectedCategoryIndex].timeStamp,
                )
            }
            is RecipeEvent.SaveNewCategory -> {
                _state.value = state.value.copy(
                    isCategoryDialogOpen = false
                )

                // update the recipe with new categoryId
                _recipe.value = recipe.value?.copy(
                    categoryId = state.value.newCategoryId
                )

                // save the recipe
                if(recipe.value?.name != "" && recipe.value?.description != "" && recipe.value?.categoryId!! > 0) {
                    saveRecipe(recipe.value!!.toRecipe())
                }
                
            }
            is RecipeEvent.SavePrepTime -> {
                // update the recipe with new prep time
                _recipe.value = recipe.value?.copy(
                    prepTime = event.prepTime
                )

                // save the recipe
                if(recipe.value?.name != "" && recipe.value?.description != "" && recipe.value?.categoryId!! > 0) {
                    saveRecipe(recipe.value!!.toRecipe())
                }
            }
            is RecipeEvent.SaveCookTime -> {
                // update the recipe with new cook time
                _recipe.value = recipe.value?.copy(
                    cookTime = event.cookTime
                )

                // save the recipe
                if(recipe.value?.name != "" && recipe.value?.description != "" && recipe.value?.categoryId!! > 0) {
                    saveRecipe(recipe.value!!.toRecipe())
                }
            }
            is RecipeEvent.ToggleFavorite -> {
                // update the recipe with new favorite
                _recipe.value = recipe.value?.copy(
                    favorite = recipe.value?.favorite != true   /// ??????
                )

                // save the recipe
                if(recipe.value?.name != "" && recipe.value?.description != "" && recipe.value?.categoryId!! > 0) {
                    saveRecipe(recipe.value!!.toRecipe())
                }
            }
            is RecipeEvent.ToggleAddEditDialog -> {
                _state.value = state.value.copy(
                    showAddEditRecipeDialog = !state.value.showAddEditRecipeDialog
                )
                if(!state.value.showAddEditRecipeDialog && recipe.value?.recipeId == null) {
                    // its a new recipe and cancel clicked navigate back
                    appNavigator.tryNavigateBack()
                }
            }
            is RecipeEvent.NavigateBack -> {
                appNavigator.tryNavigateTo(Destination.RecipesScreen())
            }
            is RecipeEvent.RecipeNameTextChange -> {
                _recipe.value = recipe.value!!.copy(
                    name = event.name
                )
            }
            is RecipeEvent.RecipeDescriptionTextChange -> {
                _recipe.value = recipe.value!!.copy(
                    description = event.description
                )
            }
            is RecipeEvent.SaveRecipe -> {
                if(recipe.value?.name != "" && recipe.value?.description != "" && recipe.value?.categoryId!! > 0) {
                    saveRecipe(recipe.value!!.toRecipe())
                }
            }
        }
    }

    private fun getRecipe(recipeId: Long) {
        viewModelScope.launch {
            recipeUseCases.getRecipe(recipeId)?.also { recipe ->
                currentRecipeId = recipe.recipeId

                _recipe.value = recipe

                // set the initial selected value
                _state.value = state.value.copy(
                    selectedCategoryIndex = state.value.categories.indexOfFirst{
                        it.categoryId == recipe.categoryId
                    }
                )

            }
        }
    }

    private fun saveRecipe(recipe: Recipe) {
        CoroutineScope(Dispatchers.IO).launch {
            var id = recipe.recipeId
            try {
                if(id == null) {
                    id = recipeUseCases.insertRecipeReturnId(recipe)
                } else {
                    recipeUseCases.addRecipe(recipe)
                }
                _eventFlow.emit(UiEvent.SaveRecipe)
            } catch (e: InvalidRecipeException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        e.message ?: "Couldn't save recipe"
                    )
                )
            }
            if(id != null) {
                appNavigator.tryNavigateTo(Destination.RecipeScreen(id))
            }
        }
    }

    private fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = recipeUseCases.getCategories()
            .onEach { categories ->
                _state.value = state.value.copy(
                    categories = categories
                )
            }
            .launchIn(viewModelScope)
    }

}
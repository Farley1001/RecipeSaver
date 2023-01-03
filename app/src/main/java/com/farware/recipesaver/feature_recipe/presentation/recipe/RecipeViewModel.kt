package com.farware.recipesaver.feature_recipe.presentation.recipe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.domain.model.recipe.CategoryColor
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidRecipeException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.CategoryWithColor
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategoryAndColor
import com.farware.recipesaver.feature_recipe.domain.use_cases.FirebaseUseCases
import com.farware.recipesaver.feature_recipe.domain.use_cases.RecipeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeUseCases: RecipeUseCases,
    private val firebaseUseCases: FirebaseUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel()  {

    private var _state =  mutableStateOf(RecipeState())
    val state: State<RecipeState> = _state

    // contains current recipe of null for new
    private val _recipe = mutableStateOf<RecipeWithCategoryAndColor?>(null)
    val recipe: State<RecipeWithCategoryAndColor?> = _recipe

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _categoryName = mutableStateOf("")
    val categoryName: State<String> = _categoryName

    private val _prepTime = mutableStateOf(0L)
    val prepTime: State<Long> = _prepTime

    private val _cookTime = mutableStateOf(0L)
    val cookTime: State<Long> = _cookTime

    private val _favorite = mutableStateOf(false)
    val favorite: State<Boolean> = _favorite

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentRecipeId: Long? = null

    private var getCategoriesJob: Job? = null

    private var getFbIngredientsJob: Job? = null

    private val newRecipe = RecipeWithCategoryAndColor(
        recipeId = null,
        categoryId = -1,
        name = "",
        description = "",
        imagePath = null,
        prepTime = null,
        cookTime = null,
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

    init {
        getFbIngredients()
        getCategories()
        savedStateHandle.get<Long>("recipeId")?.let {
                recipeId ->
            if(recipeId != -1L) {
                getRecipe(recipeId)
                /*viewModelScope.launch {
                    recipeUseCases.getRecipe(recipeId)?.also { recipe ->
                        currentRecipeId = recipe.recipe.recipeId

                        _recipe.value = recipe

                        _name.value = recipe.recipe.name

                        _categoryName.value = recipe.category.category.name

                        _prepTime.value = recipe.recipe.prepTime!!

                        _cookTime.value = recipe.recipe.cookTime!!

                        _favorite.value = recipe.recipe.favorite!!

                        // set the initial selected value
                        _state.value = state.value.copy(
                            selectedCategoryIndex = state.value.categories.indexOfFirst{
                                it.category.categoryId == recipe.recipe.categoryId
                            }
                        )

                    }
                }*/
            } else {

                // TODO"  open dialog to enter recipe name and category
                currentRecipeId = recipeId

                _recipe.value = newRecipe
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
            is RecipeEvent.NewSelectedCategory -> {
                _state.value = state.value.copy(
                    newCategoryId = event.categoryId
                )
            }
            is RecipeEvent.SaveNewCategory -> {
                _state.value = state.value.copy(
                    isCategoryDialogOpen = false
                )

                // update the recipe with new categoryId
                val saveRecipe = recipe.value?.copy(
                    categoryId = state.value.newCategoryId
                )

                // save the recipe
                if (saveRecipe != null) {
                    saveRecipe(saveRecipe.toRecipe())
                    // because the category and category colors are embedded
                    // get the recipe again
                    getRecipe(currentRecipeId!!)
                }
                
            }
            is RecipeEvent.SavePrepTime
            -> {
                // update the prep time field
                _prepTime.value = event.prepTime

                // update the recipe with new prep time
                val saveRecipe = recipe.value?.copy(
                    prepTime = event.prepTime
                )

                // save the recipe
                if (saveRecipe != null) {
                    saveRecipe(saveRecipe.toRecipe())
                }
            }
            is RecipeEvent.SaveCookTime -> {
                // update the cook time field
                _cookTime.value = event.cookTime

                // update the recipe with new cook time
                val saveRecipe = recipe.value?.copy(
                    cookTime = event.cookTime
                )

                // save the recipe
                if (saveRecipe != null) {
                    saveRecipe(saveRecipe.toRecipe())
                }
            }
            is RecipeEvent.ToggleFavorite -> {
                // toggle the favorite switch
                _favorite.value = !favorite.value

                /// update the recipe with new favorite
                val saveRecipe = recipe.value?.copy(
                    favorite = favorite.value
                )

                // save the recipe
                if (saveRecipe != null) {
                    saveRecipe(saveRecipe.toRecipe())
                }
            }
            is RecipeEvent.ChangeRecipeName -> {
                //TOTO: Toggle recipe name change dialog
            }
        }
    }

    private fun getRecipe(recipeId: Long) {
        viewModelScope.launch {
            recipeUseCases.getRecipe(recipeId)?.also { recipe ->
                currentRecipeId = recipe.recipeId

                _recipe.value = recipe

                _name.value = recipe.name

                _categoryName.value = recipe.category

                _prepTime.value = recipe.prepTime!!

                _cookTime.value = recipe.cookTime!!

                _favorite.value = recipe.favorite!!

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
        viewModelScope.launch {
            try {
                recipeUseCases.saveRecipe(recipe)
                _eventFlow.emit(UiEvent.SaveRecipe)
            } catch (e: InvalidRecipeException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        e.message ?: "Couldn't save recipe"
                    )
                )
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

    private fun getFbIngredients() {
        getFbIngredientsJob?.cancel()
        getFbIngredientsJob = firebaseUseCases.getFirebaseIngredients()
            .onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            fbIngredients = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String): UiEvent()
        object SaveRecipe: UiEvent()
    }
}
package com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure
import com.farware.recipesaver.feature_recipe.domain.model.recipe.RecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeIngredientWithIngredient
import com.farware.recipesaver.feature_recipe.domain.use_cases.RecipeIngredientUseCases
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.IngredientFocus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientsTabViewModel @Inject constructor(
    private val ingredientUseCases: RecipeIngredientUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private var _state =  mutableStateOf(IngredientsTabState())
    val state: State<IngredientsTabState> = _state

    private var _measure = mutableStateOf(Measure(null, "", ""))
    val measure: State<Measure> = _measure

    private var _ingredient = mutableStateOf(Ingredient(-1, "", ""))
    val ingredient: State<Ingredient> = _ingredient

    private var _recipeIngredient = mutableStateOf(RecipeIngredient(null, -1, -1, -1, "", "", ""))
    val recipeIngredient: State<RecipeIngredient> = _recipeIngredient

    private var _recipeIngredientWithIngredient = mutableStateOf(RecipeIngredientWithIngredient(null, -1, -1, -1, "", "", "", "", ""))
    val recipeIngredientWithIngredient: State<RecipeIngredientWithIngredient> = _recipeIngredientWithIngredient

    private var _newIngredient = mutableStateOf(FullRecipeIngredient(-1,-1L,-1,-1, "","","","",""))
    val newIngredient: State<FullRecipeIngredient> = _newIngredient

    private var getIngredientsJob: Job? = null

    init {
        savedStateHandle.get<Long>("recipeId")?.let { recipeId ->
            if(recipeId != -1L) {
                getRecipeIngredients(recipeId)
            }
        }
    }

    fun onEvent(event: IngredientsTabEvent) {
        when (event) {
            is IngredientsTabEvent.IngredientFocusChanged -> {
                var sf = state.value.ingredientFocus
                sf = sf.map { item ->
                    if(item.fullIngredient.ingredientId == event.ingredientFocus.fullIngredient.ingredientId)
                        item.copy(focused = true)
                    else
                        item.copy(focused = false)
                }
                _state.value = state.value.copy(
                    ingredientFocus = sf
                )
            }
            is IngredientsTabEvent.NewIngredient -> {
                // TODO : add new ingredient logic here
            }
            is IngredientsTabEvent.SaveIngredient -> {
                viewModelScope.launch {
                    ingredientUseCases.addRecipeIngredient(event.ingredient.toRecipeIngredient())
                }
            }
            is IngredientsTabEvent.DeleteIngredient -> {
                viewModelScope.launch {
                    ingredientUseCases.deleteRecipeIngredient(event.ingredient.toRecipeIngredient())
                }
            }
        }
    }

    private fun getRecipeIngredients(recipeId: Long) {
        getIngredientsJob?.cancel()
        getIngredientsJob = ingredientUseCases.getRecipeIngredientsByRecipeId(recipeId)
            .onEach { ingredients ->
                _state.value = state.value.copy(
                    ingredients = ingredients
                )
                var ifList = listOf<IngredientFocus>()
                ingredients.forEach { ingredient ->
                    ifList += IngredientFocus(
                        fullIngredient = ingredient!!,
                        focused = false
                    )
                }
                _state.value = _state.value.copy(
                    ingredientFocus = ifList
                )

                _newIngredient.value = newIngredient(recipeId)
            }
            .launchIn(viewModelScope)
    }

    private fun newIngredient(recipeId: Long): FullRecipeIngredient {
        return FullRecipeIngredient(
            recipeIngredientId = -1,
            recipeId = -1L,
            ingredientId = -1,
            measureId = -1,
            amount = "",
            measure = "",
            ingredient = "",
            ingredientName = "",
            type = ""
        )
    }
}
package com.farware.recipesaver.feature_recipe.presentation.recipes

import androidx.compose.ui.focus.FocusState
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategory
import com.farware.recipesaver.feature_recipe.domain.util.RecipeOrder
import com.farware.recipesaver.feature_recipe.domain.util.RecipeSearch

sealed class RecipesEvent {
    data class Order(val recipeOrder: RecipeOrder): RecipesEvent()
    data class Search(val recipeSearch: RecipeSearch): RecipesEvent()
    data class DeleteRecipe(val recipe: Recipe): RecipesEvent()
    data class EnteredSearch(val value: String): RecipesEvent()
    data class ChangeSearchFocus(val focusState: FocusState): RecipesEvent()
    data class NavigateToRecipeAddEdit(val recipeId: Long): RecipesEvent()
    data class NavigateToRecipe(val recipe: RecipeWithCategory): RecipesEvent()
    data class NavMenuNavigate(val route: String): RecipesEvent()
    object DeleteConfirmed: RecipesEvent()
    object DeleteCanceled: RecipesEvent()
    object ClearSearch: RecipesEvent()
    object ToggleFavorites: RecipesEvent()
    object ToggleOrderSection: RecipesEvent()
    object ToggleSearchSection: RecipesEvent()
    object NewRecipe: RecipesEvent()
}

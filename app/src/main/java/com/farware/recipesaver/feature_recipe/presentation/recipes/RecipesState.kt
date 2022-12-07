package com.farware.recipesaver.feature_recipe.presentation.recipes

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategoryAndColor
import com.farware.recipesaver.feature_recipe.domain.util.OrderType
import com.farware.recipesaver.feature_recipe.domain.util.RecipeOrder
import com.farware.recipesaver.feature_recipe.domain.util.RecipeSearch

data class RecipesState(
    var recipeOrder: RecipeOrder = RecipeOrder.Date(OrderType.Descending),
    val recipeSearch: RecipeSearch = RecipeSearch.Category(),
    val isOrderSectionVisible: Boolean = false,
    val isSearchSectionVisible: Boolean = false,
    val isSelectFavoritesOnly: Boolean = false,
    val recipes: List<RecipeWithCategoryAndColor> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val recipeToDelete: Recipe? = null,
    val showDeleteDialog: Boolean = false
)

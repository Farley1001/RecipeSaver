package com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab

import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.IngredientFocus

data class IngredientsTabState(
    val ingredients: List<FullRecipeIngredient?> = emptyList(),
    val ingredientFocus: List<IngredientFocus> = emptyList(),
)

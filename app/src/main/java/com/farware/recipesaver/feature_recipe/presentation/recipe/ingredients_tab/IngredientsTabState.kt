package com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.IngredientFocus

data class IngredientsTabState(
    val recipeIngredients: List<FullRecipeIngredient?> = emptyList(),
    val ingredientFocus: List<IngredientFocus> = emptyList(),
    val measures: List<Measure> = emptyList(),
    val allIngredients: List<Ingredient> = emptyList(),
    val showSnackbar: Boolean = false,
    val message: String = ""
)

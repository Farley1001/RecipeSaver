package com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab

import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.IngredientFocus

sealed class IngredientsTabEvent{
    data class IngredientFocusChanged(val ingredientFocus: IngredientFocus) : IngredientsTabEvent()
    data class SaveIngredient(val ingredient: IngredientFocus): IngredientsTabEvent()
    data class DeleteIngredient(val ingredient: FullRecipeIngredient): IngredientsTabEvent()
}

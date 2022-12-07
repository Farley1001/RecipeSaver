package com.farware.recipesaver.feature_recipe.presentation.recipes

data class RecipeTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)

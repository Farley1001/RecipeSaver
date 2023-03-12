package com.farware.recipesaver.feature_recipe.presentation.recipe_add_edit

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category


data class RecipeAddEditState(
    val categories: List<Category> = emptyList(),
    val selectedCategoryIndex: Int = 0,
    val newCategoryId: Long = -1,
)
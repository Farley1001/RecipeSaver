package com.farware.recipesaver.feature_recipe.presentation.recipe_add_edit

import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.CategoryWithColor

data class RecipeAddEditState(
    val categories: List<CategoryWithColor> = emptyList(),
    val selectedCategoryIndex: Int = 0,
    val newCategoryId: Long = -1,
)
package com.farware.recipesaver.feature_recipe.presentation.recipe

import com.farware.recipesaver.feature_recipe.domain.model.firebase.FbIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category

data class RecipeState(
    val newRecipeId: Long = -1,
    val showAddEditRecipeDialog: Boolean = false,
    val categories: List<Category> = emptyList(),
    val selectedCategoryIndex: Int = 0,
    val newCategoryId: Long = -1,
    val selectedTabIndex: Int = 0,
    val toggleIngredients: Boolean = false,
    val fbIngredients: List<FbIngredient>? = null,
    val isLoading: Boolean? = false,
    val error: String? = "",
    val isCategoryDialogOpen: Boolean = false
)

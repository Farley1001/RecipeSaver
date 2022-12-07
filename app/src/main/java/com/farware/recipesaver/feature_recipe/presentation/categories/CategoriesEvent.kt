package com.farware.recipesaver.feature_recipe.presentation.categories

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.domain.util.CategoryOrder
import com.farware.recipesaver.feature_recipe.presentation.recipes.RecipesEvent

sealed class CategoriesEvent {
    data class Order(val categoryOrder: CategoryOrder): CategoriesEvent()
    data class DeleteCategory(val category: Category): CategoriesEvent()
    object DeleteConfirmed: CategoriesEvent()
    object DeleteCanceled: CategoriesEvent()
    object ToggleOrderSection: CategoriesEvent()
}

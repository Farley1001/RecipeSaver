package com.farware.recipesaver.feature_recipe.presentation.categories

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.CategoryWithColor
import com.farware.recipesaver.feature_recipe.domain.util.CategoryOrder
import com.farware.recipesaver.feature_recipe.domain.util.OrderType

data class CategoriesState(
    var categoryOrder: CategoryOrder = CategoryOrder.Name(OrderType.Ascending),
    val isOrderSectionVisible: Boolean = false,
    val isSearchSectionVisible: Boolean = false,
    val isSelectFavoritesOnly: Boolean = false,
    val categories: List<CategoryWithColor> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val categoryToDelete: Category? = null,
    val showDeleteDialog: Boolean = false
)

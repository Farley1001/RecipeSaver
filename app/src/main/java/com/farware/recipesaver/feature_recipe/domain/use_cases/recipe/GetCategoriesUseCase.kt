package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.CategoryWithColor
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import com.farware.recipesaver.feature_recipe.domain.util.CategoryOrder
import com.farware.recipesaver.feature_recipe.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCategoriesUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(
        categoryOrder: CategoryOrder = CategoryOrder.Name(OrderType.Descending)
    ): Flow<List<CategoryWithColor>> {
        return repository.getCategories().map { categories ->
            when (categoryOrder.orderType) {
                is OrderType.Ascending -> {
                    when(categoryOrder) {
                        is CategoryOrder.Name -> categories.sortedBy { it.name.lowercase() }
                        is CategoryOrder.Color -> categories.sortedBy { it.categoryId }
                    }
                }
                is OrderType.Descending -> {
                    when(categoryOrder) {
                        is CategoryOrder.Name -> categories.sortedByDescending { it.name.lowercase() }
                        is CategoryOrder.Color -> categories.sortedByDescending { it.categoryId }
                    }
                }
            }
        }
    }
}
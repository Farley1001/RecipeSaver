package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategoryAndColor
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import com.farware.recipesaver.feature_recipe.domain.util.OrderType
import com.farware.recipesaver.feature_recipe.domain.util.RecipeOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchRecipesOnDirectionsUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(
        searchString: String,
        onlyFavorites: Boolean = false,
        recipeOrder: RecipeOrder = RecipeOrder.Category(OrderType.Ascending)
    ): Flow<List<RecipeWithCategoryAndColor>> {
        return repository.searchRecipesOnDirections(searchString, onlyFavorites).map { recipes ->
            when (recipeOrder.orderType) {
                is OrderType.Ascending -> {
                    when(recipeOrder) {
                        is RecipeOrder.Name -> recipes.sortedBy { it.recipe.name.lowercase() }
                        is RecipeOrder.Category -> recipes.sortedBy { it.category.category.name.lowercase() }
                        is RecipeOrder.Date -> recipes.sortedBy { it.recipe.timeStamp}
                    }
                }
                is OrderType.Descending -> {
                    when(recipeOrder) {
                        is RecipeOrder.Name -> recipes.sortedByDescending { it.recipe.name.lowercase() }
                        is RecipeOrder.Category -> recipes.sortedByDescending { it.category.category.name.lowercase() }
                        is RecipeOrder.Date -> recipes.sortedByDescending { it.recipe.timeStamp}
                    }
                }
            }
        }
    }
}
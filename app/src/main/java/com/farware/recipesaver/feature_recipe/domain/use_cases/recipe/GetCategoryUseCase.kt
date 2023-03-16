package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class GetCategoryUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id:Long): Category? {
        return repository.getCategoryById(id)
    }
}
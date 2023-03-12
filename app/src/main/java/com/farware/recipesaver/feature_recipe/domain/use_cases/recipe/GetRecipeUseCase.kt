package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategory
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class GetRecipeUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id:Long): RecipeWithCategory? {
        return repository.getRecipeById(id)
    }
}
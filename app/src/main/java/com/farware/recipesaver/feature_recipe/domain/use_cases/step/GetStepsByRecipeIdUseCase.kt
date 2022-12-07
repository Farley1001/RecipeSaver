package com.farware.recipesaver.feature_recipe.domain.use_cases.step

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetStepsByRecipeIdUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(id: Long): Flow<List<Step?>> {
        return repository.getStepsByRecipeId(id)
    }
}
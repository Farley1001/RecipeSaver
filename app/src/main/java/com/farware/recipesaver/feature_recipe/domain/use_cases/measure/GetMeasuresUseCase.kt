package com.farware.recipesaver.feature_recipe.domain.use_cases.measure

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetMeasuresUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(): Flow<List<Measure>> {
        return repository.getMeasures()
    }
}
package com.farware.recipesaver.feature_recipe.domain.use_cases.measure

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class GetMeasureByIdUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id:Int): Measure? {
        return repository.getMeasureById(id)
    }
}
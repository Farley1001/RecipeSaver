package com.farware.recipesaver.feature_recipe.domain.use_cases.step

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class DeleteStepUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(step: Step) {
        repository.deleteStep(step)
    }
}
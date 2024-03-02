package com.farware.recipesaver.feature_recipe.domain.use_cases.step

import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidStepException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class InsertStepUseCase(
    private val repository: RecipeRepository
) {
    @Throws(InvalidStepException::class)
    suspend operator fun invoke(step: Step) {
        if(step.stepNumber < 1) {
            throw InvalidStepException("The step number was not supplied.")
        }
        if(step.recipeId < 1) {
            throw InvalidStepException("The recipeId was not supplied.")
        }
        if(step.text.isBlank()){
            throw InvalidStepException("The step text was not supplied.")
        }
        repository.insertStep(step)
    }
}
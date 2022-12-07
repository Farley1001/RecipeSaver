package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.step.AddStepUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.step.DeleteStepUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.step.GetStepsByRecipeIdUseCase

data class StepUseCases(
    val getStepsByRecipeId: GetStepsByRecipeIdUseCase,
    val addStep: AddStepUseCase,
    val deleteStep: DeleteStepUseCase
)

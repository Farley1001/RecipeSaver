package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.step.DeleteStepUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.step.GetStepsByRecipeIdUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.step.InsertStepUseCase

data class StepUseCases(
    val getStepsByRecipeId: GetStepsByRecipeIdUseCase,
    val insertStep: InsertStepUseCase,
    val deleteStep: DeleteStepUseCase
)

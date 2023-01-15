package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.measure.GetMeasureByIdUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.measure.GetMeasuresUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.measure.InsertMeasureReturnIdUseCase

data class MeasureUseCases(
    val getMeasures: GetMeasuresUseCase,
    val getMeasureById: GetMeasureByIdUseCase,
    val insertMeasureReturnId: InsertMeasureReturnIdUseCase,
)
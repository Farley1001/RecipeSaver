package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.tip.AddTipUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.tip.DeleteTipUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.tip.GetTipsByRecipeIdUseCase

data class TipUseCases(
    val getTipsByRecipeId: GetTipsByRecipeIdUseCase,
    val addTip: AddTipUseCase,
    val deleteTip: DeleteTipUseCase
)

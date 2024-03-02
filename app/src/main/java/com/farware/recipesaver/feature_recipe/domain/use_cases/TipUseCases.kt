package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.tip.InsertTipUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.tip.DeleteTipUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.tip.GetTipsByRecipeIdUseCase

data class TipUseCases(
    val getTipsByRecipeId: GetTipsByRecipeIdUseCase,
    val insertTip: InsertTipUseCase,
    val deleteTip: DeleteTipUseCase
)

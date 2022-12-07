package com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TipFocus

data class TipsTabState(
    val tips: List<Tip?> = emptyList(),
    val tipsFocus: List<TipFocus> = emptyList(),
)

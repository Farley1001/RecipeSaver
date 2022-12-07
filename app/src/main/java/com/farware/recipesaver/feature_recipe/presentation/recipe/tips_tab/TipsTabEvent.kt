package com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TipFocus

sealed class TipsTabEvent {
    data class TipFocusChanged(val tipFocus: TipFocus) : TipsTabEvent()
    data class SaveTip(val tip: Tip): TipsTabEvent()
    data class DeleteTip(val tip: Tip): TipsTabEvent()
}

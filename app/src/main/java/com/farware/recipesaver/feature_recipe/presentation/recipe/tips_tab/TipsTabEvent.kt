package com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab

import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TipFocus

sealed class TipsTabEvent {
    data class TipFocusChanged(val tipFocus: TipFocus) : TipsTabEvent()
    data class DeleteTip(val tip: TipFocus): TipsTabEvent()
    object CancelConfirmDeleteTip: TipsTabEvent()
    object ConfirmDeleteTip: TipsTabEvent()
    data class EditTip(val tip: TipFocus): TipsTabEvent()
    data class EditTipTextChanged(val tipText: String): TipsTabEvent()
    object SaveEditTip: TipsTabEvent()
    object CancelEditTip: TipsTabEvent()
    object ToggleNewTipDialog: TipsTabEvent()
    data class NewTipTextChanged(val tipText: String): TipsTabEvent()
    object SaveNewTip: TipsTabEvent()
}

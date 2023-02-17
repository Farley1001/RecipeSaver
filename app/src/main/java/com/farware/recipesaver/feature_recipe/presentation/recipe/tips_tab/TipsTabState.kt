package com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TipFocus

data class TipsTabState(
    val tips: List<Tip?> = emptyList(),
    val tipFocus: List<TipFocus> = emptyList(),
    val editedTip: Tip = Tip.new(),
    val tipToDelete: Tip = Tip.new(),
    val editTipText: String = "",
    val newTipText: String = "",
    val showEditTipDialog: Boolean = false,
    val showDeleteTipDialog: Boolean = false,
    val showNewTipDialog: Boolean = false
)

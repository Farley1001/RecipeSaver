package com.farware.recipesaver.feature_recipe.presentation.category

import androidx.compose.ui.focus.FocusState

sealed class CategoryEvent {
    data class EnteredName(val value: String): CategoryEvent()
    data class ChangeNameFocus(val focusState: FocusState): CategoryEvent()
    data class CategoryNameTextChanged(val category: String): CategoryEvent()
    data class ChangeLightColor(val color: Int): CategoryEvent()
    data class ChangeDarkColor(val color: Int): CategoryEvent()
    object NavigateBack: CategoryEvent()
    object SaveCategory: CategoryEvent()
    object ToggleLightColorPicker: CategoryEvent()
    object ToggleDarkColorPicker: CategoryEvent()
}
package com.farware.recipesaver.feature_recipe.presentation.settings

import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureType
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureUnit

data class SettingsState(
    val displayEmail: String = "",
    val displayName: String = "",
    val displayNameIsDirty: Boolean = false,
    val displayNameIsEditing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = "",
    val measureType: MeasureType = MeasureType.US,
    val measureUnit: MeasureUnit = MeasureUnit.FLUID_OZ,
    val showDynamicColorOption: Boolean = false,
    val useDynamicColor: Boolean = false
)

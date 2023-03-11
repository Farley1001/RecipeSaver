package com.farware.recipesaver.feature_recipe.presentation.settings

import android.content.Context
import com.farware.recipesaver.feature_recipe.presentation.recipes.RecipesEvent

sealed class SettingsEvent {
    object EditDisplayName: SettingsEvent()
    object ClearDisplayName: SettingsEvent()
    data class NavMenuNavigate(val route: String): SettingsEvent()
    data class DisplayNameValueChange(val value: String): SettingsEvent()
    data class SaveDisplayName(val context: Context): SettingsEvent()
    data class ToggleImperialMeasure(val context: Context): SettingsEvent()
    data class ToggleMilliliterUnits(val context: Context): SettingsEvent()
    data class ToggleDynamicColor(val context: Context): SettingsEvent()
}

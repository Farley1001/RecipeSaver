package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.data_store.*

data class DataStoreUseCases(
    val getAllPreferences: GetAllPreferencesUseCase,
    val updateAllPreferences: UpdateAllPreferencesUseCase,
    val updateDisplayEmail: UpdateDisplayEmailUseCase,
    val updateDisplayName: UpdateDisplayNameUseCase,
    val updateDisplayEmailAndName: UpdateDisplayEmailAndNameUseCase,
    val getUseDynamicColor: GetUseDynamicColorUseCase,
    val updateUseDynamicColor: UpdateUseDynamicColor,
    val updateMeasureType: UpdateMeasureTypeUseCase,
    val updateMeasureUnit: UpdateMeasureUnitUseCase
)

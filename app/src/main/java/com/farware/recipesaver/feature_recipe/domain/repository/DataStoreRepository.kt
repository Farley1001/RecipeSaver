package com.farware.recipesaver.feature_recipe.domain.repository

import android.content.Context
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureType
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureUnit
import com.farware.recipesaver.feature_recipe.domain.model.data_store.Preferences

interface DataStoreRepository {
    suspend fun getAllPreferences(context: Context): Preferences
    suspend fun updateAllPreferences(context: Context, displayEmail: String, displayName: String, measureType: MeasureType, measureUnit: MeasureUnit)
    suspend fun updateUserEmail(context: Context, displayEmail: String)
    suspend fun updateUserDisplayName(context: Context, displayName: String)
    suspend fun updateUserDisplayEmailAndName(context: Context, displayEmail: String, displayName: String)
    suspend fun getUseDynamicColor(context: Context): Boolean
    suspend fun updateUseDynamicColor(context: Context, useDynamicColor: Boolean)
    suspend fun updateMeasureType(context: Context, type: MeasureType)
    suspend fun updateMeasureUnit(context: Context, unit: MeasureUnit)
}
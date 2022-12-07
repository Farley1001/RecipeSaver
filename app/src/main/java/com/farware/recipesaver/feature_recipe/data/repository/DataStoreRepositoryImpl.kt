package com.farware.recipesaver.feature_recipe.data.repository

import android.content.Context
import androidx.datastore.dataStore
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureType
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureUnit
import com.farware.recipesaver.feature_recipe.domain.model.data_store.Preferences
import com.farware.recipesaver.feature_recipe.domain.model.data_store.PreferencesSerializer
import com.farware.recipesaver.feature_recipe.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(): DataStoreRepository {

    companion object {
        private val Context.dataStore by dataStore("preferences.json", PreferencesSerializer)
    }

    override suspend fun getAllPreferences(context: Context): Preferences {
        val data = context.applicationContext.dataStore.data.first()
        return data
    }

    override suspend fun updateAllPreferences(
        context: Context,
        displayEmail: String,
        displayName: String,
        measureType: MeasureType,
        measureUnit: MeasureUnit
    ) {
        context.applicationContext.dataStore.updateData {
            it.copy(
                displayEmail = displayEmail,
                displayName = displayName,
                measureType = measureType,
                measureUnit = measureUnit
            )
        }
    }

    override suspend fun updateUserEmail(context: Context, displayEmail: String) {
        context.applicationContext.dataStore.updateData {
            it.copy(displayEmail = displayEmail)
        }
    }

    override suspend fun updateUserDisplayName(context: Context, displayName: String) {
        context.applicationContext.dataStore.updateData {
            it.copy(displayName = displayName)
        }
    }

    override suspend fun updateUserDisplayEmailAndName(
        context: Context,
        displayEmail: String,
        displayName: String
    ) {
        context.applicationContext.dataStore.updateData {
            it.copy(displayEmail = displayEmail, displayName = displayName)
        }
    }

    override suspend fun getUseDynamicColor(context: Context): Boolean {
        val data = context.applicationContext.dataStore.data.first().useDynamicColor
        return data
    }

    override suspend fun updateUseDynamicColor(context: Context, useDynamicColor: Boolean) {
        context.applicationContext.dataStore.updateData {
            it.copy(useDynamicColor = useDynamicColor)
        }
    }


    override suspend fun updateMeasureType(context: Context, type: MeasureType) {
        context.applicationContext.dataStore.updateData {
            it.copy(measureType = type)
        }
    }

    override suspend fun updateMeasureUnit(context: Context, unit: MeasureUnit) {
        context.applicationContext.dataStore.updateData {
            it.copy(measureUnit = unit)
        }
    }

}
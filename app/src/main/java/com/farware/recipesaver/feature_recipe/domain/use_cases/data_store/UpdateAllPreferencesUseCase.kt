package com.farware.recipesaver.feature_recipe.domain.use_cases.data_store

import android.content.Context
import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureType
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureUnit
import com.farware.recipesaver.feature_recipe.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateAllPreferencesUseCase @Inject constructor(
    private val repository: DataStoreRepository
){
    operator fun invoke(context: Context, displayEmail: String, displayName: String, measureType: MeasureType, measureUnit: MeasureUnit): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading<String>())
            repository.updateAllPreferences(context, displayEmail, displayName, measureType, measureUnit)
            emit(Resource.Success<String>("App preferences successfully updated."))
        } catch (e: Exception) {
            emit(Resource.Error<String>(e.localizedMessage ?: "An unexpected error occurred updating all preferences."))
        }
    }
}
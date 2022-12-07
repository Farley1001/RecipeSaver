package com.farware.recipesaver.feature_recipe.domain.use_cases.data_store

import android.content.Context
import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.model.data_store.Preferences
import com.farware.recipesaver.feature_recipe.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllPreferencesUseCase @Inject constructor(
    private val repository: DataStoreRepository
){
    operator fun invoke(context: Context): Flow<Resource<Preferences>> = flow {
        try {
            emit(Resource.Loading<Preferences>())
            val preferences = repository.getAllPreferences(context)
            emit(Resource.Success<Preferences>(preferences))
        } catch (e: Exception) {
            emit(Resource.Error<Preferences>(e.localizedMessage ?: "An unexpected error occurred getting preferences."))
        }
    }
}
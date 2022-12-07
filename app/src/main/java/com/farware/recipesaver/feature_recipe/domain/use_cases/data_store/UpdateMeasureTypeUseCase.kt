package com.farware.recipesaver.feature_recipe.domain.use_cases.data_store

import android.content.Context
import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureType
import com.farware.recipesaver.feature_recipe.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateMeasureTypeUseCase @Inject constructor(
    private val repository: DataStoreRepository
){
    operator fun invoke(context: Context, measureType: MeasureType): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading<String>())
            repository.updateMeasureType(context, measureType)
            emit(Resource.Success<String>("Measure Unit successfully updated."))
        } catch (e: Exception) {
            emit(Resource.Error<String>(e.localizedMessage ?: "An unexpected error occurred updating measure unit."))
        }
    }
}
package com.farware.recipesaver.feature_recipe.domain.use_cases.data_store

import android.content.Context
import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUseDynamicColorUseCase @Inject constructor(
    private val repository: DataStoreRepository
){
    operator fun invoke(context: Context): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            val result = repository.getUseDynamicColor(context)
            emit(Resource.Success<Boolean>(result))
        } catch (e: Exception) {
            emit(Resource.Error<Boolean>(e.localizedMessage ?: "An unexpected error occurred getting the UseDynamicColor setting."))
        }
    }
}
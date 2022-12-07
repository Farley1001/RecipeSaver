package com.farware.recipesaver.feature_recipe.domain.use_cases.data_store

import android.content.Context
import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateDisplayNameUseCase @Inject constructor(
    private val repository: DataStoreRepository
){
    operator fun invoke(context: Context, displayName: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading<String>())
            repository.updateUserDisplayName(context, displayName)
            emit(Resource.Success<String>("Display Name successfully updated."))
        } catch (e: Exception) {
            emit(Resource.Error<String>(e.localizedMessage ?: "An unexpected error occurred updating display name."))
        }
    }
}
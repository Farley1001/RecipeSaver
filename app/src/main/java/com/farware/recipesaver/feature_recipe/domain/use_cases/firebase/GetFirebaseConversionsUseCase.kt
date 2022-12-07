package com.farware.recipesaver.feature_recipe.domain.use_cases.firebase

import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.data.remote.dto.toConversion
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Conversion
import com.farware.recipesaver.feature_recipe.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetFirebaseConversionsUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(): Flow<Resource<List<Conversion>>> = flow {
        try {
            emit(Resource.Loading<List<Conversion>>())
            val conversions = repository.getAllFirebaseConversions().map { it!!.toConversion()}
            emit(Resource.Success<List<Conversion>>(conversions))
        } catch(e: Exception) {
            emit(Resource.Error<List<Conversion>>(e.localizedMessage ?: "An unexpected error occurred getting conversions from firebase."))
        } catch(e: IOException) {
            emit(Resource.Error<List<Conversion>>("Couldn't reach server. Check your internet connection."))
        }
    }
}
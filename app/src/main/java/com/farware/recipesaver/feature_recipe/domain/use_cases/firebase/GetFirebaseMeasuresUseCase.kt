package com.farware.recipesaver.feature_recipe.domain.use_cases.firebase

import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.data.remote.dto.toMeasure
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure
import com.farware.recipesaver.feature_recipe.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetFirebaseMeasuresUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(): Flow<Resource<List<Measure>>> = flow {
        try {
            emit(Resource.Loading<List<Measure>>())
            val measures = repository.getAllFirebaseMeasures().map { it!!.toMeasure()}
            emit(Resource.Success<List<Measure>>(measures))
        } catch(e: Exception) {
            emit(Resource.Error<List<Measure>>(e.localizedMessage ?: "An unexpected error occurred getting measures from firebase."))
        } catch(e: IOException) {
            emit(Resource.Error<List<Measure>>("Couldn't reach server. Check your internet connection."))
        }
    }
}
package com.farware.recipesaver.feature_recipe.domain.use_cases.firebase

import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.data.remote.dto.toFbIngredient
import com.farware.recipesaver.feature_recipe.domain.model.firebase.FbIngredient
import com.farware.recipesaver.feature_recipe.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetFirebaseIngredientsUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(): Flow<Resource<List<FbIngredient>>> = flow {
        try {
            emit(Resource.Loading<List<FbIngredient>>())
            val ingredients = repository.getAllFirebaseIngredients().map { it!!.toFbIngredient() }
            emit(Resource.Success<List<FbIngredient>>(ingredients))
        } catch(e: Exception) {
            emit(Resource.Error<List<FbIngredient>>(e.localizedMessage ?: "An unexpected error occurred getting ingredients from firebase."))
        } catch(e: IOException) {
            emit(Resource.Error<List<FbIngredient>>("Couldn't reach server. Check your internet connection."))
        }
    }
}
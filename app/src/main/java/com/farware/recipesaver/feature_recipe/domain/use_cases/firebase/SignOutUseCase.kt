package com.farware.recipesaver.feature_recipe.domain.use_cases.firebase

import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(): Flow<Resource<String?>> = flow {
        try {
            emit(Resource.Loading<String?>())
            repository.signOut()
            emit(Resource.Success<String?>("Sign out successful"))
        } catch (e: Exception) {
            emit(
                Resource.Error<String?>(
                    e.localizedMessage ?: "An unexpected error occurred during sign out."
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<String?>("Couldn't reach server. Check your internet connection."))
        }
    }
}
package com.farware.recipesaver.feature_recipe.domain.use_cases.firebase

import com.farware.recipesaver.feature_recipe.common.Resource
import com.farware.recipesaver.feature_recipe.domain.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    operator fun invoke(token: String): Flow<Resource<FirebaseUser?>> = flow {
        try {
            emit(Resource.Loading<FirebaseUser?>())
            val user = repository.signInWithGoogle(token).user
            emit(Resource.Success<FirebaseUser?>(user))
        } catch(e: Exception) {
            emit(Resource.Error<FirebaseUser?>(e.localizedMessage ?: "An unexpected error occurred during sign-in with google."))
        } catch(e: IOException) {
            emit(Resource.Error<FirebaseUser?>("Couldn't reach server. Check your internet connection."))
        }
    }
}
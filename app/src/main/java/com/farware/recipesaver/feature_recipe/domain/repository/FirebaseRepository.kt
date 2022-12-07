package com.farware.recipesaver.feature_recipe.domain.repository

import com.farware.recipesaver.feature_recipe.data.remote.dto.FbConversionDto
import com.farware.recipesaver.feature_recipe.data.remote.dto.FbIngredientDto
import com.farware.recipesaver.feature_recipe.data.remote.dto.FbMeasureDto
import com.google.firebase.auth.AuthResult


interface FirebaseRepository {
    suspend fun getAllFirebaseMeasures(): List<FbMeasureDto?>
    suspend fun getAllFirebaseConversions(): List<FbConversionDto?>
    suspend fun getAllFirebaseIngredients(): List<FbIngredientDto?>
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult
    suspend fun signInWithGoogle(token: String): AuthResult
    suspend fun signOut()
}
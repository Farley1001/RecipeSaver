package com.farware.recipesaver.feature_recipe.data.repository

import com.farware.recipesaver.feature_recipe.data.remote.dto.FbConversionDto
import com.farware.recipesaver.feature_recipe.data.remote.dto.FbIngredientDto
import com.farware.recipesaver.feature_recipe.data.remote.dto.FbMeasureDto
import com.farware.recipesaver.feature_recipe.domain.repository.FirebaseRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseRepositoryImpl @Inject constructor(): FirebaseRepository {

    private val database = Firebase.database

    override suspend fun getAllFirebaseIngredients(): List<FbIngredientDto?> {
        val ref = database.getReference("ingredients")
        val snap = ref.get().await().children.map { snapShot ->
            snapShot.getValue(FbIngredientDto::class.java)
        }

        return snap
    }

    override suspend fun getAllFirebaseConversions(): List<FbConversionDto?> {
        val ref = database.getReference("conversions")
        val snap = ref.get().await().children.map { snapShot ->
            snapShot.getValue(FbConversionDto::class.java)
        }

        return snap
    }

    override suspend fun getAllFirebaseMeasures(): List<FbMeasureDto?> {
        val ref = database.getReference("measures")
        val snap = ref.get().await().children.map { snapShot ->
            snapShot.getValue(FbMeasureDto::class.java)
        }

        return snap
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult {
        val auth: FirebaseAuth = Firebase.auth

        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult {
        val auth: FirebaseAuth = Firebase.auth

        return auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signInWithGoogle(token: String): AuthResult {
        val auth: FirebaseAuth = Firebase.auth

        val credential = GoogleAuthProvider.getCredential(token, null)

        return auth.signInWithCredential(credential).await()
    }

    override suspend fun signOut() {
        val auth: FirebaseAuth = Firebase.auth
        auth.signOut()
    }
}
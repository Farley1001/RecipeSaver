package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.firebase.*

data class FirebaseUseCases(
    val getFirebaseMeasures: GetFirebaseMeasuresUseCase,
    val getFirebaseConversions: GetFirebaseConversionsUseCase,
    val getFirebaseIngredients: GetFirebaseIngredientsUseCase,
    val createUserWithEmailAndPassword: CreateUserWithEmailAndPasswordUseCase,
    val signInWithEmailAndPassword: SignInWithEmailAndPasswordUseCase,
    val signInWithGoogle: SignInWithGoogleUseCase,
    val signOut: SignOutUseCase
)

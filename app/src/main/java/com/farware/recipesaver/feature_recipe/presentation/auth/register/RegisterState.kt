package com.farware.recipesaver.feature_recipe.presentation.auth.register

import com.google.firebase.auth.FirebaseUser

data class RegisterState(
    val isLoading: Boolean = false,
    val error: String = "",
    val currentUser: FirebaseUser? = null
)

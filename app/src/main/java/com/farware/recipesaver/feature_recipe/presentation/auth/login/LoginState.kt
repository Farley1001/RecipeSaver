package com.farware.recipesaver.feature_recipe.presentation.auth.login

import com.google.firebase.auth.FirebaseUser

data class LoginState(
    val isLoading: Boolean = false,
    val error: String = "",
    val currentUser: FirebaseUser? = null
)

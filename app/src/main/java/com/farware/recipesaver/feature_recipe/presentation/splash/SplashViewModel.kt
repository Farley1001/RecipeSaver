package com.farware.recipesaver.feature_recipe.presentation.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.presentation.navigation.AppNavigator
import com.farware.recipesaver.feature_recipe.presentation.navigation.Destination
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth

    var currentUser by mutableStateOf<FirebaseUser?>(auth.currentUser)

    init {
        viewModelScope.launch {
            delay(1000L)
            if (currentUser != null) {
                appNavigator.tryNavigateTo(route = Destination.RecipesScreen(), popUpToRoute = "recipe_feature", inclusive = true)
                currentUser = null
            } else {
                appNavigator.tryNavigateTo(Destination.LoginScreen())
            }
        }
    }
}
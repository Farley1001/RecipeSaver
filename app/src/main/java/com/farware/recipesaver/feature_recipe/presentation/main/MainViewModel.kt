package com.farware.recipesaver.feature_recipe.presentation.main

import androidx.lifecycle.ViewModel
import com.farware.recipesaver.feature_recipe.presentation.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    appNavigator: AppNavigator
) : ViewModel() {

    val navigationChannel = appNavigator.navigationChannel
}
package com.farware.recipesaver.feature_recipe.presentation.main

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.farware.recipesaver.feature_recipe.presentation.auth.login.LoginScreen
import com.farware.recipesaver.feature_recipe.presentation.auth.register.RegisterScreen
import com.farware.recipesaver.feature_recipe.presentation.categories.CategoriesScreen
import com.farware.recipesaver.feature_recipe.presentation.category.CategoryScreen
import com.farware.recipesaver.feature_recipe.presentation.navigation.Destination
import com.farware.recipesaver.feature_recipe.presentation.navigation.NavHost
import com.farware.recipesaver.feature_recipe.presentation.navigation.NavigationIntent
import com.farware.recipesaver.feature_recipe.presentation.navigation.composable
import com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeScreen
import com.farware.recipesaver.feature_recipe.presentation.recipe_add_edit.RecipeAddEditScreen
import com.farware.recipesaver.feature_recipe.presentation.recipes.RecipesScreen
import com.farware.recipesaver.feature_recipe.presentation.settings.SettingsScreen
import com.farware.recipesaver.feature_recipe.presentation.splash.SplashScreen
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.RecipeSaverTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    NavigationEffects(
        navigationChannel = mainViewModel.navigationChannel,
        navHostController = navController
    )

    RecipeSaverTheme {

        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = Destination.SplashScreen
            ) {
                composable(destination = Destination.SplashScreen) {
                    SplashScreen()
                }
                composable(destination = Destination.LoginScreen) {
                    LoginScreen()
                }
                composable(destination = Destination.RegisterScreen) {
                    RegisterScreen()
                }
                composable(destination = Destination.SettingsScreen) {
                    SettingsScreen(navDrawerState = navDrawerState)
                }
                composable(destination = Destination.RecipesScreen) {
                    RecipesScreen(navDrawerState = navDrawerState)
                }
                composable(
                    destination = Destination.RecipeScreen,
                    arguments = listOf(
                        navArgument(
                            name = "recipeId"
                        ) {
                            type = NavType.LongType
                            defaultValue = -1
                            nullable = false
                        }
                    )
                ) {
                    RecipeScreen(snackbarHostState = snackbarHostState)
                }
                composable(
                    destination = Destination.RecipeAddEditScreen,
                    arguments = listOf(
                        navArgument(
                            name = "recipeId"
                        ) {
                            type = NavType.LongType
                            defaultValue = -1
                            nullable = false
                        }
                    )
                ) {
                    RecipeAddEditScreen(snackbarHostState = snackbarHostState)
                }
                composable(destination = Destination.CategoriesScreen) {
                    CategoriesScreen(navDrawerState = navDrawerState)
                }
                composable(
                    destination = Destination.CategoryScreen,
                    arguments = listOf(
                        navArgument(
                            name = "categoryId"
                        ) {
                            type = NavType.LongType
                            defaultValue = -1
                            nullable = false
                        }
                    )
                ) {
                    CategoryScreen(snackbarHostState = snackbarHostState)
                }
            }
        }
    }
}

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>,
    navHostController: NavHostController
) {
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity?.isFinishing == true) {
                return@collect
            }
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }
                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                        }
                    }
                }
            }
        }
    }
}
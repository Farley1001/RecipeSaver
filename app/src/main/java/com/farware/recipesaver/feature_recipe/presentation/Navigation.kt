package com.farware.recipesaver.feature_recipe.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.farware.recipesaver.feature_recipe.presentation.auth.login.LoginScreen
import com.farware.recipesaver.feature_recipe.presentation.auth.register.RegisterScreen
import com.farware.recipesaver.feature_recipe.presentation.categories.CategoriesScreen
import com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeScreen
import com.farware.recipesaver.feature_recipe.presentation.recipes.RecipesScreen
import com.farware.recipesaver.feature_recipe.presentation.settings.SettingsScreen
import com.farware.recipesaver.feature_recipe.presentation.splash.SplashScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(navController = navController, navDrawerState = navDrawerState)
        }
        composable(route = Screen.RecipesScreen.route) {
            RecipesScreen(navController = navController, navDrawerState = navDrawerState)
        }
        composable(
            route = Screen.RecipeScreen.route + "/{recipeId}",
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
            RecipeScreen(
                navController = navController, snackbarHostState = snackbarHostState
            )
        }
        composable(
            route = Screen.AddEditRecipeScreen.route + "/{recipeId}",
            arguments = listOf(
                navArgument(
                    name = "recipeId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                    nullable = false
                }
            )
        ) {
            //Screen.AddEditRecipeScreen(
            //    navController = navController,
            //    recipeId = it.arguments!!.getInt("recipeId")
            //)
        }
        composable(route = Screen.CategoriesScreen.route) {
            CategoriesScreen(navController = navController, navDrawerState = navDrawerState)
        }
    }
}
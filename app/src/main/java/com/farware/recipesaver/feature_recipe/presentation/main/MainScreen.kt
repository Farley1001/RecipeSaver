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
import androidx.lifecycle.ViewModel
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import com.farware.recipesaver.feature_recipe.presentation.auth.login.LoginScreen
import com.farware.recipesaver.feature_recipe.presentation.auth.register.RegisterScreen
import com.farware.recipesaver.feature_recipe.presentation.categories.CategoriesScreen
import com.farware.recipesaver.feature_recipe.presentation.category.CategoryScreen
import com.farware.recipesaver.feature_recipe.presentation.navigation.Destination
import com.farware.recipesaver.feature_recipe.presentation.navigation.NavHost
import com.farware.recipesaver.feature_recipe.presentation.navigation.NavigationIntent
import com.farware.recipesaver.feature_recipe.presentation.navigation.composable
import com.farware.recipesaver.feature_recipe.presentation.permissions.PermissionsScreen
import com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeScreen
import com.farware.recipesaver.feature_recipe.presentation.recipe_add_edit.RecipeAddEditScreen
import com.farware.recipesaver.feature_recipe.presentation.recipes.RecipesScreen
import com.farware.recipesaver.feature_recipe.presentation.settings.SettingsScreen
import com.farware.recipesaver.feature_recipe.presentation.share_recipe.ShareRecipeScreen
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

    /*
        to share a viewmodel between screens you will need to use the following pattern

        composable(destination = Destination.LoginScreen) {
            val viewModel = it.sharedViewModel<AuthViewModel>(navController = navController)
            LoginScreen(viewModel = viewModel)
            //LoginScreen()
        }
        composable(destination = Destination.RegisterScreen) {
            val viewModel = it.sharedViewModel<AuthViewModel>(navController = navController)
            RegisterScreen(viewModel = viewModel)
            //RegisterScreen()
        }
    */

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
                navigation(
                    startDestination = "login_screen",
                    route = "auth"
                ) {

                    composable(destination = Destination.LoginScreen) {
                        LoginScreen()
                    }
                    composable(destination = Destination.RegisterScreen) {
                        RegisterScreen()
                    }
                }
                navigation(
                    startDestination = "recipes_screen",
                    route = "recipe_feature"
                ) {
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
                    composable(
                        destination = Destination.PermissionsScreen,
                        arguments = listOf(
                            navArgument(
                                name = "permissionType"
                            ) {
                                type = NavType.StringType
                                defaultValue = ""
                                nullable = false
                            },
                            navArgument(
                                name = "successPath"
                            ) {
                                type = NavType.StringType
                                defaultValue = ""
                                nullable = false
                            },
                            navArgument(
                                name = "declinePath"
                            ) {
                                type = NavType.StringType
                                defaultValue = ""
                                nullable = false
                            }
                        )
                    ) {
                        PermissionsScreen()
                    }
                    composable(
                        destination = Destination.ShareRecipeScreen,
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
                        ShareRecipeScreen()
                    }
                    composable(destination = Destination.SettingsScreen) {
                        SettingsScreen(navDrawerState = navDrawerState)
                    }
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

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}
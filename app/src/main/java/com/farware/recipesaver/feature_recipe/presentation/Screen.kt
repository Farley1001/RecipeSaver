package com.farware.recipesaver.feature_recipe.presentation

sealed class Screen(val route: String) {
    object AddEditRecipeScreen: Screen("add_edit_recipe_screen")
    object CategoriesScreen: Screen("categories_screen")
    object LoginScreen: Screen("login_screen")
    object SettingsScreen: Screen("settings_screen")
    object RecipeScreen: Screen("recipe_screen")
    object RecipesScreen: Screen("recipes_screen")
    object RegisterScreen: Screen("register_screen")
    object SplashScreen: Screen("splash_screen")

    // mandatory arguments only
    fun withArgs(vararg args: String): String {
        val string =  buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
        return string
    }
}
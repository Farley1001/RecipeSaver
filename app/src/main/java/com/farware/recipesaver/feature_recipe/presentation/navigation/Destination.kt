package com.farware.recipesaver.feature_recipe.presentation.navigation

sealed class Destination(protected val route: String, vararg params: String) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }
    object SplashScreen: NoArgumentsDestination("splash_screen")
    object CategoriesScreen: NoArgumentsDestination("categories_screen")
    object LoginScreen: NoArgumentsDestination("login_screen")
    object RecipeAddEditScreen: Destination("recipe_add_edit_screen", "recipeId") {
        const val RECIPE_ID_KEY = "recipeId"

        operator fun invoke(recipeId: Long): String = route.appendParams(
            RECIPE_ID_KEY to recipeId
        )
    }
    object RecipeScreen: Destination("recipe_screen", "recipeId") {
        const val RECIPE_ID_KEY = "recipeId"

        operator fun invoke(recipeId: Long): String = route.appendParams(
            RECIPE_ID_KEY to recipeId
        )
    }
    object RecipesScreen: NoArgumentsDestination("recipes_screen")
    object RegisterScreen: NoArgumentsDestination("register_screen")
    object SettingsScreen: NoArgumentsDestination("settings_screen")
}

internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}
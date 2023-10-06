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
    object CategoryScreen: Destination("category_screen", "categoryId") {
        const val CATEGORY_ID_KEY = "categoryId"

        operator fun invoke(categoryId: Long): String = route.appendParams(
            CATEGORY_ID_KEY to categoryId
        )
    }
    object CategoriesScreen: NoArgumentsDestination("categories_screen")
    object LoginScreen: NoArgumentsDestination("login_screen")
    /*object RecipeAddEditScreen: Destination("recipe_add_edit_screen", "recipeId") {
        const val RECIPE_ID_KEY = "recipeId"

        operator fun invoke(recipeId: Long): String = route.appendParams(
            RECIPE_ID_KEY to recipeId
        )
    }*/
    object RecipeScreen: Destination("recipe_screen", "recipeId") {
        const val RECIPE_ID_KEY = "recipeId"

        operator fun invoke(recipeId: Long): String = route.appendParams(
            RECIPE_ID_KEY to recipeId
        )
    }
    object RecipesScreen: NoArgumentsDestination("recipes_screen")
    object RegisterScreen: NoArgumentsDestination("register_screen")
    object SettingsScreen: NoArgumentsDestination("settings_screen")
    object PermissionsScreen: Destination("permissions_screen", "permissionType","successPath", "declinePath") {
        private const val PERMISSION_TYPE_KEY = "permissionType"
        private const val SUCCESS_KEY = "successPath"
        private const val DECLINE_KEY = "declinePath"

        operator fun invoke(permissionType: String, successPath: String, declinePath: String): String = route.appendParams(
            PERMISSION_TYPE_KEY to permissionType,
            SUCCESS_KEY to successPath,
            DECLINE_KEY to declinePath
        )
    }
    object ShareRecipeScreen: Destination("share_recipe_screen", "recipeId") {
        private const val RECIPE_ID_KEY = "recipeId"

        operator fun invoke(recipeId: Long): String = route.appendParams(
            RECIPE_ID_KEY to recipeId
        )
    }
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
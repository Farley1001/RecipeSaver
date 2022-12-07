package com.farware.recipesaver.feature_recipe.domain.util

sealed class RecipeSearch(
) {
    class Name(): RecipeSearch()
    class Category(): RecipeSearch()
    class Ingredients(): RecipeSearch()
    class Directions(): RecipeSearch()


    fun copy(): RecipeSearch {
        return when(this) {
            is Name -> Name()
            is Category -> Category()
            is Ingredients -> Ingredients()
            is Directions -> Directions()
        }
    }
}

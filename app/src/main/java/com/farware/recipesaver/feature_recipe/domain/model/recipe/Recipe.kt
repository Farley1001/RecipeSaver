package com.farware.recipesaver.feature_recipe.domain.model.recipe

data class Recipe(
    val recipeId: Long? = null,
    val categoryId: Long,
    val name: String,
    val description: String,
    val imagePath: String? = null,
    val prepTime: Long? = 0,
    val cookTime: Long? = 0,
    val favorite: Boolean? = false,
    val timeStamp: Long,
) {
    /**
     * create any functions that are needed to operate on this entity
     */
    /*fun shouldBeWatered(since: Calendar, lastWateringDate: Calendar) =
        since > lastWateringDate.apply { add(DAY_OF_YEAR, wateringInterval) }*/

    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}

class InvalidRecipeException(message: String): Exception(message)

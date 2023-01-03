package com.farware.recipesaver.feature_recipe.domain.model.recipe

data class Ingredient(
    val ingredientId: Int,
    val name: String,
    val type: String
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

class InvalidIngredientException(message: String): Exception(message)

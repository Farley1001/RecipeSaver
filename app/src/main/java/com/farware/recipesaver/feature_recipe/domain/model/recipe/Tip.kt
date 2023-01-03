package com.farware.recipesaver.feature_recipe.domain.model.recipe

data class Tip(
    val tipId: Long? = null,
    val recipeId: Long,
    val tipNumber: Int,
    val imagePath: String? = null,
    val text: String
) {
    /**
     * create any functions that are needed to operate on this entity
     */
    /*fun shouldBeWatered(since: Calendar, lastWateringDate: Calendar) =
        since > lastWateringDate.apply { add(DAY_OF_YEAR, wateringInterval) }*/

    /**
     * override toString() to return name as the default
     */
    override fun toString() = text
}

class InvalidTipException(message: String): Exception(message)

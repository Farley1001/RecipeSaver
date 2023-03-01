package com.farware.recipesaver.feature_recipe.domain.model.recipe

data class Measure(
    val measureId: Long? = null,
    val name: String
){
    companion object {
        fun new(): Measure {
            return Measure(
                measureId = null,
                name = ""
            )
        }
    }
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

class InvalidMeasureException(message: String): Exception(message)

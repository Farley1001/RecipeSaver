package com.farware.recipesaver.feature_recipe.domain.model.recipe

data class Measure(
    val measureId: Int? = null,
    val name: String,
    val shortName: String
){
    companion object {
        fun new(): Measure {
            return Measure(
                measureId = null,
                name = "",
                shortName = ""
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

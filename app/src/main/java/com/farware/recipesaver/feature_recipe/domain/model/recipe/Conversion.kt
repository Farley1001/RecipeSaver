package com.farware.recipesaver.feature_recipe.domain.model.recipe

data class Conversion(
    val conversionId: Int,
    val name: String,
    val shortName: String,
    val size: Float,
    val sizeText: String,
    val flozUs: Float,
    val flozImp: Float,
    val mlUs: Float,
    val mlImp: Float
){
    /**
     * create any functions that are needed to operate on this entity
     */

    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}

class InvalidConversionException(message: String): Exception(message)

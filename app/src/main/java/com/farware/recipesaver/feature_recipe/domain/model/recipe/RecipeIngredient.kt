package com.farware.recipesaver.feature_recipe.domain.model.recipe

import java.math.RoundingMode

data class RecipeIngredient(
    val recipeIngredientId: Long? = null,
    val recipeId: Long,
    val ingredientId: Int,
    val measureId: Int,
    val amount: String,
    val measure: String,
    val ingredient: String
) {
    /**
     * create any functions that are needed to operate on this entity
     */
    private fun amountToString(amount: Float): String {
        var result: String
        val s = amount.toString()
        val i = s.split(".")


        if(i[1].startsWith("0")) {
            // decimal is to small return 0
            result = i[0]
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.125f) {
            // has a decimal of <= .125
            result = i[0] + " 1/8"
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.25f) {
            // has a decimal of <= .25
            result = i[0] + " 1/4"
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.333f) {
            // has a decimal of <= .333
            result = i[0] + " 1/3"
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.5f) {
            // has a decimal of <= .5
            result = i[0] + " 1/2"
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.666f) {
            // has a decimal of <= .666
            result = i[0] + " 2/3"
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.75f) {
            // has a decimal of <= .75
            result = i[0] + " 3/4"
        } else {
            result = i[0]
        }


        return result
    }


    /*
     * override toString() to return amount as the default
     */
    override fun toString() = "$amount $measure $ingredient"
}

class InvalidRecipeIngredientException(message: String): Exception(message)

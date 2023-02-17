package com.farware.recipesaver.feature_recipe.domain.model.recipe

data class Tip(
    val tipId: Long? = null,
    val recipeId: Long,
    val tipNumber: Int,
    val imagePath: String? = null,
    val text: String
) {
    companion object {
        fun new(): Tip {
            return Tip(
                tipId = null,
                recipeId = -1L,
                tipNumber = -1,
                imagePath = null,
                text = ""
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */


    /**
     * override toString() to return name as the default
     */
    override fun toString() = text
}

class InvalidTipException(message: String): Exception(message)

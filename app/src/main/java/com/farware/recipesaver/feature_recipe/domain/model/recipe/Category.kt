package com.farware.recipesaver.feature_recipe.domain.model.recipe

data class Category(
    val categoryId: Long? = null,
    val name: String,
    val colorId: Long,
    val timeStamp: Long,
) {
    /**
     * create any functions that are needed to operate on this entity
     */


    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}

class InvalidCategoryException(message: String): Exception(message)

package com.farware.recipesaver.feature_recipe.domain.util

sealed class RecipeOrder(
    val orderType: OrderType
) {
    class Name(orderType: OrderType): RecipeOrder(orderType)
    class Category(orderType: OrderType): RecipeOrder(orderType)
    class Date(orderType: OrderType): RecipeOrder(orderType)


    fun copy(orderType: OrderType): RecipeOrder {
        return when(this) {
            is Name -> Name(orderType)
            is Category -> Category(orderType)
            is Date -> Date(orderType)
        }
    }
}

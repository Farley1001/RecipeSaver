package com.farware.recipesaver.feature_recipe.domain.util

sealed class CategoryOrder(
    val orderType: OrderType
) {
    class Name(orderType: OrderType): CategoryOrder(orderType)
    class Color(orderType: OrderType): CategoryOrder(orderType)


    fun copy(orderType: OrderType): CategoryOrder {
        return when(this) {
            is Name -> Name(orderType)
            is Color -> Color(orderType)
        }
    }
}

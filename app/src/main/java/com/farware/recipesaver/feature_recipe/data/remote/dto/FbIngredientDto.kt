package com.farware.recipesaver.feature_recipe.data.remote.dto

import com.farware.recipesaver.feature_recipe.domain.model.firebase.FbIngredient
import com.squareup.moshi.Json

data class FbIngredientDto(
    @field:Json(name = "Id") val Id: Long = -1,
    @field:Json(name = "Name") val Name: String = "",
    @field:Json(name = "Type") val Type: String = ""
)

fun FbIngredientDto.toFbIngredient(): FbIngredient {
    return FbIngredient(
        id = Id,
        name = Name,
        type = Type
    )
}

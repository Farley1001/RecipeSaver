package com.farware.recipesaver.feature_recipe.data.remote.dto

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure
import com.squareup.moshi.Json

data class FbMeasureDto(
    @field:Json(name = "Id") val Id: Int = -1,
    @field:Json(name = "Name") val Name: String = "",
    @field:Json(name = "ShortName") val ShortName: String = "",
)

fun FbMeasureDto.toMeasure(): Measure {
    return Measure(
        measureId = Id.toLong(),
        name = Name
    )
}


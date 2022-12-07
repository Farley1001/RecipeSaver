package com.farware.recipesaver.feature_recipe.domain.model.data_store

import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
    val displayName: String = "",
    val displayEmail: String = "",
    val useDynamicColor: Boolean = false,
    val measureType: MeasureType = MeasureType.US,
    val measureUnit: MeasureUnit = MeasureUnit.FLUID_OZ,
    //val knownLocations: PersistentList<Location> = persistentListOf()
)

enum class MeasureType {
    US, IMPERIAL
}

enum class MeasureUnit {
    FLUID_OZ, MILLILITERS
}

/*
@Serializable
data class Location(
    val lat: Double,
    val lng: Double
)
*/

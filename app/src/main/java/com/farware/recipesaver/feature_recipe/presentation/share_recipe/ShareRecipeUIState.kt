package com.farware.recipesaver.feature_recipe.presentation.share_recipe

import com.farware.recipesaver.feature_recipe.domain.bluetooth.BluetoothDevice
import com.farware.recipesaver.feature_recipe.domain.bluetooth.BluetoothMessage
import com.farware.recipesaver.feature_recipe.domain.model.recipe.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategory

data class ShareRecipeUiState(
    val recipeId: Long = 0,
    val recipe: RecipeWithCategory = RecipeWithCategory.new(),
    val recipes: List<RecipeWithCategory?> = emptyList(),
    val categories: List<Category> = emptyList(),
    val steps: List<Step?> = emptyList(),
    val tips: List<Tip?> = emptyList(),
    val ingredients: List<FullRecipeIngredient?> = emptyList(),
    val allIngredients: List<Ingredient?> = emptyList(),
    val allMeasures: List<Measure?> = emptyList(),
    val sendString: String = "",
    val deviceName: String = "",
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val allDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val messages: List<BluetoothMessage> = emptyList(),
    val partialMessage: String = "",
    val messageSize: Int = 0
)
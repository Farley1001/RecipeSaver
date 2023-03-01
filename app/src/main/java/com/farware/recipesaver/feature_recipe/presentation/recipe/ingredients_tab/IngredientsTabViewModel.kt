package com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure
import com.farware.recipesaver.feature_recipe.domain.model.recipe.RecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeIngredientWithIngredient
import com.farware.recipesaver.feature_recipe.domain.use_cases.IngredientUseCases
import com.farware.recipesaver.feature_recipe.domain.use_cases.MeasureUseCases
import com.farware.recipesaver.feature_recipe.domain.use_cases.RecipeIngredientUseCases
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.IngredientFocus
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.MatchTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale.filter
import javax.inject.Inject

@HiltViewModel
class IngredientsTabViewModel @Inject constructor(
    private val recipeIngredientUseCases: RecipeIngredientUseCases,
    private val measureUseCases: MeasureUseCases,
    private val ingredientUseCases: IngredientUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private var _state =  mutableStateOf(IngredientsTabState())
    val state: State<IngredientsTabState> = _state

    private var _measure = mutableStateOf(Measure(-1, ""))
    val measure: State<Measure> = _measure

    private var _ingredient = mutableStateOf(Ingredient(-1, "", ""))
    val ingredient: State<Ingredient> = _ingredient

    private var _recipeIngredient = mutableStateOf(RecipeIngredient(null, -1, -1, -1, "", "", ""))
    val recipeIngredient: State<RecipeIngredient> = _recipeIngredient

    private var _recipeIngredientWithIngredient = mutableStateOf(RecipeIngredientWithIngredient(null, -1, -1, -1, "", "", "", "", ""))
    val recipeIngredientWithIngredient: State<RecipeIngredientWithIngredient> = _recipeIngredientWithIngredient

    private var _newFullRecipeIngredient = mutableStateOf(FullRecipeIngredient.new())
    val newFullRecipeIngredient: State<FullRecipeIngredient> = _newFullRecipeIngredient

    private var _newFullIngredient = mutableStateOf(IngredientFocus(newFullRecipeIngredient.value, "", "", false))
    val newFullIngredient: State<IngredientFocus> = _newFullIngredient

    private var getRecipeIngredientsJob: Job? = null
    private var getMeasuresJob: Job? = null
    private var getIngredientsJob: Job? = null

    init {
        // get all ingredients for this selected recipe
        savedStateHandle.get<Long>("recipeId")?.let { recipeId ->
            if(recipeId != -1L) {
                getRecipeIngredients(recipeId)
            }
        }
        // get all measures to match for any changes and adds
        getMeasures()
        // get all ingredients to match for any changes and adds
        getIngredients()


    }

    fun onEvent(event: IngredientsTabEvent) {
        when (event) {
            is IngredientsTabEvent.IngredientFocusChanged -> {
                // used to display the edit and delete icons on the ingredients chip
                var mf = state.value.ingredientFocus
                mf = mf.map { item ->
                    if(item.fullIngredient.ingredientId == event.ingredientFocus.fullIngredient.ingredientId)
                        item.copy(focused = true)
                    else
                        item.copy(focused = false)
                }
                _state.value = state.value.copy(
                    ingredientFocus = mf
                )
            }
            is IngredientsTabEvent.DeleteIngredient -> {
                var mf = state.value.ingredientFocus
                mf = mf.map { item ->
                    if(item.fullIngredient.ingredientId == event.ingredient.fullIngredient.ingredientId) {
                        item.copy(
                            focused = true
                        )
                    }
                    else {
                        item.copy(focused = false)
                    }
                }

                _state.value = state.value.copy(
                    showDeleteIngredientDialog = true,
                    ingredientToDelete = event.ingredient.fullIngredient
                )
            }
            is IngredientsTabEvent.CancelConfirmDeleteIngredient -> {
                _state.value = state.value.copy(
                    showDeleteIngredientDialog = false
                )
            }
            is IngredientsTabEvent.ConfirmDeleteIngredient -> {
                _state.value = state.value.copy(
                    showDeleteIngredientDialog = false
                )
                viewModelScope.launch {
                    recipeIngredientUseCases.deleteRecipeIngredient(state.value.ingredientToDelete.toRecipeIngredient())
                }
            }
            is IngredientsTabEvent.EditIngredient -> {
                var mf = state.value.ingredientFocus
                mf = mf.map { item ->
                    if(item.fullIngredient.ingredientId == event.ingredient.fullIngredient.ingredientId) {
                        item.copy(
                            focused = true
                        )
                    }
                    else {
                        item.copy(focused = false)
                    }
                }
                _state.value = state.value.copy(
                    showEditIngredientDialog = true,
                    ingredientFocus = mf,
                    editedIngredient = event.ingredient.fullIngredient,
                    editIngredientText = event.ingredient.fullIngredient.ingredient,
                    editAmountText = event.ingredient.fullIngredient.amount,
                    editMeasureText = event.ingredient.fullIngredient.measure
                )
            }
            is IngredientsTabEvent.EditAmountTextChanged -> {
                _state.value = state.value.copy(
                    editAmountText = event.amountText
                )
            }
            is IngredientsTabEvent.EditMeasureTextChanged -> {
                _state.value = state.value.copy(
                    editMeasureText = event.measureText
                )

                var dropdownOptions: List<MatchTo> = emptyList()

                if(event.measureText != "") {

                    dropdownOptions = state.value.measures.filter {
                        (it.name.lowercase().startsWith(event.measureText, true) || it.name.contains(event.measureText, true)) && it.name.lowercase() != event.measureText.lowercase()
                    }.take(8).map { MatchTo(it.measureId?.toLong(), "Measure", it.name) }
                }

                _state.value = state.value.copy(
                    showMeasureDropdown = dropdownOptions.isNotEmpty(),
                    measureDropdownList = dropdownOptions
                )
            }
            is IngredientsTabEvent.EditIngredientTextChanged -> {
                _state.value = state.value.copy(
                    editIngredientText = event.ingredientText
                )

                val dropdownOptions: List<MatchTo> = if(event.ingredientText == "") {
                    emptyList()
                } else {
                    state.value.allIngredients.filter {
                        (it.name.lowercase().startsWith(event.ingredientText, true) || it.name.contains(event.ingredientText, true)) && it.name.lowercase() != event.ingredientText.lowercase()
                    }.take(8).map { MatchTo(it.ingredientId?.toLong(), "Ingredient", it.name) }
                }

                _state.value = state.value.copy(
                    showIngredientDropdown = dropdownOptions.isNotEmpty(),
                    ingredientDropdownList = dropdownOptions
                )
            }
            is IngredientsTabEvent.SaveEditIngredient -> {
                // save the edit amount
                _state.value = state.value.copy(
                    editedIngredient = _state.value.editedIngredient.copy(
                        amount = state.value.editAmountText
                    )
                )

                // save the edit measure
                if(state.value.measures.filter { it.name.startsWith(state.value.editMeasureText, true) }.isNotEmpty()) {
                    val editMeasure = state.value.measures.first {
                        it.name.startsWith(
                            state.value.editMeasureText,
                            true
                        )
                    }
                    if(state.value.editedIngredient.measureId != editMeasure.measureId) {
                        // save if different from measure for consistency
                        _state.value = state.value.copy(
                            editedIngredient = _state.value.editedIngredient.copy(
                                measureId = editMeasure.measureId!!,
                                measure = editMeasure.name
                            )
                        )
                    }
                } else if(state.value.editMeasureText != "") {
                    viewModelScope.launch {
                        // if new measure insert the new measure
                        addNewMeasure(state.value.editMeasureText.lowercase().split(" ").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }, "Edit")
                    }
                }

                // save the edit ingredient
                if(state.value.allIngredients.filter { it.name.startsWith(state.value.editIngredientText, true) }.isNotEmpty()) {
                    val editIngredient = state.value.allIngredients.first {
                        it.name.startsWith(
                            state.value.editIngredientText,
                            true
                        )
                    }
                    if(state.value.editedIngredient.ingredientId != editIngredient.ingredientId) {
                        // save from Ingredient for consistency
                        _state.value = state.value.copy(
                            editedIngredient = _state.value.editedIngredient.copy(
                                ingredientId = editIngredient.ingredientId!!,
                                ingredient = editIngredient.name
                            )
                        )
                    }
                } else {
                    viewModelScope.launch {
                        // if new ingredient insert the new ingredient
                        addNewIngredient(state.value.editIngredientText.lowercase().split(" ").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }, "Edit")
                    }
                }

                // save the edited recipe ingredient
                if(state.value.editedIngredient.ingredientId > 0) {
                    viewModelScope.launch {
                        // save the new recipe
                        recipeIngredientUseCases.addRecipeIngredient(state.value.editedIngredient.toRecipeIngredient())
                    }
                }

                // close the dialog and clear the state
                _state.value = _state.value.copy(
                    showEditIngredientDialog =  false,
                    editAmountText = "",
                    editMeasureText = "",
                    editIngredientText = ""
                )
            }
            is IngredientsTabEvent.CancelEditIngredient -> {
                _state.value = state.value.copy(
                    showEditIngredientDialog = false,
                    editAmountText = "",
                    editMeasureText = "",
                    editIngredientText = ""
                )
            }
            is IngredientsTabEvent.ToggleNewIngredientDialog -> {
                _state.value = state.value.copy(
                    showNewIngredientDialog = !state.value.showNewIngredientDialog,
                    newAmountText = "",
                    newMeasureText = "",
                    newIngredientText = ""
                )
            }
            is IngredientsTabEvent.NewAmountTextChanged -> {
                _state.value = state.value.copy(
                    newAmountText = event.amountText
                )
            }
            is IngredientsTabEvent.NewMeasureTextChanged -> {
                _state.value = state.value.copy(
                    newMeasureText = event.measureText
                )

                var dropdownOptions: List<MatchTo> = emptyList()

                if(event.measureText != "") {

                    dropdownOptions = state.value.measures.filter {
                        (it.name.lowercase().startsWith(event.measureText, true) || it.name.contains(event.measureText, true)) && it.name.lowercase() != event.measureText.lowercase()
                    }.take(8).map { MatchTo(it.measureId?.toLong(), "Measure", it.name) }
                }

                _state.value = state.value.copy(
                    showMeasureDropdown = dropdownOptions.isNotEmpty(),
                    measureDropdownList = dropdownOptions
                )
            }
            is IngredientsTabEvent.NewIngredientTextChanged -> {
                _state.value = state.value.copy(
                    newIngredientText = event.ingredientText
                )

                val dropdownOptions: List<MatchTo> = if(event.ingredientText == "") {
                    emptyList()
                } else {
                    state.value.allIngredients.filter {
                        (it.name.lowercase().startsWith(event.ingredientText, true) || it.name.contains(event.ingredientText, true)) && it.name.lowercase() != event.ingredientText.lowercase()
                    }.take(8).map { MatchTo(it.ingredientId?.toLong(), "Ingredient", it.name) }
                }

                _state.value = state.value.copy(
                    showIngredientDropdown = dropdownOptions.isNotEmpty(),
                    ingredientDropdownList = dropdownOptions
                )
            }
            is IngredientsTabEvent.SaveNewIngredient -> {

                // save the new amount
                _newFullRecipeIngredient.value = newFullRecipeIngredient.value.copy(
                    amount = state.value.newAmountText
                )

                // save the new measure
                if(state.value.newMeasureText != "") {
                    if (state.value.measures.filter {
                            it.name.startsWith(
                                state.value.newMeasureText,
                                true
                            )
                        }.isNotEmpty()) {
                        val newMeasure = state.value.measures.first {
                            it.name.startsWith(
                                state.value.newMeasureText,
                                true
                            )
                        }
                        // save from measure for consistency
                        _newFullRecipeIngredient.value = newFullRecipeIngredient.value.copy(
                            measureId = newMeasure.measureId!!,
                            measure = newMeasure.name
                        )
                    } else {
                        viewModelScope.launch {
                            // if new measure insert the new measure
                            addNewMeasure(
                                state.value.newMeasureText.lowercase().split(" ")
                                    .joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) },
                                "New"
                            )
                        }

                    }
                }
                // save the new ingredient
                if(state.value.allIngredients.filter { it.name.startsWith(state.value.newIngredientText, true) }.isNotEmpty()) {
                    val newIngredient = state.value.allIngredients.first {
                        it.name.startsWith(
                            state.value.newIngredientText,
                            true
                        )
                    }
                    // save from Ingredient for consistency
                    _newFullRecipeIngredient.value = newFullRecipeIngredient.value.copy(
                        ingredientId = newIngredient.ingredientId!!,
                        ingredient = newIngredient.name
                    )
                } else {
                    viewModelScope.launch {
                        // if new ingredient insert the new ingredient
                        addNewIngredient(state.value.newIngredientText.lowercase().split(" ").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }, "New")
                    }
                }

                // save the new recipe ingredient
                if(newFullRecipeIngredient.value.ingredientId > 0) {
                    viewModelScope.launch {
                        // save the new recipe
                        recipeIngredientUseCases.addRecipeIngredient(newFullRecipeIngredient.value.toRecipeIngredient())
                    }
                }

                // close the dialog and clear state
                _state.value = state.value.copy(
                    showNewIngredientDialog = !state.value.showNewIngredientDialog,
                    newAmountText = "",
                    newMeasureText = "",
                    newIngredientText = ""
                )
            }
            is IngredientsTabEvent.SetMeasureTextFromDropdown -> {
                if(state.value.showEditIngredientDialog) {
                    _state.value = state.value.copy(
                        editMeasureText = event.measureText,
                        showMeasureDropdown = false
                    )
                }
                if(state.value.showNewIngredientDialog) {
                    _state.value = state.value.copy(
                        newMeasureText = event.measureText,
                        showMeasureDropdown = false
                    )
                }
            }
            is IngredientsTabEvent.SetIngredientTextFromDropdown -> {
                if(state.value.showEditIngredientDialog) {
                    _state.value = state.value.copy(
                        editIngredientText = event.ingredientText,
                        showIngredientDropdown = false
                    )
                }
                if(state.value.showNewIngredientDialog) {
                    _state.value = state.value.copy(
                        newIngredientText = event.ingredientText,
                        showIngredientDropdown = false
                    )
                }
            }
            is IngredientsTabEvent.DismissAllDropdowns -> {
                _state.value = state.value.copy(
                    showMeasureDropdown = false,
                    showIngredientDropdown = false
                )
            }
        }
    }

    private fun getRecipeIngredients(recipeId: Long) {
        getRecipeIngredientsJob?.cancel()
        getRecipeIngredientsJob = recipeIngredientUseCases.getRecipeIngredientsByRecipeId(recipeId)
            .onEach { ingredients ->
                _state.value = state.value.copy(
                    recipeIngredients = ingredients
                )
                var ifList = listOf<IngredientFocus>()
                ingredients.forEach { ingredient ->
                    ifList += IngredientFocus(
                        fullIngredient = ingredient!!,
                        focused = false
                    )
                }
                _state.value = _state.value.copy(
                    ingredientFocus = ifList
                )

                _newFullRecipeIngredient.value = newFullRecipeIngredient.value.copy(
                    recipeId = recipeId,
                )

                _newFullIngredient.value = newFullIngredient.value.copy(
                    fullIngredient = newFullRecipeIngredient.value,
                    amountAndMeasure = "",
                    ingredient = "",
                    focused = false
                )

            }
            .launchIn(viewModelScope)
    }

    private fun getMeasures() {
        getMeasuresJob?.cancel()
        getMeasuresJob = measureUseCases.getMeasures()
            .onEach { measures ->
                _state.value = state.value.copy(
                    measures = measures
                )
            }
            .launchIn(viewModelScope)
    }
/*
    private fun findMeasureInMeasures(newMeasure:String): Measure? {
        // search measure name for match
        var x = state.value.measures.filter { it.name.lowercase().startsWith(newMeasure.trimEnd('s').lowercase()) }
        if(x.isEmpty()) {
            // search short name for match
            x = state.value.measures.filter { it.name.lowercase().startsWith(newMeasure.trimEnd('s').lowercase()) }
        }
        if(x.isNotEmpty()) {
            // return the first found
            return x[0]
        }
        return null
    }*/

    private fun getIngredients() {
        getIngredientsJob?.cancel()
        getIngredientsJob = ingredientUseCases.getIngredients()
            .onEach { ingredients ->
                _state.value = state.value.copy(
                    allIngredients = ingredients
                )
            }
            .launchIn(viewModelScope)
    }
/*

    private fun findIngredientInIngredients(newIngredient: String): Ingredient? {
        // search ingredient name for match
        val x = state.value.allIngredients.filter { it.name.lowercase().startsWith(newIngredient.lowercase()) }
        if(x.isNotEmpty()) {
            // return the first found
            return x[0]
        }
        return null
    }
*/

    private fun addNewMeasure(measureText: String, type: String) {
        var id = -1L
        CoroutineScope(Dispatchers.IO).launch {
            val newMeasure = Measure(
                measureId = null,
                name = measureText
            )
            id = measureUseCases.insertMeasureReturnId(newMeasure)
            if (id > 0 && type == "New") {
                _newFullRecipeIngredient.value = newFullRecipeIngredient.value.copy(
                    measureId = id,
                    measure =  measureText
                )
                // ok to check for measureId here as a new measure was just added
                if (newFullRecipeIngredient.value.measureId > 0 && newFullRecipeIngredient.value.ingredientId > 0) {
                    // save the new recipe ingredient
                    recipeIngredientUseCases.addRecipeIngredient(newFullRecipeIngredient.value.toRecipeIngredient())
                }
            }
            if(id > 0 && type == "Edit") {
                _state.value = state.value.copy(
                    editedIngredient = _state.value.editedIngredient.copy(
                        measureId = id,
                        measure = newMeasure.name
                    )
                )
                // ok to check for measureId here as a new measure was just added
                if (state.value.editedIngredient.measureId > 0 && state.value.editedIngredient.ingredientId > 0) {
                    // save the edited recipe ingredient
                    recipeIngredientUseCases.addRecipeIngredient(state.value.editedIngredient.toRecipeIngredient())
                }
            }
        }
    }

    private fun addNewIngredient(ingredientText: String, type: String) {
        var id = -1L
        CoroutineScope(Dispatchers.IO).launch {
            val newIngredient = Ingredient(
                ingredientId = null,
                name = ingredientText,
                type = "Added Ingredient"
            )

            id = ingredientUseCases.insertIngredientReturnId(newIngredient)
            if(id > 0 && type == "New") {
                _newFullRecipeIngredient.value = newFullRecipeIngredient.value.copy(
                    ingredientId = id,
                    ingredient =  ingredientText
                )
                if (newFullRecipeIngredient.value.ingredientId > 0) {
                    // save the new recipe ingredient
                    recipeIngredientUseCases.addRecipeIngredient(newFullRecipeIngredient.value.toRecipeIngredient())
                }
            }
            if(id > 0 && type == "Edit") {
                _state.value = state.value.copy(
                    editedIngredient = _state.value.editedIngredient.copy(
                        ingredientId = id,
                        ingredient = newIngredient.name
                    )
                )
                if (state.value.editedIngredient.ingredientId > 0) {
                    // save the edited recipe ingredient
                    recipeIngredientUseCases.addRecipeIngredient(state.value.editedIngredient.toRecipeIngredient())
                }
            }
        }
    }
}
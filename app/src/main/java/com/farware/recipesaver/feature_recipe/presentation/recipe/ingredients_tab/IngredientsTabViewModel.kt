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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
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

    private var _measure = mutableStateOf(Measure(null, "", ""))
    val measure: State<Measure> = _measure

    private var _ingredient = mutableStateOf(Ingredient(-1, "", ""))
    val ingredient: State<Ingredient> = _ingredient

    private var _recipeIngredient = mutableStateOf(RecipeIngredient(null, -1, -1, -1, "", "", ""))
    val recipeIngredient: State<RecipeIngredient> = _recipeIngredient

    private var _recipeIngredientWithIngredient = mutableStateOf(RecipeIngredientWithIngredient(null, -1, -1, -1, "", "", "", "", ""))
    val recipeIngredientWithIngredient: State<RecipeIngredientWithIngredient> = _recipeIngredientWithIngredient

    private var _newIngredient = mutableStateOf(FullRecipeIngredient.new())
    val newIngredient: State<FullRecipeIngredient> = _newIngredient

    private var _newFullIngredient = mutableStateOf(IngredientFocus(newIngredient.value, "", "", false))
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
                var sf = state.value.ingredientFocus
                sf = sf.map { item ->
                    if(item.fullIngredient.ingredientId == event.ingredientFocus.fullIngredient.ingredientId)
                        item.copy(focused = true)
                    else
                        item.copy(focused = false)
                }
                _state.value = state.value.copy(
                    ingredientFocus = sf
                )
            }
            is IngredientsTabEvent.SaveIngredient -> {
                var saveNeeded = false
                var newFull = event.ingredient.fullIngredient

                // if something in amountAndMeasure it has changed
                // split and save both parts
                if (event.ingredient.amountAndMeasure != "") {
                    var (amount, measure) = splitAmountAndMeasure(event.ingredient.amountAndMeasure.toString())
                    // Capitalize each word in measure
                    measure = measure.split(" ")
                        .joinToString(" ") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } }
                    if(amount.lowercase() != event.ingredient.fullIngredient.amount.lowercase()) {
                        newFull.amount = amount
                        saveNeeded = true
                    }
                    if(measure.lowercase() != event.ingredient.fullIngredient.measure.lowercase()) {
                        // TODO: check for measure in data and get id
                        var found = findMeasureInMeasures(measure)
                        if(found != null) {
                            newFull.measureId = found.measureId!!
                            newFull.measure = found.name
                            saveNeeded = true
                        } else {
                            //  or insert new measure and get the id
                            val id = addNewMeasure(measure)
                            //  add the measureId to newFull
                            if(id > 0) {
                                newFull.measureId = id
                                newFull.measure = measure
                                saveNeeded = true
                                // TODO: display new measure added on snackbar
                                _state.value = state.value.copy(
                                    message = "A new Measure - $measure has been added.",
                                    showSnackbar = true
                                )
                            }
                        }
                    }
                }
                if(event.ingredient.fullIngredient.ingredient != event.ingredient.ingredient) {
                    var ingredient = event.ingredient.ingredient!!.split(" ")
                        .joinToString(" ") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } }
                    // TODO: check for ingredient in data and get id
                    var found = findIngredientInIngredients(ingredient)
                    if(found != null) {
                        newFull.ingredientId = found.ingredientId!!
                        newFull.ingredient = found.name
                        saveNeeded = true
                        // TODO: display modified ingredient on snackbar
                        _state.value = state.value.copy(
                            message = "$ingredient has been changed to ${found.name}.",
                            showSnackbar = true
                        )
                    } else {
                        //  or insert new ingredient and get the id
                        val id = addNewIngredient(ingredient)
                        //  add the ingredientId to newFull
                        if(id > 0) {
                            newFull.ingredientId = id
                            newFull.ingredient = ingredient
                            saveNeeded = true
                            // TODO: display new measure added on snackbar
                            _state.value = state.value.copy(
                                message = "A new Ingredient - $ingredient has been added.",
                                showSnackbar = true
                            )
                        }
                    }
                }

                //  if amount, measure or ingredient has changed then save the recipe
                if(saveNeeded) {
                    viewModelScope.launch {
                        recipeIngredientUseCases.addRecipeIngredient(newFull.toRecipeIngredient())
                    }
                }
            }
            is IngredientsTabEvent.DeleteIngredient -> {
                viewModelScope.launch {
                    recipeIngredientUseCases.deleteRecipeIngredient(event.ingredient.toRecipeIngredient())
                }
            }
        }
    }

    private fun splitAmountAndMeasure(amountAndMeasure: String) : Pair<String, String> {
        var amount: String = ""
        var measure: String = ""

        // using simple for-loop
        for (i in amountAndMeasure.length - 1 downTo 0) {
            if(amountAndMeasure[i] == ' ') {
                amount = amountAndMeasure.substring(0, i)
                measure = amountAndMeasure.substring(i+1)
                break
            }
        }

        return Pair(amount, measure)
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

                _newIngredient.value = newIngredient.value.copy(
                    recipeId = recipeId
                )

                _newFullIngredient.value = newFullIngredient.value.copy(
                    fullIngredient = newIngredient.value,
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

    private fun findMeasureInMeasures(newMeasure:String): Measure? {
        // search measure name for match
        var x = state.value.measures.filter { it.name.lowercase().startsWith(newMeasure.trimEnd('s').lowercase()) }
        if(x.isEmpty()) {
            // search short name for match
            x = state.value.measures.filter { it.shortName.lowercase().startsWith(newMeasure.trimEnd('s').lowercase()) }
        }
        if(x.isNotEmpty()) {
            // return the first found
            return x[0]
        }
        return null
    }

    private fun addNewMeasure(measure: String): Int {
        val newMeasure = Measure(
            measureId = null,
            name = measure,
            shortName = measure
        )
        var id = -1
        viewModelScope.launch {
            id = measureUseCases.insertMeasureReturnId(newMeasure).toInt()
        }
        return id
    }

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

    private fun findIngredientInIngredients(newIngredient: String): Ingredient? {
        // search ingredient name for match
        var x = state.value.allIngredients.filter { it.name.lowercase().startsWith(newIngredient.lowercase()) }
        if(x.isNotEmpty()) {
            // return the first found
            return x[0]
        }
        return null
    }

    private fun addNewIngredient(ingredient: String): Long {
        val newIngredient = Ingredient(
            ingredientId = null,
            name = ingredient,
            type = "Added Ingredient"
        )
        var id = -1L
        viewModelScope.launch {
            id = ingredientUseCases.insertIngredientReturnId(newIngredient)
        }
        return id
    }
}
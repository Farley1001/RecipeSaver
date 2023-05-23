package com.farware.recipesaver.feature_recipe.presentation.share_recipe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.domain.bluetooth.BluetoothConnectionResult
import com.farware.recipesaver.feature_recipe.domain.bluetooth.BluetoothController
import com.farware.recipesaver.feature_recipe.domain.bluetooth.BluetoothDeviceDomain
import com.farware.recipesaver.feature_recipe.domain.bluetooth.BluetoothMessage
import com.farware.recipesaver.feature_recipe.domain.model.recipe.*
import com.farware.recipesaver.feature_recipe.domain.use_cases.*
import com.farware.recipesaver.feature_recipe.presentation.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareRecipeViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val recipeUseCases: RecipeUseCases,
    private val recipeIngredientUseCases: RecipeIngredientUseCases,
    private val stepUseCases: StepUseCases,
    private val tipUseCases: TipUseCases,
    private val ingredientUseCases: IngredientUseCases,
    private val measureUseCases: MeasureUseCases,
    private val bluetoothController: BluetoothController,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(ShareRecipeUiState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            allDevices = pairedDevices + scannedDevices,
            messages = if (state.isConnected) state.messages else emptyList()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    private var deviceConnectionJob: Job? = null
    private var getRecipesJob: Job? = null
    private var getCategoriesJob: Job? = null
    private var getStepsJob: Job? = null
    private var getTipsJob: Job? = null
    private var getIngredientsJob: Job? = null
    private var getAllMeasuresJob: Job? = null
    private var getAllIngredientsJob: Job? = null

    init {
        savedStateHandle.get<Long>("recipeId")?.let { recipeId ->
            if(recipeId != -1L) {
                _state.update { it.copy(recipeId = recipeId) }
                viewModelScope.launch {
                    recipeUseCases.getRecipe(recipeId)?.also { recipe ->
                        _state.value = state.value.copy(
                            recipe = recipe
                        )
                    }

                    getRecipes(recipeId)
                    getSteps(recipeId)
                    getTips(recipeId)
                    getIngredients(recipeId)
                    getCategories()
                    getAllMeasures()
                    getAllIngredients()
                }
            }
        }
        bluetoothController.isConnected.onEach { isConnected ->
            _state.update {
                it.copy(
                    isConnected = isConnected,
                    deviceName = bluetoothController.getBluetoothName() ?: ""
                )
            }
        }.launchIn(viewModelScope)

        bluetoothController.errors.onEach { error ->
            _state.update {
                it.copy(
                    errorMessage = error
                )
            }
        }.launchIn(viewModelScope)
    }


    fun onEvent(event: ShareRecipeEvent) {
        when(event) {
            is ShareRecipeEvent.Navigate -> {
                appNavigator.tryNavigateTo(event.path)
            }
        }
    }

    fun connectToDevice(device: BluetoothDeviceDomain) {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .connectToDevice(device)
            .listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update {
            it.copy(
                isConnecting = false,
                isConnected = false
            )
        }
    }

    fun waitForIncomingConnections() {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .startBluetoothServer()
            .listen()
    }

    fun sendMessage(message: String) {
        val recipe = buildRecipeToSend()
        viewModelScope.launch {
            val bluetoothMessage = bluetoothController.trySendMessage("${ recipe.length.toString()}^${ recipe }")
            if (bluetoothMessage != null) {
                _state.update {
                    it.copy(
                        messages = it.messages + bluetoothMessage
                    )
                }
            }
        }
    }

    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }

    private fun Flow<BluetoothConnectionResult>.listen(): Job {
        return onEach { result ->
            when (result) {
                BluetoothConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            isConnected = true,
                            isConnecting = false,
                            errorMessage = null
                        )
                    }
                }
                is BluetoothConnectionResult.TransferSucceeded -> {
                    if(state.value.messageSize == 0) {
                        // get the message size
                        _state.update {
                            it.copy(
                                messageSize = result.message.message.substring(0, result.message.message.indexOf("^")).toInt(),
                                partialMessage = result.message.message.substring(result.message.message.indexOf("^") + 1)
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                partialMessage = state.value.partialMessage + result.message.message
                            )
                        }
                    }

                    if(state.value.partialMessage.length >= state.value.messageSize) {
                        // have entire recipe
                        _state.update {
                            it.copy(
                                messages = it.messages + BluetoothMessage(
                                    message = state.value.partialMessage,
                                    senderName = result.message.senderName ,
                                    isFromLocalUser = result.message.isFromLocalUser
                                ),
                                messageSize = 0,
                                partialMessage = ""
                            )
                        }
                        if (!result.message.isFromLocalUser) {
                            parseNewRecipeString(state.value.partialMessage)
                        }
                    }
                }
                is BluetoothConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }.catch { throwable ->
            bluetoothController.closeConnection()
            _state.update {
                it.copy(
                    isConnected = false,
                    isConnecting = false
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }

    private fun getRecipes(recipeId: Long) {
        getRecipesJob?.cancel()
        getRecipesJob = recipeUseCases.getRecipes()
            .onEach { recipe ->
                _state.update { it.copy(recipes = recipe) }
            }
            .launchIn(viewModelScope)
    }

    private fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = recipeUseCases.getCategories()
            .onEach { category ->
                _state.update { it.copy(categories = category) }
            }
            .launchIn(viewModelScope)
    }

    private fun getSteps(recipeId: Long) {
        getStepsJob?.cancel()
        getStepsJob = stepUseCases.getStepsByRecipeId(recipeId)
            .onEach { step ->
                _state.update { it.copy(steps = step) }
            }
            .launchIn(viewModelScope)

    }

    private fun getTips(recipeId: Long) {
        getTipsJob?.cancel()
        getTipsJob = tipUseCases.getTipsByRecipeId(recipeId)
            .onEach { tip ->
                _state.update { it.copy(tips = tip) }
            }
            .launchIn(viewModelScope)
    }

    private fun getAllMeasures() {
        getAllMeasuresJob?.cancel()
        getAllMeasuresJob = measureUseCases.getMeasures()
            .onEach { measure ->
                _state.update { it.copy(allMeasures = measure) }
            }
            .launchIn(viewModelScope)
    }


    private fun getIngredients(recipeId: Long) {
        getIngredientsJob?.cancel()
        getIngredientsJob = recipeIngredientUseCases.getRecipeIngredientsByRecipeId(recipeId)
            .onEach { ingredient ->
                _state.update { it.copy(ingredients = ingredient) }
            }
            .launchIn(viewModelScope)
    }

    private fun getAllIngredients() {
        getAllIngredientsJob?.cancel()
        getAllIngredientsJob = ingredientUseCases.getIngredients()
            .onEach { ingredient ->
                _state.update { it.copy(allIngredients = ingredient) }
            }
            .launchIn(viewModelScope)
    }

    // *********************************************************************************************************************************************
    //        need fo fix this section to use theis apps db
    // *********************************************************************************************************************************************

    private fun buildRecipeToSend(): String {
        val rx = state.value.recipe
        val r = "${rx.name}; ${rx.description}; ${rx.prepTime}; ${rx.cookTime}; ${rx.favorite}; ${rx.category}; ${rx.lightThemeColor}; ${rx.onLightThemeColor}; ${rx.darkThemeColor}; ${rx.onDarkThemeColor}"
        var s = ""
        state.value.steps.forEach {
            s += it!!.text + ";"
        }
        var t = ""
        state.value.tips.forEach {
            t += it!!.text + ";"
        }
        var i = ""
        state.value.ingredients.forEach {
            i += it!!.amount + ";"
            i += it!!.measure + "; "
            i += it!!.ingredient + ";"
        }
        _state.value = state.value.copy(
            sendString = "$r:$s:$t:$i:"
        )

        return "$r:$s:$t:$i:"
    }

    private suspend fun parseNewRecipeString(recipeString: String) {
        var r = emptyList<String>()
        var s = ""
        var t = ""
        var i = ""
        val all = recipeString.split(":")
        if (all.size >= 4) {
            r = all[0].split(";")
            s = all[1]
            t = all[2]
            i = all[3]
        } else {
            // TODO: Error
        }

        if(r.size == 10) {
            createRecipe(r, s, t, i)
        }
    }

    private suspend fun createRecipe(recipeList: List<String>, stepString: String, tipString: String, ingredientString: String) {
        var catId = 0L                         //createCategory(recipeList[5])
        var recId = 0L
        // check for existing category name
        catId = state.value.categories.first { it.name == recipeList[5].trim() }.categoryId!!
        // create new category
        if(catId == 0L) {
            viewModelScope.launch {
                catId = recipeUseCases.insertCategoryReturnId(
                    Category(
                        categoryId = null,
                        name = recipeList[5],
                        lightThemeColor = recipeList[6].toInt(),
                        onLightThemeColor = recipeList[7].toInt(),
                        darkThemeColor = recipeList[8].toInt(),
                        onDarkThemeColor = recipeList[9].toInt(),
                        timeStamp = System.currentTimeMillis()
                    )
                )
            }
        }

        // create new recipe and return recipeId
        if(catId > 0) {
            viewModelScope.launch {
                recId = recipeUseCases.insertRecipeReturnId(
                    Recipe(
                        recipeId = null,
                        categoryId = catId,
                        name = recipeList[0].trim(),
                        description = recipeList[1].trim(),
                        prepTime = recipeList[2].trim().toLong(),
                        cookTime = recipeList[3].trim().toLong(),
                        favorite = recipeList[4].trim().toBoolean(),
                        timeStamp = System.currentTimeMillis()
                    )
                )

                if(recId > 0) {
                    var resultA = async {createSteps(recId, stepString.split(";") ) }
                    var resultB = async { createTips(recId, tipString.split(";")) }
                    var resultC = async { createIngredients(recId, ingredientString.split(";")) }

                    val a = resultA.await()
                    val b = resultB.await()
                    val c = resultC.await()
                }
            }
        }
    }

    private suspend fun createSteps(recipeId: Long, stepList: List<String>): Int {
        // list of 1 field step text
        var stepNum = 0
        stepList.forEach {
            if(it.trim().isNotEmpty()) {
                stepNum++
                val newStep = Step(
                    stepId = null,
                    recipeId = recipeId,
                    stepNumber = stepNum,
                    text = it.trim()
                )
                viewModelScope.launch {
                    stepUseCases.addStep(newStep)
                }
            }
        }
        return 100
    }

    private suspend fun createTips(recipeId: Long, tipList: List<String>): Int {
        // list of 1 field tip text
        var tipNum = 0
        tipList.forEach {
            tipNum++
            if(it.trim().isNotEmpty()) {
                val newTip = Tip(
                    tipId = null,
                    recipeId = recipeId,
                    tipNumber = tipNum,
                    text = it.trim()
                )
                viewModelScope.launch {
                    tipUseCases.addTip(newTip)
                }
            }
        }
        return 100
    }

    private suspend fun createIngredients(recipeId: Long, ingredientList: List<String>): Int {
        // list of 3 fields amount, measureShortName, ingredient
/*        var ax = emptyList<String>()
        var mx = emptyList<String>()
        var ix = emptyList<String>()
        ax = ingredientList.filterIndexed { index, data ->
            index % 3 == 0
        }
        mx = ingredientList.filterIndexed { index, data ->
            index > 0 && index-1 % 3 == 0
        }
        ix = ingredientList.filterIndexed { index, data ->
            index > 1 && index-2 % 3 == 0
        }*/

        var size = ingredientList.size/3*3
        for(i in 0 until size step 3) {
            var ingId = -1L
            var mesId = -1L

            // measureId
            if (ingredientList[i + 1].trim().isNotEmpty())
                mesId = state.value.allMeasures.first { it?.name?.lowercase() == ingredientList[i + 1].trim().lowercase() }?.measureId!!

            if (mesId == -1L) {
                viewModelScope.launch {
                    mesId = measureUseCases.insertMeasureReturnId(
                        Measure(
                            measureId = null,
                            name = ingredientList[i + 1].trim()
                        )
                    )
                }
            }

            // ingredientId
            if (ingredientList[i + 2].trim().isNotEmpty()) {
                ingId = state.value.allIngredients.first { it?.name?.lowercase() == ingredientList[i + 2].trim().lowercase() }?.ingredientId!!

                if(ingId == -1L) {
                    viewModelScope.launch {
                        ingId = ingredientUseCases.insertIngredientReturnId(
                            Ingredient(
                                ingredientId = -1,
                                name = ingredientList[i + 2].trim(),
                                type = "Import"
                            )
                        )
                    }
                }
            }

            // recipe ingredient
            viewModelScope.launch {
                recipeIngredientUseCases.addRecipeIngredient(
                    RecipeIngredient(
                        recipeIngredientId = null,
                        recipeId = recipeId,
                        ingredientId = ingId,
                        measureId = mesId,
                        amount = ingredientList[i].trim()
                    )
                )
            }

            // ingredients have 3 elements amount, measure and ingredient
            //i += 3
        }
        return 100
    }
}
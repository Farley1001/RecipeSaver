package com.farware.recipesaver.feature_recipe.presentation.recipe

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Right
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.presentation.appbar.ActionItem
import com.farware.recipesaver.feature_recipe.presentation.appbar.OverflowMode
import com.farware.recipesaver.feature_recipe.presentation.components.DropdownWithLabel
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.ChangeCategoryDialog
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TabItem
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TextWithAppendedContent
import com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab.IngredientsTabScreen
import com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab.StepsTabScreen
import com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab.TipsTabScreen
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.mainTitle
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.TabOrder
import com.farware.recipesaver.feature_recipe.presentation.util.UiEvent
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RecipeScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    //val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // following is used to show snackbars
    val showSnackbarMessage = remember { mutableStateOf(false) }

    // display any errors in a toast
    if(viewModel.state.value.error != "") {
        Toast.makeText(LocalContext.current, "error: ${viewModel.state.value.error}", Toast.LENGTH_LONG).show()
    }

    val actionItems = listOf(
        ActionItem("Change Recipe Name", Icons.Default.EditNote, OverflowMode.NEVER_OVERFLOW) {
            viewModel.onEvent(
                RecipeEvent.ToggleAddEditDialog
            )
        }
    )

    val showAddEditDialog = viewModel.state.value.showAddEditRecipeDialog
    val recipe = viewModel.recipe.value

    val tabList = listOf(TabItem.Steps, TabItem.Ingredients, TabItem.Tips)

    //TODO: change colors
    var categoryColor =   MaterialTheme.colorScheme. primaryContainer        // MaterialTheme.extraColors.recipeItem
    var onCategoryColor = MaterialTheme.colorScheme.onPrimaryContainer       // MaterialTheme.extraColors.onRecipeItem
    var favoriteColor = MaterialTheme.colorScheme.tertiary                   // secondaryVariant

    // prep-time time picker
    val newPrepTime = remember { mutableStateOf(0) }
    val prepTimeChanged = remember { mutableStateOf(false) }
    var prepTimePickerDialog = TimePickerDialog(context, { _, h: Int, m: Int -> newPrepTime.value = (h*60) + m }, 0,0, false)

    // cook-time time picker
    val newCookTime = remember { mutableStateOf(0) }
    val cookTimeChanged = remember { mutableStateOf(false) }
    var cookTimePickerDialog = TimePickerDialog(context, { _, h: Int, m: Int -> newPrepTime.value = (h*60) + m }, 0,0, false)


    if (recipe != null) {
        categoryColor = Color(viewModel.recipe.value!!.background(isSystemInDarkTheme()))
        onCategoryColor = Color(viewModel.recipe.value!!.onBackground(isSystemInDarkTheme()))
        // TODO Color
        if(viewModel.recipe.value!!.favorite!!) { favoriteColor = Color.Red }

        // prep-time time picker
        prepTimePickerDialog = TimePickerDialog(
            context,
            android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK,
            { _, h: Int, m: Int ->
                if(h != viewModel.recipe.value!!.prepTime!!.toInt() / 60 || m != viewModel.recipe.value!!.prepTime!!.toInt() % 60) {
                    prepTimeChanged.value = true
                    newPrepTime.value = (h*60) + m
                    viewModel.onEvent(RecipeEvent.SavePrepTime(newPrepTime.value.toLong()))
                }
            },
            viewModel.recipe.value!!.prepTime!!.toInt() / 60,
            viewModel.recipe.value!!.prepTime!!.toInt() % 60,
            true
        )

        // cook-time time picker
        cookTimePickerDialog = TimePickerDialog(
            context,
            android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
            { _, h: Int, m: Int ->
                if(h != viewModel.recipe.value!!.cookTime!!.toInt() / 60 || m != viewModel.recipe.value!!.cookTime!!.toInt() % 60) {
                    cookTimeChanged.value = true
                    newCookTime.value = (h*60) + m
                    viewModel.onEvent(RecipeEvent.SaveCookTime(newCookTime.value.toLong()))
                }
            },
            viewModel.recipe.value!!.cookTime!!.toInt() / 60,
            viewModel.recipe.value!!.cookTime!!.toInt() % 60,
            true
        )

    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.SaveRecipe -> {
                    snackbarHostState.showSnackbar(
                        message = "Recipe Saved"
                    )
                }
            }
        }
    }

    RecipeContent(
        snackbarHostState = snackbarHostState,
        categories = viewModel.state.value.categories,
        selectedCategoryIndex = viewModel.state.value.selectedCategoryIndex,
        newCategorySelected = { viewModel.onEvent(RecipeEvent.NewCategorySelected(it)) },
        categoryColor = categoryColor,
        onCategoryColor = onCategoryColor,
        favoriteColor = favoriteColor,
        name = recipe!!.name,
        description = recipe!!.description,
        onRecipeNameTextChange = { viewModel.onEvent(RecipeEvent.RecipeNameTextChange(it)) },
        onRecipeDescriptionTextChange = { viewModel.onEvent(RecipeEvent.RecipeDescriptionTextChange(it)) },
        categoryName = recipe!!.category,
        prepTime = viewModel.recipe.value!!.prepTime!!,
        cookTime = viewModel.recipe.value!!.cookTime!!,
        showAddEditDialog = showAddEditDialog,
        onToggleAddEditDialog = { viewModel.onEvent(RecipeEvent.ToggleAddEditDialog) },
        selectedTabIndex = viewModel.state.value.selectedTabIndex,
        tabs = tabList,
        onSelectedTabChange = { viewModel.onEvent(RecipeEvent.SelectedTabChanged(it)) },               //onSelectedTabChange(it)
        onNavBackButtonClick = { viewModel.onEvent(RecipeEvent.NavigateBack) },                         //onNavBackButtonClick()
        isCategoryDialogOpen = viewModel.state.value.isCategoryDialogOpen,
        onToggleCategoryDialog = { viewModel.onEvent(RecipeEvent.ToggleCategoryDialog) },
        onSaveCategoryClick = { viewModel.onEvent(RecipeEvent.SaveNewCategory) },
        onCancelCategoryClick = { viewModel.onEvent(RecipeEvent.ToggleCategoryDialog) },
        prepTimePicker = prepTimePickerDialog,
        cookTimePicker = cookTimePickerDialog,
        onToggleFavorite = { viewModel.onEvent(RecipeEvent.ToggleFavorite) },
        onSaveRecipe = { viewModel.onEvent(RecipeEvent.SaveRecipe) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun RecipeContent(
    snackbarHostState: SnackbarHostState,
    categories: List<Category>,
    selectedCategoryIndex: Int,
    newCategorySelected: (Long) -> Unit,
    categoryColor: Color,
    onCategoryColor: Color,
    favoriteColor: Color,
    name: String,
    description: String,
    categoryName: String = "",
    onRecipeNameTextChange: (String) -> Unit,
    onRecipeDescriptionTextChange: (String) -> Unit,
    prepTime: Long,
    cookTime: Long,
    showAddEditDialog: Boolean,
    onToggleAddEditDialog: () -> Unit,
    selectedTabIndex: Int,
    tabs: List<TabItem>,
    onSelectedTabChange: (Int) -> Unit,
    onNavBackButtonClick: () -> Unit,
    isCategoryDialogOpen: Boolean,
    onToggleCategoryDialog: () -> Unit,
    onSaveCategoryClick: () -> Unit,
    onCancelCategoryClick: () -> Unit,
    prepTimePicker: TimePickerDialog,
    cookTimePicker: TimePickerDialog,
    onToggleFavorite: () -> Unit,
    onSaveRecipe: () -> Unit
) {
    if(showAddEditDialog){
        AlertDialog(
            modifier = Modifier
                .customDialogPosition(CustomDialogPosition.TOP)
                .padding(8.dp),
            onDismissRequest = { onToggleAddEditDialog() },
            title = {
                Text(text = "Add / Edit Recipe")
            },
            text = {
                Column() {//modifier = Modifier.fillMaxSize()
                    Row(
                        modifier = Modifier
                            .padding(start = 50.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (categories.isNotEmpty() && selectedCategoryIndex != -1) {
                            DropdownWithLabel(
                                labelText = "Category",
                                initialIndex = selectedCategoryIndex,
                                items = categories,
                                onDropdownItemSelected = { newCategorySelected(it) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    OutlinedTextField(
                        value = name,
                        label = { Text(text = "Recipe Name") },
                        onValueChange = {
                            onRecipeNameTextChange(it)
                        }

                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    OutlinedTextField(
                        value = description,
                        label = { Text(text = "Recipe Description") },
                        onValueChange = {
                            onRecipeDescriptionTextChange(it)
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSaveRecipe()
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onToggleAddEditDialog()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    if(isCategoryDialogOpen){
        ChangeCategoryDialog(
            categories = categories,
            selectedIndex = selectedCategoryIndex,
            onSaveCategoryClick = { onSaveCategoryClick() },
            onCancelCategoryClick = { onCancelCategoryClick() },
            newCategorySelected = { newCategorySelected(it) }
        )
    }
    if (name != null) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = name,
                        style = mainTitle,
                        //color = MaterialTheme.colors.onPrimary,
                        color = onCategoryColor,
                        maxLines = 2,
                        overflow = TextOverflow.Visible
                    )
                },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onNavBackButtonClick()
                            }
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                onToggleAddEditDialog()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.EditNote,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                )
            }
        ) { paddingValue ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    // TODO: Colors
                    //containerColor = MaterialTheme.colorScheme.inversePrimary,
                    //contentColor = MaterialTheme.colors.onBackground
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            text = { Text(tab.title) },
                            selected = selectedTabIndex == index,
                            onClick = {
                                onSelectedTabChange(index)
                            }
                        )
                    }
                }
                // show this section only when steps are shown
                if(selectedTabIndex == 0) {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .background(MaterialTheme.colorScheme.inverseOnSurface),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextWithAppendedContent(
                            text = categoryName,
                            onTextClicked = { onToggleCategoryDialog() },
                            placeholderWidth = 24.sp,
                            placeholderHeight = 24.sp,
                            placeholderVertAlign = PlaceholderVerticalAlign.TextTop,
                            appendContent = {
                                Badge(
                                    modifier = Modifier.padding(start = 5.dp, top = 1.dp),
                                    containerColor = categoryColor
                                ){
                                    Text(text = "")
                                }
                            }
                        )
                        Text(
                            text = if(prepTime > 0){"Prep: ${prepTime.toInt()}"}else{"Prep:"},
                            modifier = Modifier
                                .clickable { prepTimePicker.show() }
                                .padding(start = 4.dp),
                        )
                        Text(
                            text = if(cookTime > 0){"Cook: ${cookTime!!.toInt() / 60}:${cookTime!!.toInt() % 60}"}else{"Cook:"},
                            modifier = Modifier
                                .clickable { cookTimePicker.show() }
                                .padding(start = 4.dp),
                        )
                        IconButton(
                            onClick = {  }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .clickable { onToggleFavorite() }
                                    .padding(end = 12.dp),
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorites Icon",
                                tint = favoriteColor
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    //TODO: Colors
                        //.background(MaterialTheme.colors.background),
                ) {
                    AnimatedContent(
                        targetState = selectedTabIndex,
                        transitionSpec = {
                            slideIntoContainer(
                                animationSpec = tween(300, easing = EaseIn),
                                towards = Right
                            ).with(
                                slideOutOfContainer(
                                    animationSpec = tween(300, easing = EaseOut),
                                    towards = Right
                                )
                            )
                        }
                    ) {targetState ->
                        when(targetState){
                            TabOrder.STEP.ordinal -> { StepsTabScreen() }
                            TabOrder.INGREDIENT.ordinal -> { IngredientsTabScreen(snackbarHostState = snackbarHostState) }
                            TabOrder.TIP.ordinal -> { TipsTabScreen() }
                        }
                    }
                }
            }
        }
    }
}


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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.CategoryWithColor
import com.farware.recipesaver.feature_recipe.presentation.appbar.ActionItem
import com.farware.recipesaver.feature_recipe.presentation.appbar.OverflowMode
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.ChangeCategoryDialog
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TabItem
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TextWithAppendedContent
import com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab.IngredientsTabScreen
import com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab.StepsTabScreen
import com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab.TipsTabScreen
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.mainTitle
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import com.farware.recipesaver.feature_recipe.presentation.util.TabOrder

@Composable
fun RecipeScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
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
                RecipeEvent.ChangeRecipeName
            )
        }
    )

    val recipe = viewModel.recipe.value
    val name = viewModel.name.value
    val categoryName = viewModel.categoryName.value
    val prepTime = viewModel.prepTime.value
    val cookTime = viewModel.cookTime.value
    val favorite = viewModel.favorite.value

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
        if(favorite) { favoriteColor = Color.Red }

        // prep-time time picker
        prepTimePickerDialog = TimePickerDialog(
            context,
            android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK,
            { _, h: Int, m: Int ->
                if(h != prepTime.toInt() / 60 || m != prepTime.toInt() % 60) {
                    prepTimeChanged.value = true
                    newPrepTime.value = (h*60) + m
                    viewModel.onEvent(com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeEvent.SavePrepTime(newPrepTime.value.toLong()))
                }
            },
            prepTime.toInt() / 60,
            prepTime.toInt() % 60,
            true
        )

        // cook-time time picker
        cookTimePickerDialog = TimePickerDialog(
            context,
            android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
            { _, h: Int, m: Int ->
                if(h != cookTime.toInt() / 60 || m != cookTime.toInt() % 60) {
                    cookTimeChanged.value = true
                    newCookTime.value = (h*60) + m
                    viewModel.onEvent(com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeEvent.SaveCookTime(newCookTime.value.toLong()))
                }
            },
            cookTime.toInt() / 60,
            cookTime.toInt() % 60,
            true
        )

    }

    //
    fun onSelectedTabChange(index: Int) {
        viewModel.onEvent(com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeEvent.SelectedTabChanged(index))
    }

    fun onNavBackButtonClick() {
        navController.navigateUp()
        //Toast.makeText(context, "Add/Edit Recipe Click", Toast.LENGTH_LONG).show()
    }

    RecipeContent(
        recipeId = viewModel.state.value.newRecipeId,
        isNewRecipe = viewModel.state.value.isNewRecipe,
        snackbarHostState = snackbarHostState,
        categories = viewModel.state.value.categories,
        selectedCategoryIndex = viewModel.state.value.selectedCategoryIndex,
        newCategorySelected = { viewModel.onEvent(RecipeEvent.NewSelectedCategory(it)) },
        categoryColor = categoryColor,
        onCategoryColor = onCategoryColor,
        favoriteColor = favoriteColor,
        name = name,
        categoryName = categoryName,
        prepTime = prepTime,
        cookTime = cookTime,
        favorite = favorite,
        selectedTabIndex = viewModel.state.value.selectedTabIndex,
        tabs = tabList,
        onSelectedTabChange = { onSelectedTabChange(it) },
        onNavBackButtonClick = { onNavBackButtonClick() },
        isCategoryDialogOpen = viewModel.state.value.isCategoryDialogOpen,
        toggleCategoryDialog = { viewModel.onEvent(RecipeEvent.ToggleCategoryDialog) },
        onSaveCategoryClick = { viewModel.onEvent(RecipeEvent.SaveNewCategory) },
        onCancelCategoryClick = { viewModel.onEvent(RecipeEvent.ToggleCategoryDialog) },
        prepTimePicker = prepTimePickerDialog,
        onSavePrepTime = { viewModel.onEvent(RecipeEvent.SavePrepTime(it)) },
        cookTimePicker = cookTimePickerDialog,
        onSaveCookTime = { viewModel.onEvent(RecipeEvent.SaveCookTime(it)) },
        onToggleFavorite = { viewModel.onEvent(RecipeEvent.ToggleFavorite) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun RecipeContent(
    recipeId: Long,
    isNewRecipe: Boolean,
    snackbarHostState: SnackbarHostState,
    categories: List<CategoryWithColor>,
    selectedCategoryIndex: Int,
    newCategorySelected: (Long) -> Unit,
    categoryColor: Color,
    onCategoryColor: Color,
    favoriteColor: Color,
    name: String,
    categoryName: String = "",
    prepTime: Long,
    cookTime: Long,
    favorite: Boolean,
    selectedTabIndex: Int,
    tabs: List<TabItem>,
    onSelectedTabChange: (Int) -> Unit,
    onNavBackButtonClick: () -> Unit,
    isCategoryDialogOpen: Boolean,
    toggleCategoryDialog: () -> Unit,
    onSaveCategoryClick: () -> Unit,
    onCancelCategoryClick: () -> Unit,
    prepTimePicker: TimePickerDialog,
    onSavePrepTime: (Long) -> Unit,
    cookTimePicker: TimePickerDialog,
    onSaveCookTime: (Long) -> Unit,
    onToggleFavorite: () -> Unit
) {
    if(isNewRecipe) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                // TODO: dismiss new recipe dialog
            },
            title = {
                Text(text = "Title")
            },
            text = {
                // TODO show recipe name and description textFields
                Text(text = "Turned on by default")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // TODO save the new recipe
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // TODO: dismiss new recipe dialog
                    }
                ) {
                    Text("Dismiss")
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
                        // TODO: Add any actions to the appbar
                    })
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
                            onTextClicked = { toggleCategoryDialog() },
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
                            text = "Prep: ${prepTime.toInt()}",
                            modifier = Modifier
                                .clickable { prepTimePicker.show() }
                                .padding(start = 4.dp),
                        )
                        Text(
                            text = "Cook: ${cookTime!!.toInt() / 60}:${cookTime!!.toInt() % 60}",
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
                            TabOrder.STEP.ordinal -> { StepsTabScreen(
                                recipeId = recipeId
                            ) }
                            TabOrder.INGREDIENT.ordinal -> { IngredientsTabScreen(snackbarHostState = snackbarHostState) }
                            TabOrder.TIP.ordinal -> { TipsTabScreen() }
                        }
                    }
                }
            }
        }
    }
}


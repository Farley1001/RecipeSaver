package com.farware.recipesaver.feature_recipe.presentation.recipes

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategoryAndColor
import com.farware.recipesaver.feature_recipe.domain.util.RecipeOrder
import com.farware.recipesaver.feature_recipe.domain.util.RecipeSearch
import com.farware.recipesaver.feature_recipe.presentation.Screen
import com.farware.recipesaver.feature_recipe.presentation.appbar.*
import com.farware.recipesaver.feature_recipe.presentation.recipes.components.OrderSection
import com.farware.recipesaver.feature_recipe.presentation.recipes.components.RecipeItem
import com.farware.recipesaver.feature_recipe.presentation.recipes.components.SearchSection
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.fabShape
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.mainTitle
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun RecipesScreen(
    navController: NavController,
    navDrawerState: DrawerState,
    viewModel: RecipesViewModel = hiltViewModel()
) {
    Log.d(TAG, "RecipesScreen: Start")
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.state.value

    val actionItems = listOf(
        ActionItem("Favorite", Icons.Default.Favorite, OverflowMode.NEVER_OVERFLOW) {
            viewModel.onEvent(
                RecipesEvent.ToggleFavorites
            )
        },
        ActionItem("Search", Icons.Default.Search, OverflowMode.IF_NECESSARY) {
            viewModel.onEvent(
                RecipesEvent.ToggleSearchSection
            )
        },
        ActionItem("Sort", Icons.Default.Sort, OverflowMode.IF_NECESSARY) {
            viewModel.onEvent(
                RecipesEvent.ToggleOrderSection
            )
        },
        //ActionItem("Delete", Icons.Default.Delete, OverflowMode.IF_NECESSARY) {},
    )

    fun navButtonOpenClicked() {
        scope.launch { navDrawerState.open() }
    }

    fun navButtonCloseClicked(arg: String) {
        var route = arg
        when (arg) {
            "sign_out" -> {
                viewModel.signOut()
                route = Screen.LoginScreen.route
            }
            else -> {
            }
        }
        scope.launch {
            navController.navigate(route)
            navDrawerState.close()
        }
    }

    fun addEditRecipeButtonClicked() {
        navController.navigate(Screen.RecipeScreen.withArgs(
            "-1"
        ))
        //navController.navigate(Screen.AddEditRecipeScreen.route)
        //Toast.makeText(context, "Add/Edit Recipe Click", Toast.LENGTH_LONG).show()
    }

    fun recipeItemClicked(recipe: RecipeWithCategoryAndColor) {
        navController.navigate(Screen.RecipeScreen.withArgs(
            recipe.recipeId.toString()
        ))
        //Toast.makeText(context, "RecipeItem Click", Toast.LENGTH_LONG).show()
    }

    fun recipeItemLongClicked(recipe: RecipeWithCategoryAndColor) {
        navController.navigate(Screen.AddEditRecipeScreen.withArgs(
            recipe.recipeId.toString()
        ))
        Toast.makeText(context, "RecipeItem Long Click", Toast.LENGTH_LONG).show()
    }

    /*fun recipeItemDeleteClicked(recipe: Recipe) {
        viewModel.onEvent(RecipesEvent.DeleteRecipe(recipe))
        // TODO: make this a dialog

        scope.launch {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message = "Recipe Deleted",
                actionLabel = "Undo"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.onEvent(RecipesEvent.RestoreRecipe)
            }
        }
    }*/

    RecipesContent(
        navDrawerState = navDrawerState,
        recipeItems = state.recipes,
        actionItems = actionItems,
        context = context,
        searchStringText = viewModel.searchString.value.text,
        searchStringHint = viewModel.searchString.value.hint,
        searchStringHintVisible = viewModel.searchString.value.isHintVisible,
        hasSearchString = viewModel.searchString.value.text != "",
        selectFavoritesOnly = state.isSelectFavoritesOnly,
        searchSectionVisible = state.isSearchSectionVisible,
        orderSectionVisible = state.isOrderSectionVisible,
        showDeleteDialog = state.showDeleteDialog,
        recipeOrder = state.recipeOrder,
        recipeSearch = state.recipeSearch,
        onSearchChange = { viewModel.onEvent(it) },
        onSearchBoxValueChange = { viewModel.onEvent(it) },
        onSearchBoxFocusChange = { viewModel.onEvent(it) },
        onSearchBoxClearClick = { viewModel.onEvent(RecipesEvent.ClearSearch) },
        onOrderChange = { viewModel.onEvent(it) },
        onNavButtonOpenClick = { navButtonOpenClicked() },
        onNavButtonCloseClick = { navButtonCloseClicked(it) },
        onAddEditRecipeButtonClick = { addEditRecipeButtonClicked() },
        onRecipeItemClick = { recipeItemClicked(it) },
        onRecipeItemLongClick = { recipeItemLongClicked(it) },
        onRecipeItemDeleteClick = { viewModel.onEvent(RecipesEvent.DeleteRecipe(it)) },
        onConfirmDeleteRecipeClick = { viewModel.onEvent(RecipesEvent.DeleteConfirmed) },
        onCancelDeleteRecipeClick = { viewModel.onEvent(RecipesEvent.DeleteCanceled) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun RecipesContent(
    navDrawerState: DrawerState,
    recipeItems: List<RecipeWithCategoryAndColor>,
    actionItems: List<ActionItem>,
    context: Context,
    searchStringText: String,
    searchStringHint: String,
    searchStringHintVisible: Boolean,
    hasSearchString: Boolean,
    selectFavoritesOnly: Boolean,
    searchSectionVisible: Boolean,
    orderSectionVisible: Boolean,
    showDeleteDialog: Boolean,
    recipeOrder: RecipeOrder,
    recipeSearch: RecipeSearch,
    onSearchChange: (RecipesEvent.Search) -> Unit,
    onSearchBoxValueChange: (RecipesEvent.EnteredSearch) -> Unit,
    onSearchBoxFocusChange: (RecipesEvent.ChangeSearchFocus) -> Unit,
    onSearchBoxClearClick: () -> Unit,
    onOrderChange: (RecipesEvent.Order) -> Unit,
    onNavButtonOpenClick: () -> Unit,
    onNavButtonCloseClick: (String) -> Unit,
    onAddEditRecipeButtonClick: () -> Unit,
    onRecipeItemClick: (RecipeWithCategoryAndColor) -> Unit,
    onRecipeItemLongClick: (RecipeWithCategoryAndColor) -> Unit,
    onRecipeItemDeleteClick: (Recipe) -> Unit,
    onConfirmDeleteRecipeClick: () -> Unit,
    onCancelDeleteRecipeClick: () -> Unit

) {

    ModalNavigationDrawer(
        drawerContent = { NavDrawerMenu(selectedMenuItem = "recipes_screen", onNavButtonCloseClick =  onNavButtonCloseClick) },
        drawerState = navDrawerState,
        //drawerShape = RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp),
        //drawerTonalElevation = 5.dp
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onNavButtonOpenClick()
                            }
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = "Open Nav")
                        }
                    },
                    title = {
                        Text(
                            text = "Recipes",
                            style = mainTitle,
                            //TODO: Color
                            //color = MaterialTheme.colors.onPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                            modifier = Modifier
                                .width(130.dp)
                        )
                    },
                    actions = {
                        // show 3 icons including overflow
                        ActionMenu(
                            items = actionItems,
                            context = context,
                            hasSearchString = hasSearchString,
                            selectFavoritesOnly = selectFavoritesOnly,
                            searchSectionVisible = searchSectionVisible,
                            orderSectionVisible = orderSectionVisible
                        )
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    modifier = Modifier.padding(1.dp),
                    onClick = {
                        onAddEditRecipeButtonClick()
                    },
                    //TODO: Color
                    //backgroundColor = MaterialTheme.colorScheme.primary,
                    shape = fabShape,
                    icon = {
                        Icon(
                            modifier = Modifier.padding(1.dp),
                            imageVector = Icons.Default.Add,
                            contentDescription = "New Recipe"
                        )
                    },
                    text = {
                        Text(
                            modifier = Modifier.padding(1.dp),
                            text = "New Recipe",
                        )
                    }
                )
                /*
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Recipe")
            */
            }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                AnimatedVisibility(
                    visible = orderSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    OrderSection(
                        recipeOrder = recipeOrder,
                        onOrderChange = { onOrderChange(RecipesEvent.Order(it)) }
                    )
                }
                AnimatedVisibility(
                    visible = searchSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    SearchSection(
                        searchStringText = searchStringText,
                        searchStringHint = searchStringHint,
                        isHintVisible = searchStringHintVisible,
                        searchFieldsEnabled = hasSearchString,
                        selectedSearch = recipeSearch,
                        onSearchChange = { onSearchChange(RecipesEvent.Search(it)) },
                        onSearchBoxValueChange = {
                            onSearchBoxValueChange(
                                RecipesEvent.EnteredSearch(it)
                            )
                        },
                        onSearchBoxFocusChange = {
                            onSearchBoxFocusChange(
                                RecipesEvent.ChangeSearchFocus(
                                    it
                                )
                            )
                        },
                        onSearchBoxClearClick = { onSearchBoxClearClick() },
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(recipeItems) { recipe ->
                        RecipeItem(
                            recipe = recipe,
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
                                        onRecipeItemClick(recipe)
                                    },
                                    onLongClick = {
                                        onRecipeItemLongClick(recipe)
                                    }
                                ),
                            onDeleteClick = {
                                onRecipeItemDeleteClick(recipe.toRecipe())
                            }
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    }
                }
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { },
                    title = {
                        Text(text = "Confirm Delete")
                    },
                    text = {
                        Text(text = "Are you sure you want to delete this recipe?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onConfirmDeleteRecipeClick()
                            }
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                onCancelDeleteRecipeClick()
                            }
                        ) {
                            Text("Dismiss")
                        }
                    }
                )
            }
        }
    }
}
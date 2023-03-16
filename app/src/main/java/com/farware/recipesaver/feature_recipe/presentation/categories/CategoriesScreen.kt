package com.farware.recipesaver.feature_recipe.presentation.categories

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.domain.util.CategoryOrder
import com.farware.recipesaver.feature_recipe.presentation.appbar.ActionItem
import com.farware.recipesaver.feature_recipe.presentation.appbar.ActionMenu
import com.farware.recipesaver.feature_recipe.presentation.appbar.NavDrawerMenu
import com.farware.recipesaver.feature_recipe.presentation.appbar.OverflowMode
import com.farware.recipesaver.feature_recipe.presentation.categories.components.CategoryItem
import com.farware.recipesaver.feature_recipe.presentation.categories.components.OrderSection
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.fabShape
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.mainTitle
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navDrawerState: DrawerState,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()

    val actionItems = listOf(
        ActionItem("Sort", Icons.Default.Sort, OverflowMode.IF_NECESSARY) {
            viewModel.onEvent(
                CategoriesEvent.ToggleOrderSection
            )
        },
        //ActionItem("Delete", Icons.Default.Delete, OverflowMode.IF_NECESSARY) {},
    )

    fun navButtonOpenClicked() {
        scope.launch { navDrawerState.open() }
    }

    fun navButtonCloseClicked(route: String) {
        viewModel.onEvent(CategoriesEvent.NavMenuNavigate(route = route))
        scope.launch {
            navDrawerState.close()
        }
    }

    CategoriesContent(
        navDrawerState = navDrawerState,
        categoryItems = state.categories,
        actionItems = actionItems,
        context = context,
        hasSearchString = false,
        selectFavoritesOnly = false,
        searchSectionVisible = false,
        orderSectionVisible = state.isOrderSectionVisible,
        showDeleteDialog = state.showDeleteDialog,
        categoryOrder = state.categoryOrder,
        onOrderChange = { viewModel.onEvent(it) },
        onNavButtonOpenClick = { navButtonOpenClicked() },
        onNavButtonCloseClick = { navButtonCloseClicked(it) },
        onAddEditCategoryButtonClick = { viewModel.onEvent(CategoriesEvent.NewCategory) },
        onCategoryItemClick = { viewModel.onEvent(CategoriesEvent.NavigateToCategory(it)) },
        onCategoryItemDeleteClick = { viewModel.onEvent(CategoriesEvent.DeleteCategory(it)) },
        onConfirmDeleteCategoryClick = { viewModel.onEvent(CategoriesEvent.DeleteConfirmed) },
        onCancelDeleteCategoryClick = { viewModel.onEvent(CategoriesEvent.DeleteCanceled) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    navDrawerState: DrawerState,
    categoryItems: List<Category>,
    actionItems: List<ActionItem>,
    context: Context,
    hasSearchString: Boolean,
    selectFavoritesOnly: Boolean,
    searchSectionVisible: Boolean,
    orderSectionVisible: Boolean,
    showDeleteDialog: Boolean,
    categoryOrder: CategoryOrder,
    onOrderChange: (CategoriesEvent.Order) -> Unit,
    onNavButtonOpenClick: () -> Unit,
    onNavButtonCloseClick: (String) -> Unit,
    onAddEditCategoryButtonClick: () -> Unit,
    onCategoryItemClick: (Long) -> Unit,
    onCategoryItemDeleteClick: (Category) -> Unit,
    onConfirmDeleteCategoryClick: () -> Unit,
    onCancelDeleteCategoryClick: () -> Unit
) {
    ModalNavigationDrawer(
        drawerContent = { NavDrawerMenu(selectedMenuItem = "categories_screen", onNavButtonCloseClick =  onNavButtonCloseClick) },
        drawerState = navDrawerState,
        //drawerShape = RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp),
        //drawerTonalElevation = 5.dp
    ) {
        Scaffold(
            topBar = {
                // show 3 icons including overflow
                TopAppBar(title = {
                    Text(
                        text = "Categories",
                        style = mainTitle,
                        //TODO: Fix color...copy from recipes
                        //color = MaterialTheme.colors.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier
                            .width(130.dp)
                    )
                },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onNavButtonOpenClick()
                            }
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = "Open Nav")
                        }
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
                    })
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    modifier = Modifier.padding(1.dp),
                    onClick = {
                        onAddEditCategoryButtonClick()
                    },
                    //TODO: Fix color
                    //backgroundColor = MaterialTheme.colors.primary,
                    shape = fabShape,
                    icon = {
                        Icon(
                            modifier = Modifier.padding(1.dp),
                            imageVector = Icons.Default.Add,
                            contentDescription = "New Category"
                        )
                    },
                    text = {
                        Text(
                            modifier = Modifier.padding(1.dp),
                            text = "New Category"
                        )
                    }
                )
                /*{
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Recipe")
            }*/
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
                        categoryOrder = categoryOrder,
                        onOrderChange = { onOrderChange(CategoriesEvent.Order(it)) }
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(categoryItems) { category ->
                        CategoryItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCategoryItemClick(category.categoryId!!)
                                },
                            category = category,
                            //recipesWithCategory = state.categories.filter { it.categoryId == category.categoryId }.size,
                            onCategoryClick = { onCategoryItemClick(category.categoryId!!) },
                            onDeleteClick = {
                                onCategoryItemDeleteClick(category)
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
                        Text(text = "Are you sure you want to delete this Category?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onConfirmDeleteCategoryClick()
                            }
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                onCancelDeleteCategoryClick()
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
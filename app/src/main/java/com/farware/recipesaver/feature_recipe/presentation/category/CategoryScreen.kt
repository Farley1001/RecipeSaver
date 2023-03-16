package com.farware.recipesaver.feature_recipe.presentation.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.presentation.category.components.ColorPickerDialog
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.mainTitle
import com.farware.recipesaver.feature_recipe.presentation.util.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CategoryScreen (
    snackbarHostState: SnackbarHostState,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.Success -> {
                    snackbarHostState.showSnackbar(
                        message = "Save Succeeded"
                    )
                }
            }
        }
    }

    CategoryContent(
        focusManager = focusManager,
        snackbarHostState = snackbarHostState,
        category = viewModel.category.value,
        showLightColorPicker = state.showLightColorPicker,
        showDarkColorPicker = state.showDarkColorPicker,
        onCategoryNameTextChanged = { viewModel.onEvent(CategoryEvent.CategoryNameTextChanged(it)) },
        onToggleLightColorPicker = { viewModel.onEvent(CategoryEvent.ToggleLightColorPicker) },
        onToggleDarkColorPicker = { viewModel.onEvent(CategoryEvent.ToggleDarkColorPicker) },
        onChangeLightColorClick = { viewModel.onEvent(CategoryEvent.ChangeLightColor(it)) },
        onChangeDarkColorClick = { viewModel.onEvent(CategoryEvent.ChangeDarkColor(it)) },
        onSaveCategoryClick = { viewModel.onEvent(CategoryEvent.SaveCategory) },
        onCancelCategoryClick = { viewModel.onEvent(CategoryEvent.NavigateBack) },
        onNavBackButtonClick = { viewModel.onEvent(CategoryEvent.NavigateBack) },
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CategoryContent(
    focusManager: FocusManager,
    snackbarHostState: SnackbarHostState,
    category: Category,
    showLightColorPicker: Boolean,
    showDarkColorPicker: Boolean,
    onCategoryNameTextChanged: (String) -> Unit,
    onToggleLightColorPicker: () -> Unit,
    onToggleDarkColorPicker: () -> Unit,
    onChangeLightColorClick: (Int) -> Unit,
    onChangeDarkColorClick: (Int) -> Unit,
    onSaveCategoryClick: () -> Unit,
    onCancelCategoryClick: () -> Unit,
    onNavBackButtonClick: () -> Unit,
) {

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = {
                Text(
                    text = category.name,
                    style = mainTitle,
                    color = Color(category.onBackground(isSystemInDarkTheme())),
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
                    /*IconButton(
                        onClick = {
                           // TODO
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EditNote,
                            contentDescription = "Localized description"
                        )
                    }*/
                }
            )
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValue.calculateTopPadding(), start = 8.dp, end = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(showLightColorPicker) {
                ColorPickerDialog(
                    dialogTitle = "Light Theme",
                    backgroundColor = Color.White,
                    currentColor = Color(category.lightThemeColor),
                    currentOnColor = Color(category.onLightThemeColor),
                    onChangeColorClick = { onChangeLightColorClick(it) },
                    onDismissDialogClick = { onToggleLightColorPicker() },
                )
            }
            if(showDarkColorPicker) {
                ColorPickerDialog(
                    dialogTitle = "Dark Theme",
                    backgroundColor = Color.Black,
                    currentColor = Color(category.darkThemeColor),
                    currentOnColor = Color(category.onDarkThemeColor),
                    onChangeColorClick = { onChangeDarkColorClick(it) },
                    onDismissDialogClick = { onToggleDarkColorPicker() },
                )
            }
            OutlinedTextField(
                label = {
                    Text(text = "Category") },
                value = category.name,
                onValueChange = {
                   onCategoryNameTextChanged(it)
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {focusManager.clearFocus()})
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp)
                        .width(80.dp)
                        .height(80.dp)
                        .clickable {
                            onToggleLightColorPicker()
                        }
                        .background(Color(category.lightThemeColor))
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Light Theme",
                        color = Color(category.onLightThemeColor)
                    )
                }
                Text(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(8.dp),
                    text = "Put some text here for the light theme color",
                    color = Color(category.onLightThemeColor)
                )
            }
            //Spacer(modifier = Modifier.height(48.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.Black),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp)
                        .width(80.dp)
                        .height(80.dp)
                        .background(Color(category.darkThemeColor))
                        .clickable {
                            onToggleDarkColorPicker()
                        }
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Dark Theme",
                        color = Color(category.onDarkThemeColor)
                    )
                }
                Text(
                    modifier = Modifier
                        .background(Color.Black)
                        .padding(8.dp),
                    text = "Put some text here for the dark theme color",
                    color = Color(category.onDarkThemeColor)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Button(onClick = { onCancelCategoryClick() }) {
                    Text(text = "Cancel")
                }
                Button(onClick = { onSaveCategoryClick() }) {
                    Text(text = "Save")
                }
            }
        }
    }
}
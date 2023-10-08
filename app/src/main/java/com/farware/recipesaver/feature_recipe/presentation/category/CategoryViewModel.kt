package com.farware.recipesaver.feature_recipe.presentation.category

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.domain.use_cases.RecipeUseCases
import com.farware.recipesaver.feature_recipe.presentation.navigation.AppNavigator
import com.farware.recipesaver.feature_recipe.presentation.navigation.Destination
import com.farware.recipesaver.feature_recipe.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val categoryUseCases: RecipeUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = mutableStateOf(CategoryState())
    val state: State<CategoryState> = _state

    private var _newCategory = mutableStateOf(Category(categoryId = -1, name = "", lightThemeColor = -1, onLightThemeColor = -1, darkThemeColor = -1, onDarkThemeColor = -1, timeStamp = 0))
    val newCategory: State<Category> = _newCategory

    private val _category = mutableStateOf(newCategory.value)
    val category: State<Category> = _category

    /*private val _categoryName = mutableStateOf(
        CategoryTextFieldState(
            hint = "Enter a category name..."
        )
    )
    val categoryName: State<CategoryTextFieldState> = _categoryName*/

    private val _categoryColor = mutableStateOf(category.value.darkThemeColor)
    val categoryColor: State<Int> = _categoryColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentCategoryId: Long? = null

    init {
        savedStateHandle.get<Long>("categoryId")?.let { categoryId ->
            if(categoryId != -1L) {
                viewModelScope.launch {
                    categoryUseCases.getCategory(categoryId)?.also { category ->
                        currentCategoryId = category.categoryId
                        _category.value = category
//                        _categoryName.value = categoryName.value.copy(
//                            text = category.name,
//                            isHintVisible = false
//                        )
//                        //TODO: Fix color
//                        _categoryColor.value = category.darkThemeColor

                    }
                }
            }
        }
    }

    fun onEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.EnteredName -> {
//                _categoryName.value = categoryName.value.copy(
//                    text = event.value
//                )
            }
            is CategoryEvent.ChangeNameFocus -> {
//                _categoryName.value = categoryName.value.copy(
//                    isHintVisible = !event.focusState.isFocused &&
//                            categoryName.value.text.isBlank()
//                )
            }
            is CategoryEvent.CategoryNameTextChanged -> {
                _category.value = category.value.copy(
                    name = event.category
                )
            }
            is CategoryEvent.ChangeLightColor -> {
                _category.value = category.value.copy(
                    lightThemeColor = event.color
                )
                _state.value = state.value.copy(
                    showLightColorPicker = false
                )
            }
            is CategoryEvent.ChangeDarkColor -> {
                _category.value = category.value.copy(
                    darkThemeColor = event.color
                )
                _state.value = state.value.copy(
                    showDarkColorPicker = false
                )
            }
            is CategoryEvent.ToggleLightColorPicker -> {
                _state.value = state.value.copy(
                    showLightColorPicker = !state.value.showLightColorPicker
                )
            }
            is CategoryEvent.ToggleDarkColorPicker -> {
                _state.value = state.value.copy(
                    showDarkColorPicker = !state.value.showDarkColorPicker
                )
            }
            is CategoryEvent.SaveCategory -> {
                // save the category color here
                viewModelScope.launch {
                    categoryUseCases.insertCategory(category.value)
                }

                _state.value = state.value.copy(
                    showDarkColorPicker = false,
                    showLightColorPicker = false
                )

                appNavigator.tryNavigateTo(Destination.CategoriesScreen())
            }
            is CategoryEvent.NavigateBack -> {
                appNavigator.tryNavigateTo(Destination.CategoriesScreen())
            }
        }
    }
}
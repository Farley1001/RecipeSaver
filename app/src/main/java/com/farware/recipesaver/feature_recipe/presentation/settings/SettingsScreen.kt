package com.farware.recipesaver.feature_recipe.presentation.settings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureType
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureUnit
import com.farware.recipesaver.feature_recipe.presentation.appbar.NavDrawerMenu
import com.farware.recipesaver.feature_recipe.presentation.navigation.Destination
import com.farware.recipesaver.feature_recipe.presentation.recipes.RecipesEvent
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.mainTitle
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import com.farware.recipesaver.feature_recipe.presentation.util.LoadingState
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    navDrawerState: DrawerState,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val status by viewModel.loadingState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val selectedMenuItem = remember { mutableStateOf("settings_screen") }

    val state = viewModel.state.value

    when (status.status) {
        LoadingState.Status.SUCCESS -> {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
        LoadingState.Status.FAILED -> {
            Toast.makeText(context, status.msg ?: "Error", Toast.LENGTH_LONG).show()
        }
        else -> {}
    }

    fun navButtonOpenClicked() {
        scope.launch { navDrawerState.open() }
    }

    fun navButtonCloseClicked(route: String) {
        viewModel.onEvent(SettingsEvent.NavMenuNavigate(route = route))
        scope.launch {
            navDrawerState.close()
        }
    }

    SettingsContent(
        navDrawerState = navDrawerState,
        displayName = state.displayName,
        onDisplayNameChange = { viewModel.onEvent(SettingsEvent.DisplayNameValueChange(it)) },
        displayNameIsEditing = state.displayNameIsEditing,
        editDisplayName = { viewModel.onEvent(SettingsEvent.EditDisplayName) },
        saveDisplayName = { viewModel.onEvent(SettingsEvent.SaveDisplayName(context)) },
        clearDisplayName = { viewModel.onEvent(SettingsEvent.ClearDisplayName)},
        email = state.displayEmail,
        showDynamicColorOption = state.showDynamicColorOption,
        useDynamicColor = state.useDynamicColor,
        toggleDynamicColor = { viewModel.onEvent(SettingsEvent.ToggleDynamicColor(context))},
        imperialMeasureChecked = state.measureType == MeasureType.IMPERIAL,
        toggleImperialMeasure = { viewModel.onEvent(SettingsEvent.ToggleImperialMeasure(context)) },
        millilitersChecked = state.measureUnit == MeasureUnit.MILLILITERS,
        toggleMilliliterUnits = { viewModel.onEvent(SettingsEvent.ToggleMilliliterUnits(context)) },
        onNavButtonOpenClick = { navButtonOpenClicked() },
        onNavButtonCloseClick = { navButtonCloseClicked(it) },
    )
}

@ExperimentalMaterial3Api
@Composable
fun SettingsContent(
    navDrawerState: DrawerState,
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    displayNameIsEditing: Boolean,
    editDisplayName: () -> Unit,
    saveDisplayName: () -> Unit,
    clearDisplayName: () -> Unit,
    email: String,
    showDynamicColorOption: Boolean,
    useDynamicColor: Boolean,
    toggleDynamicColor: () -> Unit,
    imperialMeasureChecked: Boolean,
    toggleImperialMeasure: () -> Unit,
    millilitersChecked: Boolean,
    toggleMilliliterUnits: () -> Unit,
    onNavButtonOpenClick: () -> Unit,
    onNavButtonCloseClick: (String) -> Unit,
) {
    val dynamicColorSwitchIcon: (@Composable () -> Unit)? = if (useDynamicColor) {
        {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }
    val measureTypeSwitchIcon: (@Composable () -> Unit)? = if (imperialMeasureChecked) {
        {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }
    val measureUnitSwitchIcon: (@Composable () -> Unit)? = if (millilitersChecked) {
        {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }


    ModalNavigationDrawer(
        drawerContent = { NavDrawerMenu(selectedMenuItem = "settings_screen", onNavButtonCloseClick =  onNavButtonCloseClick) },
        drawerState = navDrawerState,
        //drawerShape = RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp),
        //drawerTonalElevation = 20.dp
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Settings",
                        style = mainTitle,
                        // TODO: Color
                        //color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier
                            .width(130.dp)
                    )
                },
                    // TODO: Color
                    //backgroundColor = MaterialTheme.colorScheme.primary,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onNavButtonOpenClick()
                            }
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = "Open Nav")
                        }
                    })
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 18.dp, end = 18.dp, top = it.calculateTopPadding()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Email: $email",
                    style = TextStyle(
                        fontSize = 20.sp
                    ),
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumSmall))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (displayNameIsEditing) {
                        TextField(
                            value = displayName,
                            onValueChange = { onDisplayNameChange(it) },
                            singleLine = true,
                            // TODO: Color
                            //textStyle = TextStyle(color = MaterialTheme.extraColors.atfTextColor, fontSize = 24.sp),
                            label = {
                                Text(
                                    text = "Display Name",
                                    // TODO: Color
                                    //color = MaterialTheme.extraColors.atfLabelColor
                                )

                            },
                            trailingIcon = {
                                IconButton(onClick = { clearDisplayName() }) {
                                    Icon(
                                        Icons.Filled.Clear,
                                        contentDescription = "Clear Icon",
                                        // TODO: Color
                                        //tint = MaterialTheme.extraColors.atfLabelColor
                                    )
                                }
                            },
                            // TODO: Color
                            //colors = TextFieldDefaults.textFieldColors(
                            //    cursorColor = MaterialTheme.extraColors.atfCursorColor,
                            //    backgroundColor = MaterialTheme.extraColors.atfBgColor,
                            //    focusedIndicatorColor = MaterialTheme.extraColors.atfFocusIndicatorColor
                            //),
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                            //.fillMaxWidth()
                        )
                        IconButton(onClick = { saveDisplayName() }) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "save icon",
                            )
                        }
                    } else {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = "Your display name is $displayName",
                            style = TextStyle(
                                fontSize = 16.sp
                            )
                        )
                        IconButton(
                            onClick = { editDisplayName() }
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "edit icon",
                            )
                        }
                    }
                }
                if(showDynamicColorOption) {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumSmall))
                    Text(
                        text = "Use dynamic colors \nDynamic colors is set to: $useDynamicColor"
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    Switch(
                        checked = useDynamicColor,
                        onCheckedChange = { toggleDynamicColor() },
                        thumbContent = dynamicColorSwitchIcon,
                        // TODO: Color
                        //colors = SwitchDefaults.colors(
                        //    checkedThumbColor = MaterialTheme.extraColors.swtCheckedThumbColor,
                        //    uncheckedThumbColor = MaterialTheme.extraColors.swtUncheckedThumbColor
                        //)
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumSmall))
                Text(
                    text = "Use Imperial Measurements (default is US)\nUsing Imperial Measure: $imperialMeasureChecked"
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Switch(
                    checked = imperialMeasureChecked,
                    onCheckedChange = { toggleImperialMeasure() },
                    thumbContent = measureTypeSwitchIcon,
                    // TODO: Color
                    //colors = SwitchDefaults.colors(
                    //    checkedThumbColor = MaterialTheme.extraColors.swtCheckedThumbColor,
                    //    uncheckedThumbColor = MaterialTheme.extraColors.swtUncheckedThumbColor
                    //)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumSmall))
                Text(
                    text = "Use Milliliters (default is Fluid Ounces - US or Imperial)\nUsing Milliliters: $millilitersChecked"
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Switch(
                    checked = millilitersChecked,
                    onCheckedChange = { toggleMilliliterUnits() },
                    thumbContent = measureUnitSwitchIcon,
                    // TODO: Color
                    //colors = SwitchDefaults.colors(
                    //    checkedThumbColor = MaterialTheme.extraColors.swtCheckedThumbColor,
                    //    uncheckedThumbColor = MaterialTheme.extraColors.swtUncheckedThumbColor
                    //)
                )
            }
        }
    }
}

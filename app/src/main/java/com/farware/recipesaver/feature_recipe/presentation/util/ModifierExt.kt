package com.farware.recipesaver.feature_recipe.presentation.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

// this adds the mdeiaQuery extension to the compose MODIFIER

fun Modifier.mediaQuery(
    comparator: Dimensions.DimensionsComparator,
    modifier: Modifier
): Modifier = composed {

    // get current device screen width and height by setting the return type to composed
    val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels.dp /
            LocalDensity.current.density
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels.dp /
            LocalDensity.current.density

    // if true then append this modifier to the current modifier
    // else just return the current modifier
    if(comparator.compare(screenWidth, screenHeight)) {
        this.then(modifier)
    } else this
}


// this adds the customDialogPosition extension to the compose MODIFIER

fun Modifier.customDialogPosition(pos: CustomDialogPosition) = layout { measurable, constraints ->

    val placeable = measurable.measure(constraints);
    layout(constraints.maxWidth, constraints.maxHeight){
        when(pos) {
            CustomDialogPosition.BOTTOM -> {
                placeable.place(0, constraints.maxHeight - placeable.height, 10f)
            }
            CustomDialogPosition.TOP -> {
                placeable.place(0,0,10f)
            }
        }
    }
}
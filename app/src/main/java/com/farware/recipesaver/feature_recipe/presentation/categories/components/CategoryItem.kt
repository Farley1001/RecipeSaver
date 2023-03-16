package com.farware.recipesaver.feature_recipe.presentation.categories.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    //recipesWithCategory: Int,
    onCategoryClick: (Category) -> Unit,
    onDeleteClick: () -> Unit
) {
    CategoryItemContent(
        modifier = modifier,
        category = category,
        //recipesWithCategory = recipesWithCategory,
        onCategoryClick = { onCategoryClick(it) },
        onDeleteClick = onDeleteClick
    )

}

@Composable fun CategoryItemContent(
    modifier: Modifier,
    category: Category,
    //recipesWithCategory: Int,
    onCategoryClick: (Category) -> Unit,
    onDeleteClick: () -> Unit
){
    Column(modifier = Modifier.clickable { onCategoryClick(category) }) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 6.dp, top = 6.dp, end = 6.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp
                    )
                )     //, bottomEnd = 10.dp
                .background(Color(category.background(isSystemInDarkTheme())))
                .width(90.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = category.name,
                    color = Color(category.onBackground(isSystemInDarkTheme())),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                /*Text(
                    modifier = Modifier.padding(4.dp),
                    text = recipesWithCategory.toString(),
                    color = Color(category.onBackground(isSystemInDarkTheme())),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End
                )*/
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, bottom = 6.dp, end = 6.dp)
                .width(90.dp)
            ,
            //horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    //.padding(start = 6.dp)
                    .clip(RoundedCornerShape(bottomStart = 10.dp))
                    .background(Color(category.darkThemeColor))
                    .width(84.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = "Dark",
                    color = Color(category.onDarkThemeColor),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    //.padding(end =6.dp)
                    .clip(RoundedCornerShape(bottomEnd = 10.dp))
                    .background(Color(category.lightThemeColor))
                    .width(84.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = "Light",
                    color = Color(category.onLightThemeColor),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
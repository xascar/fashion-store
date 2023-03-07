package com.cyberwalker.fashionstore.favorites
/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cyberwalker.fashionstore.R
import com.cyberwalker.fashionstore.detail.DetailScreenActions
import com.cyberwalker.fashionstore.home.HomeScreenActions
import com.cyberwalker.fashionstore.login.LoginViewModel
import com.cyberwalker.fashionstore.scaffold.ScaffoldComposable

@Composable
fun FavoritesScreen(
    scaffoldState: ScaffoldState,
    onHomeActions: (actions: HomeScreenActions) -> Unit
) {
    ScaffoldComposable(scaffoldState = scaffoldState,
        onHomeActions = onHomeActions)
}



@Composable
fun FavoritesScreenContent(
    modifier: Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onAction: (actions: HomeScreenActions) -> Unit,
) {

    val favoriteItems = listOf(
        FavoriteItem("Item 1", R.drawable.fav_1, "$10.99"),
        FavoriteItem("Item 2", R.drawable.fav_2, "$12.99"),
        FavoriteItem("Item 3", R.drawable.fav_3, "$14.99"),
        FavoriteItem("Item 4", R.drawable.fav_4, "$13.99"),
        FavoriteItem("Item 5", R.drawable.fav_5, "$12.99"),
        FavoriteItem("Item 6", R.drawable.fav_6, "$11.99"),
        FavoriteItem("Item 7", R.drawable.fav_7, "$10.99"),
    )
    LazyColumn {
        items(favoriteItems) { item ->
            FavoriteItemRow(item = item)
        }
    }
}

@Composable
fun FavoriteItemRow(item: FavoriteItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(item.image),
            contentDescription = item.title,
            modifier = Modifier.size(80.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.price,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}

data class FavoriteItem(
    val title: String,
    @DrawableRes val image: Int,
    val price: String
)

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
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import com.cyberwalker.fashionstore.home.HomeScreenContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cyberwalker.fashionstore.R
import com.cyberwalker.fashionstore.dump.BottomNav
import com.cyberwalker.fashionstore.dump.BottomNavItem
import com.cyberwalker.fashionstore.dump.vertical
import com.cyberwalker.fashionstore.home.HomeScreenActions
import com.cyberwalker.fashionstore.login.LoginViewModel
import com.cyberwalker.fashionstore.ui.theme.*

@Composable
fun FavoritesScreen(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onAction: (actions: HomeScreenActions) -> Unit,
    navController: NavHostController
) {
    MyScaffold(scaffoldState = scaffoldState,
        onAction = onAction,
        navController = navController)
}

@Composable
fun MyScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onAction: (actions: HomeScreenActions) -> Unit,
    navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val (currentTab, setCurrentTab) = remember { mutableStateOf(BottomNavItem.Home.screen_route) }

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            BottomNav(navController = navController,
                currentTab = currentTab,
                setCurrentTab = setCurrentTab
            )
        }
    ) { innerPadding ->
        when (currentTab) {
            BottomNavItem.Home.screen_route -> {
                /* Fragment content */
                HomeScreenContent(modifier = Modifier.padding(innerPadding), onAction = onAction)

            }
            BottomNavItem.Liked.screen_route -> {
                FavoritesScreenContent(modifier = Modifier.padding(innerPadding), onAction = onAction)
            }
        }

    }
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

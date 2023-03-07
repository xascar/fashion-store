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
package com.cyberwalker.fashionstore.dump

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.cyberwalker.fashionstore.R
import com.cyberwalker.fashionstore.navigation.Screen
import com.cyberwalker.fashionstore.ui.theme.bottomNavbg
import com.cyberwalker.fashionstore.ui.theme.highlight

@Composable
fun BottomNav(isDark: Boolean = isSystemInDarkTheme(),
              currentTab : String,
              setCurrentTab : (String) -> Unit

) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Liked,
        BottomNavItem.Profile,
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.bottomNavbg,
        contentColor = highlight
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                selectedContentColor = highlight,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = false,
                selected =
                    if ((currentTab == Screen.Home.route  || currentTab == Screen.TargetCircle.route) && (item.screen_route == Screen.Home.route  || item.screen_route == Screen.TargetCircle.route)){
                        true
                    }
                    else
                    {
                        currentTab == item.screen_route
                    }
                           ,
                onClick = {
                    setCurrentTab(item.screen_route)
                }
            )
        }
    }
}

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
    object Home : BottomNavItem("Home", R.drawable.home, Screen.Home.route)
    object Target : BottomNavItem("Home", R.drawable.home, Screen.TargetCircle.route)
    object Search : BottomNavItem("Search", R.drawable.search, "Search")
    object Liked : BottomNavItem("Liked", R.drawable.liked, Screen.Favorites.route)
    object Profile : BottomNavItem("Profile", R.drawable.profile, "Profile")
}
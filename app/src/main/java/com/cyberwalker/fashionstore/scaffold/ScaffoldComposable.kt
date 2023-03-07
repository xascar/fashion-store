package com.cyberwalker.fashionstore.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.cyberwalker.fashionstore.circle.TargetCircleScreenContent
import com.cyberwalker.fashionstore.detail.DetailScreenActions
import com.cyberwalker.fashionstore.dump.BottomNav
import com.cyberwalker.fashionstore.dump.BottomNavItem
import com.cyberwalker.fashionstore.favorites.FavoritesScreenContent
import com.cyberwalker.fashionstore.home.HomeScreenActions
import com.cyberwalker.fashionstore.home.HomeScreenContent

@Composable
fun ScaffoldComposable(
    scaffoldState: ScaffoldState,
    onHomeActions: (actions: HomeScreenActions) -> Unit,
    onDetailAction: (actions: DetailScreenActions) -> Unit = {}
) {
    val (currentTab, setCurrentTab) = rememberSaveable { mutableStateOf(BottomNavItem.Home.screen_route) }

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            BottomNav(
                currentTab = currentTab,
                setCurrentTab = setCurrentTab
            )
        }
    ) { innerPadding ->
        when (currentTab) {
            BottomNavItem.Home.screen_route -> {
                HomeScreenContent(modifier = Modifier.padding(innerPadding), onAction = onHomeActions, setCurrentTab = setCurrentTab)
            }
            BottomNavItem.Liked.screen_route -> {
                FavoritesScreenContent(modifier = Modifier.padding(innerPadding), onAction = onHomeActions)
            }
            BottomNavItem.Target.screen_route -> {
                TargetCircleScreenContent(modifier = Modifier.padding(innerPadding), setCurrentTab = setCurrentTab)
            }
        }

    }
}
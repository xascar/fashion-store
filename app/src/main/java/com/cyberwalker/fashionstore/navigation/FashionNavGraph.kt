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
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.cyberwalker.fashionstore.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.navDeepLink
import com.cyberwalker.fashionstore.circle.TargetCircleScreen
import com.cyberwalker.fashionstore.detail.DetailScreen
import com.cyberwalker.fashionstore.detail.DetailScreenActions
import com.cyberwalker.fashionstore.dump.animatedComposable
import com.cyberwalker.fashionstore.favorites.FavoritesScreen
import com.cyberwalker.fashionstore.home.HomeScreen
import com.cyberwalker.fashionstore.home.HomeScreenActions
import com.cyberwalker.fashionstore.login.LoginScreen
import com.cyberwalker.fashionstore.splash.SplashScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

sealed class Screen(val name: String, val route: String) {
    object Splash : Screen("splash", "splash")
    object Login : Screen("login", "login")
    object Home : Screen("home", "home")
    object Favorites : Screen("favorites","favorites")
    object TargetCircle : Screen("circle","circle")
    object Detail : Screen("detail", "detail")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FashionNavGraph(
    modifier: Modifier = Modifier,
    viewModel: FashionViewModel = hiltViewModel(),
    navController: NavHostController = rememberAnimatedNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    actions: NavActions = remember(navController) {
        NavActions(navController)
    }
) {

    val uiState by viewModel.uiState.collectAsState()

    AnimatedNavHost(
        navController = navController,
        startDestination = uiState.startDestination,
        modifier = modifier
    ) {
        animatedComposable(Screen.Splash.route) {
            SplashScreen(onAction = actions::navigateToLogin)
        }

        animatedComposable(Screen.Login.route) {
            LoginScreen(onAction = actions::navigateToHome)
        }

        animatedComposable(Screen.Home.route) {
            HomeScreen(onAction = actions::navigateFromHome, scaffoldState = scaffoldState)
        }

        animatedComposable(Screen.Detail.route) {
            DetailScreen(onAction = actions::navigateFromDetails)
        }

        animatedComposable(Screen.Favorites.route) {
            FavoritesScreen(onHomeActions = actions::navigateFromHome, scaffoldState = scaffoldState)
        }

        animatedComposable(Screen.TargetCircle.route) {
            TargetCircleScreen(onHomeActions = actions::navigateFromHome, onDetailAction = actions::navigateFromDetails, scaffoldState = scaffoldState)
        }
    }
}

class NavActions(private val navController: NavController) {
    fun navigateToLogin() {
        navController.navigate(Screen.Login.name) {
            popUpTo(Screen.Login.route){
                inclusive = true
            }
        }
    }

    fun navigateToHome() {
        navController.navigate(Screen.Home.name) {
            popUpTo(Screen.Splash.route){
                inclusive = true
            }
        }
    }

    fun navigateFromHome(actions: HomeScreenActions) {
        when (actions) {
            HomeScreenActions.Details -> {
                navController.navigate(Screen.Detail.name)
            }
            HomeScreenActions.Login -> {
                navController.navigate(Screen.Login.name)
            }
        }
    }

    fun navigateFromDetails(actions: DetailScreenActions) {
        when(actions) {
            DetailScreenActions.Back -> navController.popBackStack()
        }
    }
}
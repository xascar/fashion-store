package com.cyberwalker.fashionstore.circle

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyberwalker.fashionstore.R
import com.cyberwalker.fashionstore.detail.DetailScreenActions
import com.cyberwalker.fashionstore.home.HomeScreenActions
import com.cyberwalker.fashionstore.login.LoginViewModel
import com.cyberwalker.fashionstore.scaffold.ScaffoldComposable
import com.cyberwalker.fashionstore.ui.theme.*

@Composable
fun TargetCircleScreen(
    scaffoldState: ScaffoldState,
    onHomeActions: (actions: HomeScreenActions) -> Unit,
    onDetailAction: (actions: DetailScreenActions) -> Unit
) {
    ScaffoldComposable(scaffoldState = scaffoldState,
        onHomeActions = onHomeActions,
        onDetailAction = onDetailAction)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TargetCircleScreenContent(
    modifier: Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    setCurrentTab : (String) -> Unit
) {
    LazyColumn {
        item {
            LazyRow {
                items(3) { index ->
                    Card(
                        elevation = 8.dp,
                        modifier = Modifier.padding(8.dp),
                        onClick = { /* Handle card click */ }
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.fav_1),
                                    contentDescription = "Favorite",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable(onClick = { /* Handle favorite click */ })
                                )
                                Text(
                                    text = "Card $index",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_launcher_foreground),
                                    contentDescription = "Item image",
                                    modifier = Modifier
                                        .size(64.dp)
                                )
                                Column(
                                    modifier = Modifier.padding(start = 16.dp)
                                ) {
                                    Text(
                                        text = "Item name",
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = "Item price",
                                        fontWeight = FontWeight.Normal,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            SwipeViewWithTabs(titles = listOf("Item 1", "Item 2", "Item 3"))
        }
    }
}

@Composable
fun SwipeViewWithTabs(
    titles: List<String>
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = MaterialTheme.colors.surface
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index }
                )
            }
        }
    }
}

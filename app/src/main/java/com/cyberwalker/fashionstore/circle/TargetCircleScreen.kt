package com.cyberwalker.fashionstore.circle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cyberwalker.fashionstore.R
import com.cyberwalker.fashionstore.detail.DetailScreenActions
import com.cyberwalker.fashionstore.dump.BottomNavItem
import com.cyberwalker.fashionstore.home.HomeScreenActions
import com.cyberwalker.fashionstore.scaffold.ScaffoldComposable
import com.cyberwalker.fashionstore.ui.theme.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


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
    viewModel: TargetCircleViewModel = hiltViewModel(),
    setCurrentTab : (String) -> Unit
) {

    BackHandler {
        setCurrentTab(BottomNavItem.Home.screen_route)
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = null,
            modifier = Modifier.clickable { setCurrentTab(BottomNavItem.Home.screen_route) }
        )
        Text(text = "Target Circle offers", modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1F))
        Image(painter = painterResource(id = R.drawable.baseline_search_24), contentDescription = null)
    }

    val circleOffersValue by viewModel.dataSet.observeAsState()
    circleOffersValue?.let {   circleOffers ->
        LazyColumn(modifier = modifier.padding(top = 40.dp)) {
            item {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(backgroundRed)
                ) {
                    Column(modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = "Top offers",fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "Get an even more rewarding Target run", fontWeight = FontWeight.Light, color = Color.White)
                    }
                }

                LazyRow(modifier = Modifier.background(backgroundRed)) {
                    items(circleOffers.size) { id ->

                        var favourite by remember {
                            mutableStateOf(circleOffers[id].favourite)
                        }

                        Card(
                            elevation = 8.dp,
                            modifier = Modifier
                                .padding(8.dp)
                                .width(280.dp),
                            onClick = { /* Handle card click */ }
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = circleOffers[id].itemName,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .weight(weight = 1F, fill = false)
                                    )
                                    Image(
                                        painter = painterResource( R.drawable.liked ),
                                        colorFilter =
                                        if (favourite) {
                                            ColorFilter.tint(Color.Black.copy(0.3f))
                                        }
                                        else {
                                            ColorFilter.tint(Color(android.graphics.Color.parseColor("#6ECB63")))
                                        }
                                        ,
                                        contentDescription = "Favorite",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(end = 8.dp)
                                            .clickable(onClick = {
                                                viewModel.updateDataAt(circleOffers[id])
                                            })
                                    )
                                }
                                Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    AsyncImage(
                                        model = circleOffers[id].image,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(64.dp)
                                    )
                                    Column(
                                        modifier = Modifier.padding(start = 16.dp)
                                    ) {
                                        Text(
                                            text = circleOffers[id].discount,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            text = circleOffers[id].expirationDate,
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
                SwipeViewWithTabs(titles = listOf("Special offers", "Just for you", "Saved"))
            }
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

data class CircleOffers(
    val itemName: String = "",
    val image: String = "",
    val discount: String = "",
    val expirationDate: String = "",
    val favourite: Boolean = false
)

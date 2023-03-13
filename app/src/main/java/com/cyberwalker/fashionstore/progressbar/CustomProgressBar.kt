package com.cyberwalker.fashionstore.progressbar

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cyberwalker.fashionstore.ui.theme.Purple200
import com.cyberwalker.fashionstore.ui.theme.Purple700

@Composable
fun CustomProgressBar() {

    val context = LocalContext.current
    var progressCount: Int by remember { mutableStateOf(0) }
    var progress by remember { mutableStateOf(0f) }

    when (progressCount) {
        0 -> progress = 0.0f
        1 -> progress = 0.1f
        2 -> progress = 0.2f
        3 -> progress = 0.3f
        4 -> progress = 0.4f
        5 -> progress = 0.5f
        6 -> progress = 0.6f
        7 -> progress = 0.7f
        8 -> progress = 0.8f
        9 -> progress = 0.9f
        10 -> progress = 1.0f
    }


    val size by animateFloatAsState(
        targetValue = progress,
        tween(
            durationMillis = 1000,
            delayMillis = 200,
            easing = LinearOutSlowInEasing
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, start = 30.dp, end = 30.dp)
    ) {
        // Progress bar text
        Row(
            modifier = Modifier
                .widthIn(min = 30.dp)
                .fillMaxWidth(size),
            horizontalArrangement = Arrangement.End
        )
        {
            Text(text = "${progress.toInt() * 100}%")
        }
        // Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(17.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(9.dp))
                    .background(
                        Purple200
                    )
            )
            // For the progress
            Box(
                modifier = Modifier
                    .fillMaxWidth(size)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(9.dp))
                    .background(Purple700)
                    .animateContentSize()
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Decrease button
            OutlinedButton(onClick = {
                if (progressCount > 0) {
                    progressCount -= 2
                } else {
                    Toast.makeText(context, "You cannot decrease more", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Decrease")
            }
            // Increase button
            Button(onClick = {
                if (progressCount < 10) {
                    progressCount += 2
                } else {
                    Toast.makeText(context, "You cannot increase more", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Increase")
            }

        }
    }


//    LaunchedEffect(key1 = true) {
//        progress = 0.7f
//    }
}
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
package com.cyberwalker.fashionstore

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cyberwalker.fashionstore.login.LoginViewModel
import com.cyberwalker.fashionstore.navigation.FashionNavGraph
import com.cyberwalker.fashionstore.navigation.FashionViewModel
import com.cyberwalker.fashionstore.navigation.Screen
import com.cyberwalker.fashionstore.ui.theme.FashionStoreTheme
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val viewModel: LoginViewModel by viewModels()
    private val fashionViewModel: FashionViewModel by viewModels()
    private val firebaseAnalytics = Firebase.analytics



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM){
            this.bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id")
            this.bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name")
            this.bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        }
        Log.d(TAG, "onCreate: ${intent.getStringExtra("custom_data_key")}")
        setContent {
            FashionStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App()
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        viewModel.getAuth().currentUser?.reload()?.addOnCompleteListener {task ->
            if (task.isSuccessful) {
                // Printing all extras
                val bundle = intent.extras
                if (bundle != null) {
                    for (key in bundle.keySet()) {
                        Log.d(TAG, "onStart: $key : ${if (bundle[key] != null) bundle[key] else "NULL"}")
                    }
                }
                fashionViewModel.updateStarRoute(Screen.Home.route)

                Toast.makeText(this, "Reload successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to reload user.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun App() {
    FashionNavGraph()
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2,heightDp = 400, widthDp = 300)
@Composable
fun DefaultPreview() {
    App()
}
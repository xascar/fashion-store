package com.cyberwalker.fashionstore.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FashionViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(FashionUiState(Screen.Splash.route,""))
    val uiState: StateFlow<FashionUiState> = _uiState.asStateFlow()

    private var notificationStartDestination = ""

    fun updateStarRoute(startDestination: String){
        _uiState.update { currentState ->
            currentState.copy(startDestination = notificationStartDestination.ifEmpty { startDestination })
        }
        updateNotificationStart("")
    }

    fun updateNotificationStart(startDestination: String){
        notificationStartDestination = startDestination
    }

}

data class FashionUiState(
    val startDestination: String,
    val notificationStartDestination: String
)
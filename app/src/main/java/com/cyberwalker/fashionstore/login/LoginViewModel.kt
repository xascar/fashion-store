package com.cyberwalker.fashionstore.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "LoginViewModel"
@HiltViewModel
class LoginViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

//    var uiState by mutableStateOf(LoginUiState())
//        private set

    val auth: FirebaseAuth = Firebase.auth


    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun createAccount(user: String, password: String) {
        updateUserPassword(user, password, Login.SIGNUP)
    }

    fun signIn(email: String, password: String) {
        updateUserPassword(email, password, Login.LOGIN)
    }

    fun gitHubLogin() {
        updateUserPassword("", "", Login.GITHUB)
    }
    private fun updateUserPassword(user: String, password: String, login: Login) {
        _uiState.update { currentState ->
            currentState.copy(userId = user, password = password, login = login)
        }
    }


    fun logout() {
        auth.signOut()
    }



}

data class LoginUiState(
    val firebaseUser: FirebaseUser? = null,
    val userId: String = "",
    val password: String = "",
    val login: Login = Login.LOGIN
)

enum class Login{
    LOGIN, SIGNUP, GITHUB
}
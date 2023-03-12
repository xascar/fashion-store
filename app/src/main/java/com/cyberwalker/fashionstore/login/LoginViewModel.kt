package com.cyberwalker.fashionstore.login

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
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

    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore



    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun getAuth(): FirebaseAuth {
        return auth
    }

    fun createAccount(inputEmail: String, inputPassword: String, activity: Activity) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        val email = inputEmail.trim()
        val password = inputPassword.trim()
        val errorMessage = isInputValid(email, password)
        if (errorMessage.isNotEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(errorMessage = errorMessage, isLoading = false)
            }
        }
        else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success. ${auth.currentUser}")
                        registerUser(LoginScreenActions.SIGN_UP,email,password, null)
                        _uiState.update { currentState ->
                            currentState.copy(isLoading = false, logged = true, errorMessage = "")
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        _uiState.update { currentState ->
                            currentState.copy(errorMessage = task.exception?.message.toString(), isLoading = false)
                        }
                    }
                }
        }
    }

    fun login(inputEmail: String, inputPassword: String, activity: Activity) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        val email = inputEmail.trim()
        val password = inputPassword.trim()
        val errorMessage = isInputValid(email, password)
        if (errorMessage.isNotEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(errorMessage = errorMessage, isLoading = false)
            }
        }
        else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        _uiState.update { currentState ->
                            currentState.copy(isLoading = false, logged = true, errorMessage = "")
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        _uiState.update { currentState ->
                            currentState.copy(errorMessage = task.exception?.message.toString(), isLoading = false)
                        }
                    }
                }
        }
    }



    private fun isInputValid(userId: String, password: String) = if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(password)) {
        "Fill the required info"
    } else {
        ""
    }

    fun gitHubLogin(activity: Activity) {
        val provider = OAuthProvider.newBuilder("github.com")

        val pendingResultTask = auth.pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener {
                    // User is signed in.
                    // IdP data available in
                    // authResult.getAdditionalUserInfo().getProfile().
                    // The OAuth access token can also be retrieved:
                    // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                    // The OAuth secret can be retrieved by calling:
                    // ((OAuthCredential)authResult.getCredential()).getSecret().
                    registerUser(LoginScreenActions.GITHUB,null,null, it)
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = false, logged = true, errorMessage = "")
                    }
                }
                .addOnFailureListener {
                    // Handle failure.

                    Log.w(TAG, "signInWithEmail:failure")
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = it.message.toString(), isLoading = false)
                    }
                }
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
            auth
                .startActivityForSignInWithProvider( /* activity = */activity, provider.build())
                .addOnSuccessListener {
                    // User is signed in.
                    // IdP data available in
                    // authResult.getAdditionalUserInfo().getProfile().
                    // The OAuth access token can also be retrieved:
                    // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                    // The OAuth secret can be retrieved by calling:
                    // ((OAuthCredential)authResult.getCredential()).getSecret().
                    registerUser(LoginScreenActions.GITHUB,null,null, it)
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = false, logged = true, errorMessage = "")
                    }

                }
                .addOnFailureListener {
                    // Handle failure.
                    Log.w(TAG, "signInWithEmail:failure")
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = it.message.toString(), isLoading = false)
                    }
                }
        }
    }


    fun logout() {
        auth.signOut()
    }

    private fun registerUser(
        action: LoginScreenActions,
        email: String?,
        password: String?,
        authResult: AuthResult?,

        ) {

        // Reading data
        db.collection("users")
            .whereEqualTo("userId", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty){
                    Log.d(TAG, "Creating a new user. ${authResult?.additionalUserInfo?.profile}")
                    val user = when(action) {
                        LoginScreenActions.SIGN_UP ->
                            hashMapOf(
                                "email" to email,
                                "password" to password
                            )
                        LoginScreenActions.GITHUB -> mutableMapOf(
                            "email" to (authResult?.additionalUserInfo?.profile?.get("email") ?: ""),
                            "name" to (authResult?.additionalUserInfo?.profile?.get("name") ?: ""),
                            "photoUrl" to (authResult?.additionalUserInfo?.profile?.get("avatar_url") ?: "")
                        )
                        else -> mutableMapOf() //Login
                    }

                    user.plus(Pair("userId",auth.currentUser?.uid))

                    // Add a new document with a generated ID
                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }
                else{
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Creating a new user with email and password.", exception)
            }


    }



}


data class LoginUiState(
    val isLoading: Boolean = false,
    val logged: Boolean = false,
    val errorMessage: String = "",
)
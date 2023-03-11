package com.cyberwalker.fashionstore.login

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.cyberwalker.fashionstore.circle.CircleOffers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
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

    private val auth: FirebaseAuth = Firebase.auth


    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _dataSet = MutableLiveData<MutableList<CircleOffers>>()

    val dataSet: LiveData<MutableList<CircleOffers>>
        get() = _dataSet

    init {
        _dataSet.value = mutableListOf( CircleOffers("Select skin care", "https://target.scene7.com/is/image/Target/prod_offer_386524_d5d450d7-f6a7-3688-94f7-12a34366945f?wid=320&hei=320", "20% off", "Expires Mar 21, 2023", true),
            CircleOffers("Hair car", "https://target.scene7.com/is/image/Target/prod_offer_386526_000fb4a2-9228-344a-b0a0-d0461b194110?wid=320&hei=320", "15% off", "Expires Mar 21, 2023", false),
            CircleOffers("Bedding", "https://target.scene7.com/is/image/Target/prod_offer_386552_d7f26f1d-60f6-399b-8eef-b8d974c16fac?wid=320&hei=320", "17% off", "Expires Mar 21, 2023", true),
            CircleOffers("Sun care", "https://target.scene7.com/is/image/Target/prod_offer_386525_86f42818-aa4f-3dfc-9bf2-19d223d05a87?wid=320&hei=320", "$12.99", "Expires Mar 21, 2023",false),
            CircleOffers("Outdoor furniture", "https://target.scene7.com/is/image/Target/prod_offer_386545_ed252eb2-f7ca-35e7-bea4-88ec607a6c57?wid=320&hei=320", "$11.99", "Expires Mar 21, 2023",true))
    }

    fun getAuth(): FirebaseAuth {
        return auth
    }


    fun updateDataAt(item: CircleOffers) {
        _dataSet.value?.removeIf {
            it.itemName == item.itemName
        }
        _dataSet.value?.add(item.copy(favourite = !item.favourite))
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



}


data class LoginUiState(
    val isLoading: Boolean = false,
    val logged: Boolean = false,
    val errorMessage: String = "",
)
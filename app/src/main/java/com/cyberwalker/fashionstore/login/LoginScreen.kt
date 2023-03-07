package com.cyberwalker.fashionstore.login

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyberwalker.fashionstore.R
import com.google.firebase.auth.OAuthProvider

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onAction: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    ) {

    val uiState by viewModel.uiState.collectAsState()
    val mContext = LocalContext.current
    var isError by rememberSaveable { mutableStateOf(false) }

    when(uiState.login.name){
        Login.LOGIN.name -> {
            if (!isInputValid(uiState.userId.trim(), uiState.password.trim())) {
                isError = true
            }
            else {
                viewModel.auth.signInWithEmailAndPassword(uiState.userId.trim(), uiState.password.trim())
                    .addOnCompleteListener(LocalContext.current as Activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            //val user = viewModel.auth.currentUser
                            onAction
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(mContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
//                        updateUI(null)
//                        checkForMultiFactorFailure(task.exception!!)
                        }

//                    if (!task.isSuccessful) {
//                        binding.status.setText(R.string.auth_failed)
//                    }
//                    hideProgressBar()
                    }
            }
        }
        Login.SIGNUP.name -> {
            if (!isInputValid(uiState.userId.trim(), uiState.password.trim())) {
                isError = true
            }
            else {
                viewModel.auth.createUserWithEmailAndPassword(uiState.userId.trim(), uiState.password.trim())
                    .addOnCompleteListener(LocalContext.current as Activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success. ${viewModel.auth.currentUser}")
//                        val user = auth.currentUser
//                        updateUI(user)
                            onAction
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(mContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }

                        //hideProgressBar()
                    }
            }

        }
        Login.GITHUB.name -> {
            val provider = OAuthProvider.newBuilder("github.com")

            val pendingResultTask = viewModel.auth.pendingAuthResult
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
                        onAction

                    }
                    .addOnFailureListener {
                        // Handle failure.
                        Toast.makeText(mContext, it.message,
                            Toast.LENGTH_LONG).show()
                    }
            } else {
                // There's no pending result so you need to start the sign-in flow.
                // See below.
                viewModel.auth
                    .startActivityForSignInWithProvider( /* activity = */LocalContext.current as Activity, provider.build())
                    .addOnSuccessListener {
                        // User is signed in.
                        // IdP data available in
                        // authResult.getAdditionalUserInfo().getProfile().
                        // The OAuth access token can also be retrieved:
                        // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                        // The OAuth secret can be retrieved by calling:
                        // ((OAuthCredential)authResult.getCredential()).getSecret().
                        onAction

                    }
                    .addOnFailureListener {
                        // Handle failure.
                        Toast.makeText(mContext, it.message,
                            Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    uiState.firebaseUser?.let {
        onAction
    }?:
        Scaffold(
            scaffoldState = scaffoldState
        ) { innerPadding ->
            LoginScreenContent(
                modifier = Modifier.padding(innerPadding),
                isError = isError,
                login = { user, password ->
                    viewModel.signIn(user, password)
                },
                signup =  { user, password ->
                    viewModel.createAccount(user, password)
                },
                github = {
                    viewModel.gitHubLogin()
                }
            )
        }

}

fun isInputValid(userId: String, password: String): Boolean {
    var valid = true

    if (TextUtils.isEmpty(userId)) {
        //binding.fieldEmail.error = "Required."
        valid = false
    } else {
        //binding.fieldEmail.error = null
    }

    if (TextUtils.isEmpty(password)) {
        //binding.fieldPassword.error = "Required."
        valid = false
    } else {
        //binding.fieldPassword.error = null
    }

    return valid
}

@Composable
fun LoginScreenContent(modifier: Modifier,
                       login: (user: String, password: String) -> Unit,
                       signup: (user: String, password: String) -> Unit,
                       github: () -> Unit,
                       isError: Boolean = false) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var user by remember{mutableStateOf("")}
        var password by remember{mutableStateOf("")}
        val focusManager = LocalFocusManager.current
        var passwordVisibility by remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }


        val icon = if (passwordVisibility)
            painterResource(id = R.drawable.baseline_block_24)
        else
            painterResource(id = R.drawable.baseline_remove_red_eye_24)

        Image(
            painter = painterResource(
                id = R.mipmap.ic_launcher
            ),
            contentDescription = "Logo Image",
            modifier = Modifier.size(128.dp)
        )
        Spacer(modifier = modifier.padding(vertical = 8.dp))
        OutlinedTextField(
            value = user,
            onValueChange = {user = it},
            label = { Text(text = "e-mail") },
            singleLine = true,
            maxLines = 1,
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusRequester.requestFocus()
                }
            ),
            modifier = Modifier.focusRequester(focusRequester),

            )
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text(text = "Password") },
            singleLine = true,
            maxLines = 1,
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusRequester.requestFocus()
                }
            ),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        painter = icon,
                        contentDescription = "Visibility Icon"
                    )
                }
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation(),
                    modifier = Modifier.focusRequester(focusRequester),

            )
        Row {
            Button(
                onClick = { login.invoke(user, password) },
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF495E57)
                )
            ) {
                Text(
                    text = "Login",
                    color = Color(0xFFEDEFEE)
                )
            }
            Spacer(modifier = modifier.padding(horizontal = 8.dp))
            Button(
                onClick = { signup.invoke(user, password) },
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF495E57)
                )
            ) {
                Text(
                    text = "Sign up",
                    color = Color(0xFFEDEFEE)
                )
            }
        }
        Button(
            onClick = { github.invoke() },
            colors = ButtonDefaults.buttonColors(
                Color(0xFF495E57)
            )
        ) {
            Text(
                text = "Git Hub",
                color = Color(0xFFEDEFEE)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        modifier = Modifier,
        login = { _,_->},
        signup = {_,_->},
        github = {}
    )
}
package com.cyberwalker.fashionstore.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyberwalker.fashionstore.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onAction: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    ) {

    val uiState by viewModel.uiState.collectAsState()
    val activity = LocalContext.current as Activity

    if (uiState.logged){
        onAction.invoke()
    }else{
        if (uiState.errorMessage.isNotEmpty()){
            Toast.makeText(activity, uiState.errorMessage, Toast.LENGTH_LONG).show()
        }
        Scaffold(
            scaffoldState = scaffoldState
        ) { innerPadding ->
            LoginScreenContent(
                modifier = Modifier.padding(innerPadding),
                errorMessage = uiState.errorMessage,
                onAction = { email, password, action ->
                    when (action){
                        LoginScreenActions.LOGIN -> viewModel.login(email, password, activity)
                        LoginScreenActions.SIGN_UP -> viewModel.createAccount(email, password, activity)
                        LoginScreenActions.GITHUB  -> viewModel.gitHubLogin(activity)
                    }
                },
            )
        }
    }

}

@Composable
fun LoginScreenContent(modifier: Modifier,
                       onAction: (email: String, password: String, action: LoginScreenActions) -> Unit,
                       errorMessage: String = "") {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var user by remember{mutableStateOf("")}
        var password by remember{mutableStateOf("")}
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
            isError = errorMessage.isNotEmpty(),
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
            isError = errorMessage.isNotEmpty(),
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
                onClick = { onAction.invoke(user, password, LoginScreenActions.LOGIN) },
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
                onClick = { onAction.invoke(user, password, LoginScreenActions.SIGN_UP) },
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
            onClick = { onAction.invoke("","", LoginScreenActions.GITHUB) },
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
        onAction = { _,_,_ ->}
    )
}

enum class LoginScreenActions{
    LOGIN, SIGN_UP, GITHUB
}
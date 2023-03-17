package com.codemave.mobilecomputing.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    context: Context,
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        Icon(
            painter = rememberVectorPainter(Icons.Filled.Person),
            contentDescription = "login_image",
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.9F),
            value = username.value,
            onValueChange = { text -> username.value = text },
            label = { Text(text = "Username") },
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.9F),
            value = password.value,
            onValueChange = { passwordString -> password.value = passwordString },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))

        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(0.9F)
                .height(50.dp),
            onClick = { checkCredentials(username.value, password.value, context, navController) },
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        ) {
            Text(text = "Login")
        }
    }
}

fun checkCredentials(
    username: String,
    password: String,
    context: Context,
    navController: NavController
) {
    val credentials = SharedPreferences(context)

    if (username == credentials.username && password == credentials.password || username == "" && password == "") {
        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
        navController.navigate("home")
    } else {
        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
    }
}
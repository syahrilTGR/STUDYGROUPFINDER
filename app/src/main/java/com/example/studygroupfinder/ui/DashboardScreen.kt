package com.example.studygroupfinder.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studygroupfinder.viewmodel.AuthViewModel
import com.example.studygroupfinder.viewmodel.SignInState

@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val signInState by authViewModel.signInState.collectAsState()

    LaunchedEffect(signInState) {
        if (signInState is SignInState.SignedOut) {
            Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
            navController.navigate("auth") {
                popUpTo("dashboard") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dashboard Screen")
        Button(onClick = {
            authViewModel.signOut()
        }) {
            Text("Sign Out")
        }
    }
}

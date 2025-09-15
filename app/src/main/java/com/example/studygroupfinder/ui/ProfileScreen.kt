package com.example.studygroupfinder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studygroupfinder.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (userProfile != null) {
            Text(text = "Name: ${userProfile?.name}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Campus: ${userProfile?.campus}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Major: ${userProfile?.major}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("profile_form") }) {
                Text("Edit Profile")
            }
        } else {
            Text(text = "No profile found. Please create one.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("profile_form") }) {
                Text("Create Profile")
            }
        }
    }
}

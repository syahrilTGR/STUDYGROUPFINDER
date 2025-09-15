package com.example.studygroupfinder.ui

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studygroupfinder.ui.navigation.Screen
import com.example.studygroupfinder.viewmodel.ProfileFormViewModel
import com.example.studygroupfinder.viewmodel.SaveProfileState

@Composable
fun ProfileFormScreen(
    navController: NavController,
    profileFormViewModel: ProfileFormViewModel = viewModel()
) {
    val context = LocalContext.current
    val name by profileFormViewModel.name.collectAsState()
    val campus by profileFormViewModel.campus.collectAsState()
    val major by profileFormViewModel.major.collectAsState()
    val saveProfileState by profileFormViewModel.saveProfileState.collectAsState()
    val isEditing by profileFormViewModel.isEditing.collectAsState()

    LaunchedEffect(saveProfileState) {
        when (val state = saveProfileState) {
            is SaveProfileState.Success -> {
                Toast.makeText(context, "Profile saved successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Profile.route) {
                    popUpTo(Screen.ProfileForm.route) { inclusive = true }
                }
            }
            is SaveProfileState.Error -> {
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (isEditing) "Edit Your Profile" else "Create Your Profile")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { profileFormViewModel.onNameChange(it) },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = campus,
            onValueChange = { profileFormViewModel.onCampusChange(it) },
            label = { Text("Campus") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = major,
            onValueChange = { profileFormViewModel.onMajorChange(it) },
            label = { Text("Major") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { profileFormViewModel.saveProfile() }) {
            Text("Save Profile")
        }

        if (saveProfileState == SaveProfileState.Loading) {
            CircularProgressIndicator()
        }
    }
}

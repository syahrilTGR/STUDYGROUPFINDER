package com.example.studygroupfinder.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun UserProfileForm(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var campus by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create Your Profile")
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        TextField(
            value = campus,
            onValueChange = { campus = it },
            label = { Text("Campus") }
        )
        TextField(
            value = major,
            onValueChange = { major = it },
            label = { Text("Major") }
        )
        Button(onClick = { /* TODO: Handle profile submission */ }) {
            Text("Save Profile")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileFormPreview() {
    UserProfileForm()
}

package com.example.studygroupfinder.ui

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studygroupfinder.R
import com.example.studygroupfinder.viewmodel.AuthViewModel
import com.example.studygroupfinder.viewmodel.SignInState
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val signInState by authViewModel.signInState.collectAsState()
    val scope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) } // true for login, false for signup

    LaunchedEffect(signInState) {
        when (val state = signInState) {
            is SignInState.SignedIn -> {
                Toast.makeText(context, "Signed in successfully", Toast.LENGTH_SHORT).show()
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    if (authViewModel.checkUserProfileExists(currentUser.uid)) {
                        navController.navigate("profile") {
                            popUpTo("auth") { inclusive = true }
                        }
                    } else {
                        navController.navigate("profile_form") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                } else {
                    // Should not happen if state is SignedIn, but handle defensively
                    Toast.makeText(context, "User not found after sign in", Toast.LENGTH_LONG).show()
                    authViewModel.signOut() // Force sign out if user is null unexpectedly
                }
            }
            is SignInState.Error -> {
                Toast.makeText(context, "Sign in failed: ${state.message}", Toast.LENGTH_LONG).show()
            }
            is SignInState.SignedOut -> {
                Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
                // No navigation needed here, AuthScreen is the destination for signed out users
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
        if (signInState == SignInState.Loading) {
            CircularProgressIndicator()
        } else if (signInState == SignInState.SignedIn) {
            Button(onClick = {
                authViewModel.signOut()
            }) {
                Text("Sign out")
            }
        } else {
            // Email/Password fields
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLogin) {
                Button(onClick = {
                    authViewModel.signInWithEmail(email, password)
                }) {
                    Text("Sign In with Email")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Don't have an account? ")
                Button(onClick = { isLogin = false }) {
                    Text("Sign Up")
                }
            } else {
                Button(onClick = {
                    authViewModel.signUpWithEmail(email, password)
                }) {
                    Text("Sign Up with Email")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Already have an account? ")
                Button(onClick = { isLogin = true }) {
                    Text("Sign In")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("OR")
            Spacer(modifier = Modifier.height(16.dp))

            // Google Sign-In Button
            Button(onClick = {
                scope.launch {
                    try {
                        val credentialManager = CredentialManager.create(context)
                        val googleIdOption = GetGoogleIdOption.Builder()
                            .setFilterByAuthorizedAccounts(false)
                            .setServerClientId(context.getString(R.string.default_web_client_id))
                            .build()

                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()

                        val result = credentialManager.getCredential(context, request)
                        val credential = result.credential

                        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            try {
                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                authViewModel.signInWithGoogle(googleIdTokenCredential.idToken)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to parse Google ID Token: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context, "Unexpected credential type: ${credential.type}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: GetCredentialException) {
                        Toast.makeText(context, "Sign in failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }) {
                Text("Sign in with Google")
            }
        }
    }
}

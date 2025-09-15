package com.example.studygroupfinder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygroupfinder.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository()

    private val _signInState = MutableStateFlow<SignInState>(SignInState.SignedOut)
    val signInState = _signInState.asStateFlow()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _signInState.value = SignInState.Loading
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential).await()
                _signInState.value = SignInState.SignedIn
            } catch (e: Exception) {
                _signInState.value = SignInState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _signInState.value = SignInState.Loading
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _signInState.value = SignInState.SignedIn
            } catch (e: Exception) {
                _signInState.value = SignInState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _signInState.value = SignInState.Loading
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _signInState.value = SignInState.SignedIn
            } catch (e: Exception) {
                _signInState.value = SignInState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _signInState.value = SignInState.Loading
            try {
                auth.signOut()
                _signInState.value = SignInState.SignedOut
            } catch (e: Exception) {
                _signInState.value = SignInState.Error(e.message ?: "An unknown error occurred during sign out")
            }
        }
    }

    suspend fun checkUserProfileExists(uid: String): Boolean {
        return userRepository.getUser(uid) != null
    }
}

sealed class SignInState {
    object SignedOut : SignInState()
    object Loading : SignInState()
    object SignedIn : SignInState()
    data class Error(val message: String) : SignInState()
}

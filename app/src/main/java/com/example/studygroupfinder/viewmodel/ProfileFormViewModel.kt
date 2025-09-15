package com.example.studygroupfinder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygroupfinder.data.UserRepository
import com.example.studygroupfinder.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileFormViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _campus = MutableStateFlow("")
    val campus = _campus.asStateFlow()

    private val _major = MutableStateFlow("")
    val major = _major.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing = _isEditing.asStateFlow()

    private val _saveProfileState = MutableStateFlow<SaveProfileState>(SaveProfileState.Idle)
    val saveProfileState = _saveProfileState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val user = userRepository.getUser(currentUser.uid)
                if (user != null) {
                    _name.value = user.name
                    _campus.value = user.campus
                    _major.value = user.major
                    _isEditing.value = true
                } else {
                    _isEditing.value = false
                }
            }
        }
    }

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun onCampusChange(newCampus: String) {
        _campus.value = newCampus
    }

    fun onMajorChange(newMajor: String) {
        _major.value = newMajor
    }

    fun saveProfile() {
        viewModelScope.launch {
            _saveProfileState.value = SaveProfileState.Loading
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val user = User(
                    uid = currentUser.uid,
                    name = _name.value,
                    campus = _campus.value,
                    major = _major.value
                )
                try {
                    userRepository.saveUser(user)
                    _saveProfileState.value = SaveProfileState.Success
                } catch (e: Exception) {
                    _saveProfileState.value = SaveProfileState.Error(e.message ?: "Failed to save profile")
                }
            } else {
                _saveProfileState.value = SaveProfileState.Error("User not logged in")
            }
        }
    }
}

sealed class SaveProfileState {
    object Idle : SaveProfileState()
    object Loading : SaveProfileState()
    object Success : SaveProfileState()
    data class Error(val message: String) : SaveProfileState()
}

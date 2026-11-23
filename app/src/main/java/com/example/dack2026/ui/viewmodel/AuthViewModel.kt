package com.example.dack2026.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dack2026.data.database.AppDatabase
import com.example.dack2026.data.entity.UserEntity
import com.example.dack2026.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    val allUsers: LiveData<List<UserEntity>>

    private val _loginResult = MutableLiveData<UserEntity?>()
    val loginResult: LiveData<UserEntity?> get() = _loginResult

    private val _loginAttempted = MutableLiveData<Boolean>()
    val loginAttempted: LiveData<Boolean> get() = _loginAttempted

    private val _registerResult = MutableLiveData<Boolean?>()
    val registerResult: LiveData<Boolean?> get() = _registerResult

    private val _updatePasswordResult = MutableLiveData<Boolean?>()
    val updatePasswordResult: LiveData<Boolean?> get() = _updatePasswordResult

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        allUsers = repository.allUsers
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = repository.login(username, password)
            _loginResult.postValue(user)
            _loginAttempted.postValue(true)
        }
    }

    fun register(user: UserEntity) {
        viewModelScope.launch {
            val existing = repository.getUserByUsername(user.username)
            if (existing != null) {
                _registerResult.postValue(false)
            } else {
                val id = repository.registerUser(user)
                _registerResult.postValue(id > 0)
            }
        }
    }

    fun forgotPassword(username: String, newPassword: String) {
        viewModelScope.launch {
            val existing = repository.getUserByUsername(username)
            if (existing == null) {
                _updatePasswordResult.postValue(false)
            } else {
                val rows = repository.updatePassword(username, newPassword)
                _updatePasswordResult.postValue(rows > 0)
            }
        }
    }

    fun updateRole(userId: Long, newRole: String) {
        viewModelScope.launch {
            repository.updateRole(userId, newRole)
        }
    }

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }

    fun resetState() {
        _loginResult.value = null
        _loginAttempted.value = false
        _registerResult.value = null
        _updatePasswordResult.value = null
    }
}

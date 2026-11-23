package com.example.dack2026.data.repository

import androidx.lifecycle.LiveData
import com.example.dack2026.data.dao.UserDao
import com.example.dack2026.data.entity.UserEntity

class UserRepository(private val userDao: UserDao) {
    val allUsers: LiveData<List<UserEntity>> = userDao.getAllUsers()

    suspend fun registerUser(user: UserEntity): Long {
        return userDao.insertUser(user)
    }

    suspend fun updateUser(user: UserEntity): Int {
        return userDao.updateUser(user)
    }

    suspend fun deleteUser(user: UserEntity): Int {
        return userDao.deleteUser(user)
    }

    suspend fun login(username: String, password: String): UserEntity? {
        return userDao.login(username, password)
    }

    suspend fun getUserByUsername(username: String): UserEntity? {
        return userDao.getUserByUsername(username)
    }

    suspend fun updatePassword(username: String, newPassword: String): Int {
        return userDao.updatePassword(username, newPassword)
    }

    suspend fun updateRole(userId: Long, newRole: String): Int {
        return userDao.updateRole(userId, newRole)
    }
}

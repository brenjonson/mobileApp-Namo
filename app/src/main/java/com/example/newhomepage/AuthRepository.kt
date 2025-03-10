package com.example.newhomepage.repositories

import com.example.newhomepage.api.RetrofitClient
import com.example.newhomepage.api.models.LoginRequest
import com.example.newhomepage.api.models.RegisterRequest
import com.example.newhomepage.api.models.User

class AuthRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun registerUser(username: String, email: String, password: String): Result<Int> {
        return try {
            val request = RegisterRequest(username, email, password)
            val response = apiService.registerUser(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()?.data ?: -1)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<Pair<String, User>> {
        return try {
            val request = LoginRequest(email, password)
            val response = apiService.loginUser(request)

            if (response.isSuccessful && response.body() != null) {
                val token = response.body()!!.token
                val user = response.body()!!.user
                Result.success(Pair(token, user))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(token: String): Result<User> {
        return try {
            val authToken = "Bearer $token"
            val response = apiService.getUserProfile(authToken)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to get profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
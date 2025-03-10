package com.example.newhomepage.repositories

import com.example.newhomepage.api.RetrofitClient
import com.example.newhomepage.api.models.EventResponse

class EventRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getAllEvents(): Result<List<EventResponse>> {
        return try {
            val response = apiService.getAllEvents()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.events)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to get events"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEventById(eventId: Int): Result<EventResponse> {
        return try {
            val response = apiService.getEventById(eventId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to get event"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchEvents(
        title: String? = null,
        category: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<EventResponse>> {
        return try {
            val response = apiService.searchEvents(title, category, startDate, endDate)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.events)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Search failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
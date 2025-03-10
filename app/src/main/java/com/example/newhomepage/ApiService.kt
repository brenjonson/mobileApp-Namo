package com.example.newhomepage.api

import com.example.newhomepage.api.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth Endpoints
    @POST("api/auth/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<ApiResponse<Int>>

    @POST("api/auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/auth/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<ApiResponse<User>>

    // Event Endpoints
    @GET("api/events")
    suspend fun getAllEvents(): Response<EventListResponse>

    @GET("api/events/{id}")
    suspend fun getEventById(@Path("id") eventId: Int): Response<ApiResponse<EventResponse>>

    @GET("api/events/search")
    suspend fun searchEvents(
        @Query("title") title: String? = null,
        @Query("category") category: String? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null
    ): Response<EventListResponse>

    // Registration Endpoints
    @POST("api/registrations/events/{eventId}")
    suspend fun registerForEvent(
        @Path("eventId") eventId: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Int>>

    @GET("api/registrations/user")
    suspend fun getUserRegistrations(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Any>>>
}
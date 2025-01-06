package com.example.newsapp.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

val retrofit = Retrofit.Builder()
    .baseUrl("https://newsapi.org/v2/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

suspend fun <T> requestHandler(
    apiCall: suspend () -> Response<T>,
    onSuccess: suspend (T) -> Unit,
    onError: suspend (error: String) -> Unit,
    isLoading: (Boolean) -> Unit
) {
    try {
        isLoading(true)
        val response = apiCall()

        if (response.isSuccessful) {
            val body = response.body()
            body?.let { onSuccess(it) }
        } else {
            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
            onError(errorMessage)
        }
    } catch (e: Exception) {
        val errorMessage = e.message ?: "Unknown error"
        onError(errorMessage)
    } finally {
        isLoading(false)
    }
}
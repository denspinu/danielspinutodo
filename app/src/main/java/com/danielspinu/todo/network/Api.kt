package com.danielspinu.todo.network

import com.danielspinu.todo.tasklist.TasksWebService
import com.danielspinu.todo.user.UserWebService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object Api {
    private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo3MTMsImV4cCI6MTY4MjUxMzIwMX0.Att5v96weBXQYNDil0L05LMTLSl-ym90MxWNB7q2mHs"

    val userWebService : UserWebService by lazy {
        retrofit.create(UserWebService::class.java)
    }

    val tasksWebService : TasksWebService by lazy {
        retrofit.create(TasksWebService::class.java)
    }

    private val retrofit by lazy {
        // client HTTP
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor { chain ->
                // intercepteur qui ajoute le `header` d'authentification avec votre token:
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        // transforme le JSON en objets kotlin et inversement
        val jsonSerializer = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        // instance retrofit pour implémenter les webServices:
        Retrofit.Builder()
            .baseUrl("https://android-tasks-api.herokuapp.com/api/")
            .client(okHttpClient)
            .addConverterFactory(jsonSerializer.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}

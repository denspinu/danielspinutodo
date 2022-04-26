package com.danielspinu.todo.user

import com.danielspinu.todo.user.UserInfo
import retrofit2.Response
import retrofit2.http.GET

interface UserWebService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>
}
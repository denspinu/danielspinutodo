package com.danielspinu.todo.tasklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String = "valeur par défaut !"
) : java.io.Serializable


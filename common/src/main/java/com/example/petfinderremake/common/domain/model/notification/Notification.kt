package com.example.petfinderremake.common.domain.model.notification

data class Notification(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val createdAt: Long = 0L,
    val displayed: Boolean = false
)
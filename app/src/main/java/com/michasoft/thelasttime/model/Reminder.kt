package com.michasoft.thelasttime.model

data class Reminder(
    val event: Event,
    val triggerMillis: Long = 0
)
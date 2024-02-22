package com.roberthj.musicmasterkotlin.models

data class Event(
    var name: String,
    val type: String,
    val id: String,
    val url: String,
    val startDate: String,
    val notes: String,
    val venue: String,
    val address: String,
    val city: String,
    val country: String
) {
}
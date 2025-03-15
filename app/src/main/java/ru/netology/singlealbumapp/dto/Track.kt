package ru.netology.singlealbumapp.dto

data class Track(
    val id: Int,
    val file: String,
    var toPlay: Boolean = false,
    var long: String? = ""
)
package ru.netology.singlealbumapp.adapter

import ru.netology.singlealbumapp.dto.Track

interface OnInteractionListener {
    fun playing(track: Track) {}
}
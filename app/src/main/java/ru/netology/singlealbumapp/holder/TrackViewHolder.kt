package ru.netology.singlealbumapp.holder

import androidx.recyclerview.widget.RecyclerView
import ru.netology.singlealbumapp.R
import ru.netology.singlealbumapp.adapter.OnInteractionListener
import ru.netology.singlealbumapp.databinding.TrackBinding
import ru.netology.singlealbumapp.dto.Track


class TrackViewHolder(
    private val binding: TrackBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(track: Track) {
        binding.apply {
            trackName.text = track.file
            trackTime.text = track.long

            // Изменение значка воспросиведение-пауза для трека
            play.setImageResource(
                if (track.toPlay) {
                    R.drawable.pause_circle_24dp
                } else {
                    R.drawable.play_circle_24dp
                }
            )

            play.setOnClickListener {
                onInteractionListener.playing(track)
            }
        }
    }
}
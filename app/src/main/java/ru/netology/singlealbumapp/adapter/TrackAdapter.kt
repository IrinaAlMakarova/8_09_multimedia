package ru.netology.singlealbumapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.netology.singlealbumapp.databinding.TrackBinding
import ru.netology.singlealbumapp.dto.Track
import ru.netology.singlealbumapp.holder.TrackViewHolder

class TrackAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Track, TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }
}

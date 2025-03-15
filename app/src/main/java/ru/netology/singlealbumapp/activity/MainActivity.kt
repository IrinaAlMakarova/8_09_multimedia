package ru.netology.singlealbumapp.activity

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ru.netology.singlealbumapp.R
import ru.netology.singlealbumapp.adapter.OnInteractionListener
import ru.netology.singlealbumapp.adapter.TrackAdapter
import ru.netology.singlealbumapp.databinding.ActivityMainBinding
import ru.netology.singlealbumapp.dto.Track
import ru.netology.singlealbumapp.viewmodel.ViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TrackAdapter
    private lateinit var recyclerView: RecyclerView

    private val mediaPlayer = MediaPlayer()
    private val viewModel = ViewModel(mediaPlayer)

    private val url =
        "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"

    private val interactionListener = object : OnInteractionListener {
        override fun playing(track: Track) {
            viewModel.onPlayTrack(track)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.list
        adapter = TrackAdapter(interactionListener)
        recyclerView.adapter = adapter
        listener()
    }


    private fun listener() {
        binding.playAlbum.setOnClickListener {
            if (mediaPlayer.isPlaying) { //проверим, находится ли объект MediaPlayer в состоянии Started
                mediaPlayer.pause() //воспроизведение Приостановлено
            } else {
                mediaPlayer.start() //начать воспроизведение
            }
        }
    }
}
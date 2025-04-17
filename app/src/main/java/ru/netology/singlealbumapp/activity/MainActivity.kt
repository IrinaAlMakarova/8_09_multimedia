package ru.netology.singlealbumapp.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.netology.singlealbumapp.R
import ru.netology.singlealbumapp.adapter.OnInteractionListener
import ru.netology.singlealbumapp.adapter.TrackAdapter
import ru.netology.singlealbumapp.databinding.ActivityMainBinding
import ru.netology.singlealbumapp.dto.Album
import ru.netology.singlealbumapp.dto.Track
import ru.netology.singlealbumapp.viewmodel.ViewModel
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TrackAdapter
    private lateinit var recyclerView: RecyclerView

    private val mediaPlayer = MediaPlayer()
    private val viewModel = ViewModel(mediaPlayer)
    private val url =
        "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/album.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.list

        adapter = TrackAdapter(onInteractionListener = object : OnInteractionListener {
            override fun playing(track: Track) {
                viewModel.onPlayTrack(track)
            }
        })

        recyclerView.adapter = adapter
        getAll()
        flowData()

        binding.playAlbum.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause() // Приостанавливает воспроизведение
            } else {
                mediaPlayer.start() // Возобновляет воспроизведение
            }
        }
    }


    private fun flowData() {
        // Изменение значка воспросиведение-пауза для всей музыки
        lifecycleScope.launch { // Запуск и выполнение указанного блока
            viewModel.toPlay.collect { //collect - терминальный оператор
                if (mediaPlayer.isPlaying) {
                    binding.playAlbum.setImageResource(R.drawable.pause_circle_24dp)
                } else {
                    binding.playAlbum.setImageResource(R.drawable.play_circle_24dp)
                }
            }
        }
        lifecycleScope.launch { // Запуск и выполнение указанного блока
            viewModel.dataAlbumTracks.collect { //collect - терминальный оператор
                adapter.submitList(it) // Установка нового списка для отображения
            }
        }
    }

    private fun getAll() { // Асинхронный метод
        val client = OkHttpClient()

        val request = Request.Builder() // Формируем запрос
            .url(url)
            .build()

        client.newCall(request)
            .enqueue(object :
                Callback { // Ставим запрос в очередь и передаем Callback на случ. успеха и не успеха
                override fun onResponse(call: Call, response: Response) { //Ответ был получен
                    try {
                        val album = Gson().fromJson(response.body?.string(), Album::class.java)
                        val tracks = album.tracks
                        viewModel.setTracks(tracks)
                        runOnUiThread { // Выполняет указанное действие в потоке пользовательского интерфейса (возврат в основной поток)
                            binding.apply {
                                albumName.text = album.title
                                artistName.text = album.artist
                                genreName.text = album.genre
                                yearName.text = album.published
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread { // Выполняет указанное действие в потоке пользовательского интерфейса (возврат в основной поток)
                            Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }


                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { // Выполняет указанное действие в потоке пользовательского интерфейса (возврат в основной поток)
                        Toast.makeText(
                            applicationContext,
                            "Failed to connect",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }


}
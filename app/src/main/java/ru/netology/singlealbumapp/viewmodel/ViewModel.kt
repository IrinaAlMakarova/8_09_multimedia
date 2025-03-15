package ru.netology.singlealbumapp.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import ru.netology.singlealbumapp.dto.Track

class ViewModel(
    private val mediaPlayer: MediaPlayer,
) : ViewModel() {

    companion object {
        const val URL_WAY =
            "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
    }

    private var _dataAlbumTracks: List<Track> = listOf()
    private var currentTrack: Track? = null


    fun onPlayTrack(track: Track) {
        // на случай если уже что-то было запущенно
        mediaPlayer.pause() //воспроизведение Приостановлено
        _dataAlbumTracks = _dataAlbumTracks.map {
            it.copy(toPlay = false)
        }
        if (currentTrack?.id != track.id) {
            mediaPlayer.reset() //Сбрасывает медиаплеер в его неинициализированное состояние
            val dataSource = URL_WAY + track.file
            mediaPlayer.setDataSource(dataSource)
            mediaPlayer.prepare() //подготавливает проигрыватель к воспроизведению

            mediaPlayer.start() //начать воспроизведение
            mediaPlayer.setOnCompletionListener {
                val trackInData = _dataAlbumTracks.find {
                    it.id == track.id
                }
                val trackIndex = _dataAlbumTracks.indexOf(trackInData)
                val lastIndex = _dataAlbumTracks.lastIndex
                if (lastIndex == trackIndex) {
                    onPlayTrack(_dataAlbumTracks[0])
                } else {
                    onPlayTrack(_dataAlbumTracks[trackIndex + 1])
                }
            }
        }


    }


}
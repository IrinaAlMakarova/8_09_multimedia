package ru.netology.singlealbumapp.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ru.netology.singlealbumapp.dto.Track

class ViewModel(
    private val mediaPlayer: MediaPlayer
) : ViewModel() {

    val toPlay
        get() = flow {
            while (true) {
                emit(mediaPlayer.isPlaying)
                delay(10)
            }
        }

    private var _dataAlbumTracks: List<Track> = listOf()
    val dataAlbumTracks
        get() = flow {
            while (true) {
                emit(_dataAlbumTracks)
                delay(10)
            }
        }

    private var currentTrack: Track? = null
    fun setTracks(tracks: List<Track>) {
        _dataAlbumTracks = tracks
    }

    fun onPlayTrack(track: Track) {
        // на случай если уже что-то было запущенно
        mediaPlayer.pause()
        _dataAlbumTracks = _dataAlbumTracks.map {
            it.copy(toPlay = false)
        }
        if (currentTrack?.id != track.id) {
            mediaPlayer.reset()
            val dataSource = URL_WAY + track.file
            mediaPlayer.setDataSource(dataSource)
            mediaPlayer.prepare()
            _dataAlbumTracks = _dataAlbumTracks.map {
                if (it.id == track.id) {
                    currentTrack = it
                    val minuteDur = mediaPlayer.duration / 60_000
                    val secondsDur = (mediaPlayer.duration / 1_000) - (minuteDur * 60)
                    it.copy(
                        toPlay = true,
                        long = "$minuteDur:${if (secondsDur < 10) "0" else ""}$secondsDur"
                    )
                } else {
                    it
                }
            }
            mediaPlayer.start()
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

    companion object {
        const val URL_WAY =
            "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
    }
}
package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public User createUser(String name, String mobile){
        User s = spotifyRepository.createUser(name,mobile);
        return s;
    }

    public Artist createArtist(String name) {
        Artist a = spotifyRepository.createArtist(name);
        return a;
    }

    public Album createAlbum(String title, String artistName) {
        Album a = spotifyRepository.createAlbum(title,artistName);
        return a;
    }

    public Song createSong(String title, String albumName, int length) throws Exception {
        Song s = spotifyRepository.createSong(title,albumName,length);
        return s;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        Playlist playlist = spotifyRepository.createPlaylistOnLength(mobile,title,length);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        return spotifyRepository.createPlaylistOnName(mobile,title,songTitles);
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        return spotifyRepository.findPlaylist(mobile,playlistTitle);
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        return spotifyRepository.likeSong(mobile,songTitle);
    }

    public String mostPopularArtist() {
        return spotifyRepository.mostPopularArtist();
    }

    public String mostPopularSong() {
        return spotifyRepository.mostPopularSong();
    }
}

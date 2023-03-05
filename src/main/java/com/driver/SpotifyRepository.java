package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User s = new User(name,mobile);
        users.add(s);
        return s;
    }

    public Artist createArtist(String name) {
        for(Artist art : artists){
            if(art.getName().equals(name))
                return art;
        }
        Artist a = new Artist(name);
        artists.add(a);
        return a;
    }

    public Album createAlbum(String title, String artistName) {
        Artist art = createArtist(artistName);
        for (Album album : albums){
            if(album.getTitle().equals(title))
                return album;
        }
        Album a = new Album(title);
        albums.add(a);
        List<Album> ab = new ArrayList<>();
        if(artistAlbumMap.containsKey(art))
            ab = artistAlbumMap.get(art);
        ab.add(a);
        artistAlbumMap.put(art,ab);
        return a;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean albumPresent = false;
        Album a = new Album();
        for(Album alb : albums){
            if(alb.getTitle().equals(albumName)){
                albumPresent = true;
                a = alb;
                break;
            }
        }
        if(!albumPresent)
            throw new Exception("Album does not exist");

        Song s = new Song(title,length);
        songs.add(s);

        List<Song> l = new ArrayList<>();
        if(albumSongMap.containsKey(a))
            l = albumSongMap.get(a);

        l.add(s);
        albumSongMap.put(a,l);
        return s;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return playlist;
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        List<Song> l = new ArrayList<>();
        for(Song s : songs){
            if(s.getLength()==length)
                l.add(s);
        }
        playlistSongMap.put(playlist,l);

        User curr = new User();
        boolean f = false;
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                curr = user;
                f = true;
                break;
            }
        }
        if(!f)
            throw new Exception("User does not exist");

        List<User> users1 = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist))
            users1 = playlistListenerMap.get(playlist);

        users1.add(curr);
        playlistListenerMap.put(playlist,users1);
        creatorPlaylistMap.put(curr,playlist);

        List<Playlist> userPlaylist = new ArrayList<>();
        if(userPlaylistMap.containsKey(curr))
            userPlaylist = userPlaylistMap.get(curr);

        userPlaylist.add(playlist);
        userPlaylistMap.put(curr,userPlaylist);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlist: playlists){
            if(playlist.getTitle().equals(title))
                return playlist;
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        List<Song> l = new ArrayList<>();
        for(Song song : songs){
            if(songTitles.contains(song.getTitle()))
                l.add(song);
        }
        playlistSongMap.put(playlist,l);

        User curr = new User();
        boolean flag = false;
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                curr = user;
                flag = true;
                break;
            }
        }
        if(!flag)
            throw new Exception("User does not exist");

        List<User> userList = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist))
            userList = playlistListenerMap.get(playlist);

        userList.add(curr);
        playlistListenerMap.put(playlist,userList);

        creatorPlaylistMap.put(curr,playlist);

        List<Playlist> userplaylist = new ArrayList<>();
        if(userPlaylistMap.containsKey(curr))
            userplaylist = userPlaylistMap.get(curr);

        userplaylist.add(playlist);
        userPlaylistMap.put(curr,userplaylist);

        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean flag =false;
        Playlist playlist = new Playlist();
        for(Playlist curplaylist: playlists){
            if(curplaylist.getTitle().equals(playlistTitle)){
                playlist=curplaylist;
                flag=true;
                break;
            }
        }
        if (!flag){
            throw new Exception("Playlist does not exist");
        }

        User curUser= new User();
        boolean flag2= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag2= true;
                break;
            }
        }
        if (!flag2){
            throw new Exception("User does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        if(!userslist.contains(curUser))
            userslist.add(curUser);
        playlistListenerMap.put(playlist,userslist);

        if(creatorPlaylistMap.get(curUser)!=playlist)
            creatorPlaylistMap.put(curUser,playlist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }
        if(!userplaylists.contains(playlist))userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);


        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User curUser= new User();
        boolean flag2= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag2= true;
                break;
            }
        }
        if (!flag2){
            throw new Exception("User does not exist");
        }

        Song song = new Song();
        boolean flag = false;
        for(Song cursong : songs){
            if(cursong.getTitle().equals(songTitle)){
                song=cursong;
                flag=true;
                break;
            }
        }
        if (!flag){
            throw new Exception("Song does not exist");
        }

        List<User> users = new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            users=songLikeMap.get(song);
        }
        if (!users.contains(curUser)){
            users.add(curUser);
            songLikeMap.put(song,users);
            song.setLikes(song.getLikes()+1);

            Album album = new Album();
            for(Album curAlbum : albumSongMap.keySet()){
                List<Song> temp = albumSongMap.get(curAlbum);
                if(temp.contains(song)){
                    album=curAlbum;
                    break;
                }
            }

            Artist artist = new Artist();
            for(Artist curArtist : artistAlbumMap.keySet()){
                List<Album> temp = artistAlbumMap.get(curArtist);
                if(temp.contains(album)){
                    artist=curArtist;
                    break;
                }
            }

            artist.setLikes(artist.getLikes()+1);
        }
        return song;
    }

    public String mostPopularArtist() {
        String name="";
        int maxLikes = Integer.MIN_VALUE;
        for(Artist art : artists){
            maxLikes= Math.max(maxLikes,art.getLikes());
        }
        for(Artist art : artists){
            if(maxLikes==art.getLikes()){
                name=art.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        String name="";
        int maxLikes = Integer.MIN_VALUE;
        for(Song song : songs){
            maxLikes=Math.max(maxLikes,song.getLikes());
        }
        for(Song song : songs){
            if(maxLikes==song.getLikes())
                name=song.getTitle();
        }
        return name;
    }
}

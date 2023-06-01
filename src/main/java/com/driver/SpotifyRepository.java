package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap = new HashMap<>();;
    public HashMap<Album, List<Song>> albumSongMap = new HashMap<>();;
    public HashMap<Playlist, List<Song>> playlistSongMap = new HashMap<>();;
    public HashMap<Playlist, List<User>> playlistListenerMap = new HashMap<>();;
    public HashMap<User, Playlist> creatorPlaylistMap = new HashMap<>();;
    public HashMap<User, List<Playlist>> userPlaylistMap = new HashMap<>();;
    public HashMap<Song, List<User>> songLikeMap = new HashMap<>();;

    public HashMap<String,List<String>> userLikedSongsMap = new HashMap<>();

    public List<User> users = new ArrayList<>();;
    public List<Song> songs = new ArrayList<>();;
    public List<Playlist> playlists = new ArrayList<>();;
    public List<Album> albums = new ArrayList<>();;
    public List<Artist> artists = new ArrayList<>();;



    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
//        artistAlbumMap = new HashMap<>();
//        albumSongMap = new HashMap<>();
//        playlistSongMap = new HashMap<>();
//        playlistListenerMap = new HashMap<>();
//        creatorPlaylistMap = new HashMap<>();
//        userPlaylistMap = new HashMap<>();
//        songLikeMap = new HashMap<>();
//
//        users = new ArrayList<>();
//        songs = new ArrayList<>();
//        playlists = new ArrayList<>();
//        albums = new ArrayList<>();
//        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
       Album album = new Album(title);
       albums.add(album);
       Artist artist = null;
       for(Artist art : artists){
           if(art.getName().equals(artistName)){
               artist = art;
           }
       }
       if(artist != null) {
           if(!artistAlbumMap.containsKey(artist)){
               artistAlbumMap.put(artist,new ArrayList<>());
           }

           artistAlbumMap.get(artist).add(album);

           if(!albumSongMap.containsKey(album)){
               albumSongMap.put(album,new ArrayList<>());
           }
       }
       return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song = new Song(title,length);
        songs.add(song);
        Album album = null;
        for(Album album1 : albums){
            if(album1.getTitle().equals(albumName)){
                album = album1;
            }
        }
        if(album != null) {
            albumSongMap.get(album).add(song);
        }
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        Playlist playlist = new Playlist(title);

        playlists.add(playlist);

        if(!playlistSongMap.containsKey(playlist)){
            playlistSongMap.put(playlist,new ArrayList<>());
        }
        for(Song song : songs){
            if(song.getLength() == length){
                playlistSongMap.get(playlist).add(song);
            }
        }

        User user = null;
        for(User user1: users){
            if(user1.getMobile().equals(mobile)){
                user = user1;
            }
        }

        if(user != null){
            creatorPlaylistMap.put(user,playlist);

            if(!playlistListenerMap.containsKey(playlist)){
                playlistListenerMap.put(playlist,new ArrayList<>());
            }
            playlistListenerMap.get(playlist).add(user);
        }

      return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist playlist = new Playlist(title);

        playlists.add(playlist);

        if(!playlistSongMap.containsKey(playlist)){
            playlistSongMap.put(playlist,new ArrayList<>());
        }

        for(String songTitle : songTitles){
            for(Song song : songs){
                if(songTitle.equals(song.getTitle())){
                    playlistSongMap.get(playlist).add(song);
                }
            }
        }

        User user = null;
        for(User user1: users){
            if(user1.getMobile().equals(mobile)){
                user = user1;
            }
        }

        if(user != null){
            creatorPlaylistMap.put(user,playlist);

            if(!playlistListenerMap.containsKey(playlist)){
                playlistListenerMap.put(playlist,new ArrayList<>());
            }
            playlistListenerMap.get(playlist).add(user);
        }

        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User user = null;
        for(User user1 : users){
            if(user1.getMobile().equals(mobile)){
                user = user1;
            }
        }

        Playlist pl = null;
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(playlistTitle)){
                pl = playlist;
            }
        }

        playlistListenerMap.get(pl).add(user);
        return pl;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        Song song = null;
        for(Song song1 : songs){
            if(song1.getTitle().equals(songTitle)){
                song = song1;
            }
        }
        song.setLikes(song.getLikes()+1);

        Album album = null;

        for (Album album1 : albumSongMap.keySet()){
            for(Song song1 : albumSongMap.get(album1)){
                if(song1.getTitle().equals(songTitle)){
                    album = album1;
                }
            }
        }

        Artist artist = null;

        for(Artist artist1 : artistAlbumMap.keySet()){
            for(Album album1 : artistAlbumMap.get(artist1)){
                if(album1.getTitle().equals(album.getTitle())){
                    artist = artist1;
                }
            }
        }

        Artist artistLiked = null;
        for(Artist artist1 : artists){
            if(artist1.getName().equals(artist.getName())){
                artistLiked = artist1;
            }
        }
        artistLiked.setLikes(artistLiked.getLikes()+1);
        return song;
    }

    public String mostPopularArtist() {

        Artist maxArtist = null;
        for(Artist artist : artists){
            if(maxArtist == null){
                maxArtist = artist;
            }else if(artist.getLikes() > maxArtist.getLikes()){
                maxArtist = artist;
            }
        }
        return maxArtist.getName();
    }

    public String mostPopularSong() {
        Song maxSong = null;
        for(Song song : songs){
            if(maxSong == null){
                maxSong = song;
            }else if(song.getLikes() > maxSong.getLikes()){
                maxSong = song;
            }
        }
        return maxSong.getTitle();
    }


}

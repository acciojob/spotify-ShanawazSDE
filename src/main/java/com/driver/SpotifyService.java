package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public User createUser(String name, String mobile){
        return spotifyRepository.createUser(name, mobile);

    }

    public Artist createArtist(String name) {
        return spotifyRepository.createArtist(name);

    }

    public Album createAlbum(String title, String artistName) {
        boolean artistExists = false;
        for(Artist artist : spotifyRepository.artists){
            if(artist.getName().equals(artistName))artistExists = true;
        }

        if(!artistExists){
           spotifyRepository.createArtist(artistName);
        }
       return spotifyRepository.createAlbum(title,artistName);
    }

    public Song createSong(String title, String albumName, int length) throws Exception {
        Album album = null;
        for (Album alb : spotifyRepository.albums){
            if(alb.getTitle().equals(albumName))
                album = alb;
        }
        if(album == null)throw new AlbumDoesNotExistException("Album does not exist");

       return spotifyRepository.createSong(title, albumName, length);

    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        boolean userExists = false;
        for(User user : spotifyRepository.users){
            if(user.getMobile().equals(mobile)){
                userExists = true;
            }
        }
        if(!userExists)throw new UserDoesNotExistException("User does not exist");
        return spotifyRepository.createPlaylistOnLength(mobile, title, length);
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        boolean userExists = false;
        for(User user : spotifyRepository.users){
            if(user.getMobile().equals(mobile)){
                userExists = true;
            }
        }
        if(!userExists)throw new UserDoesNotExistException("User does not exist");
        return spotifyRepository.createPlaylistOnName(mobile, title, songTitles);
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User user = null;
        for(User user1 : spotifyRepository.users){
            if(user1.getMobile().equals(mobile)){
                user = user1;
            }
        }
        if(user == null)throw new UserDoesNotExistException("User does not exist");

        Playlist pl = null;
        for(Playlist playlist : spotifyRepository.playlists){
            if(playlist.getTitle().equals(playlistTitle)){
                pl = playlist;
            }
        }
        if(pl == null) throw new PlaylistDoesNotExistsException("Playlist does not exist");

        for(User listener : spotifyRepository.playlistListenerMap.get(pl)){
            if(listener.getMobile().equals(mobile))return pl;
        }

        return spotifyRepository.findPlaylist(mobile, playlistTitle);


    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user = null;
        for(User user1 : spotifyRepository.users){
            if(user1.getMobile().equals(mobile)){
                user = user1;
            }
        }
        if(user == null)throw new UserDoesNotExistException("User does not exist");

        Song song = null;
        for(Song song1 : spotifyRepository.songs){
            if(song1.getTitle().equals(songTitle)){
                song = song1;
            }
        }
        if(song == null)throw new SongDoesNotExistException("Song does not exist");

        if(spotifyRepository.songLikeMap.containsKey(song)){
            for(User user1 : spotifyRepository.songLikeMap.get(song)){
                if(user1.getMobile().equals(user.getMobile())){
                    return song;
                }
            }
        }
         spotifyRepository.likeSong(mobile, songTitle);
        spotifyRepository.userLikedTheSong(user,song);
        return song;
    }

    public String mostPopularArtist() {
      return spotifyRepository.mostPopularArtist();
    }

    public String mostPopularSong() {
        return spotifyRepository.mostPopularSong();
    }
}

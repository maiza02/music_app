package com.music.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.music.entity.Album;
import com.music.entity.Artist;
import com.music.entity.Song;
import com.music.repository.AlbumRepository;
import com.music.repository.ArtistRepository;
import com.music.repository.SongRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    @PostMapping("/add/song")
    public String addSong(@RequestParam String songname,
                          @RequestParam String duration,
                          @RequestParam String songURL,
                          @RequestParam String albumname,
                          @RequestParam String artistName) {

        logger.info("Entering addSong() with song='{}', album='{}', artist='{}'", songname, albumname, artistName);

        // Get or create artist
        Artist artist = artistRepository.findByArtistnameIgnoreCase(artistName);
        if (artist == null) {
            artist = new Artist();
            artist.setArtistName(artistName);
            artistRepository.save(artist);
            logger.debug("Created new artist '{}'", artistName);
        }

        // Get or create album
        Album album = albumRepository.findByAlbumnameIgnoreCaseAndArtist(albumname, artist);
        if (album == null) {
            album = new Album();
            album.setAlbumname(albumname);
            album.setArtist(artist);
            album.setYearpublished("Unknown");
            album.setCoverimage("default.jpg");
            album.setDescription("No description.");
            albumRepository.save(album);
            logger.debug("Created new album '{}'", albumname);
        }

        // Check if the song already exists
        Song existing = songRepository.findBySongnameIgnoreCaseAndAlbum(songname, album);
        if (existing != null) {
            logger.warn("Song '{}' already exists in album '{}'", songname, albumname);
            return "redirect:/add/song?error=exists";
        }

        // Create and save song
        Song song = new Song();
        song.setSongname(songname);
        song.setDuration(duration);
        song.setSongURL(songURL);
        song.setAlbum(album);
        song.setArtist(artist);
        songRepository.save(song);

        logger.info("Successfully added song '{}' to album '{}'", songname, albumname);
        return "redirect:/add/song?songAdded=true";
    }
}

package com.music.entity;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "songs")
public class Song {

    private static final Logger logger = LoggerFactory.getLogger(Song.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String songname;

    private String duration;

    private String songURL;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        logger.info("Setting Song ID: {}", id);
        this.id = id;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        logger.info("Setting Song Name: {}", songname);
        this.songname = songname;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        logger.info("Setting Song Duration: {}", duration);
        this.duration = duration;
    }

    public String getSongURL() {
        return songURL;
    }

    public void setSongURL(String songURL) {
        logger.info("Setting Song URL: {}", songURL);
        this.songURL = songURL;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        logger.info("Assigning Artist '{}' to Song '{}'", 
                    (artist != null ? artist.getArtistName() : "null"), songname);
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        logger.info("Assigning Album '{}' to Song '{}'", 
                    (album != null ? album.getAlbumname() : "null"), songname);
        this.album = album;
    }
}
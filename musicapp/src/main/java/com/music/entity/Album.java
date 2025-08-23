package com.music.entity;

import java.util.List;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "albums")
public class Album {

    private static final Logger logger = LoggerFactory.getLogger(Album.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String albumname;

    private String yearpublished;

    private String coverimage;

    private String description;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Song> songs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        logger.info("Setting Album ID: {}", id);
        this.id = id;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        logger.info("Setting Album Name: {}", albumname);
        this.albumname = albumname;
    }

    public String getYearpublished() {
        return yearpublished;
    }

    public void setYearpublished(String yearpublished) {
        logger.info("Setting Album Year Published: {}", yearpublished);
        this.yearpublished = yearpublished;
    }

    public String getCoverimage() {
        return coverimage;
    }

    public void setCoverimage(String coverimage) {
        logger.info("Setting Album Cover Image: {}", coverimage);
        this.coverimage = coverimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        logger.info("Setting Album Description: {}", description);
        this.description = description;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        logger.info("Setting Album Artist: {}", artist.getArtistName());
        this.artist = artist;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        logger.info("Setting Songs List with {} songs", (songs != null ? songs.size() : 0));
        this.songs = songs;
    }
}

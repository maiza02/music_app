package com.music.entity;

import jakarta.persistence.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "artists")
public class Artist {

    private static final Logger logger = LoggerFactory.getLogger(Artist.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String artistname;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Album> albums;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        logger.info("Setting Artist ID: {}", id);
        this.id = id;
    }

    public String getArtistName() {
        return artistname;
    }

    public void setArtistName(String artistname) {
        logger.info("Setting Artist Name: {}", artistname);
        this.artistname = artistname;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        logger.info("Setting Albums List for Artist '{}' with {} albums", 
                    artistname, (albums != null ? albums.size() : 0));
        this.albums = albums;
    }
}
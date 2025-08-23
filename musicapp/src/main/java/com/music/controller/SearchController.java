package com.music.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/searching")
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    @GetMapping("/search")
    public String generalSearch(@RequestParam("query") String query, Model model) {
        logger.info("Entering generalSearch() with query='{}'", query);

        List<Album> albums = albumRepository.findByAlbumnameContainingIgnoreCase(query);
        List<Artist> artists = artistRepository.findByArtistnameContainingIgnoreCase(query);
        List<Song> songs = songRepository.findBySongnameContainingIgnoreCase(query);

        logger.debug("Found {} albums, {} artists, {} songs for query '{}'", 
                     albums.size(), artists.size(), songs.size(), query);

        model.addAttribute("albums", albums);
        model.addAttribute("artists", artists);
        model.addAttribute("songs", songs);
        model.addAttribute("query", query);

        String view = "search";
        logger.info("Exiting generalSearch() -> {}", view);
        return view;
    }
}

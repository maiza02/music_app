package com.music.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.music.entity.Album;
import com.music.entity.Artist;
import com.music.entity.Song;
import com.music.repository.AlbumRepository;
import com.music.repository.ArtistRepository;
import com.music.repository.SongRepository;

@Controller
@RequestMapping("/albums")
public class AlbumController {
	
	private static final Logger logger = LoggerFactory.getLogger(AlbumController.class);

	@Autowired
	private AlbumRepository albumRepository;
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private SongRepository songRepository;
	
	@GetMapping
	public String listAlbums(Model model) {
		logger.info("Entering listAlbums()");
		model.addAttribute("albums", albumRepository.findAll());
		logger.info("Exiting listAlbums() - albums loaded");
		return "albums";
	}
	
	@GetMapping("/add/album")
	public String showAddAlbumForm(Model model) {
	    logger.info("Entering showAddAlbumForm()");
	    model.addAttribute("album", new Album());
	    model.addAttribute("artists", artistRepository.findAll());
	    logger.info("Exiting showAddAlbumForm() - form prepared");
	    return "addalbum"; 
	}

	@PostMapping("/add")
	public String addAlbum(@RequestParam String albumname,
	                       @RequestParam String yearpublished,
	                       @RequestParam String coverimage,
	                       @RequestParam String description,
	                       @RequestParam String artistName) {
	    logger.info("Entering addAlbum() with albumname={}, artistName={}", albumname, artistName);

	    Artist artist = artistRepository.findByArtistnameIgnoreCase(artistName);
	    if (artist == null) {
	        logger.info("Artist '{}' not found, creating new artist", artistName);
	        artist = new Artist();
	        artist.setArtistName(artistName);
	        artistRepository.save(artist);
	        logger.info("New artist '{}' saved", artistName);
	    }

	    Album album = new Album();
	    album.setAlbumname(albumname);
	    album.setYearpublished(yearpublished);
	    album.setCoverimage(coverimage);
	    album.setDescription(description);
	    album.setArtist(artist);

	    albumRepository.save(album);
	    logger.info("Album '{}' saved successfully", albumname);

	    logger.info("Exiting addAlbum()");
	    return "redirect:/albums";
	}

    @GetMapping("/edit/{id}")
    public String showEditAlbumForm(@PathVariable Long id, Model model) {
        logger.info("Entering showEditAlbumForm() with id={}", id);
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid album ID={}", id);
                    return new IllegalArgumentException("Invalid album ID");
                });

        model.addAttribute("album", album);
        model.addAttribute("artists", artistRepository.findAll());
        logger.info("Exiting showEditAlbumForm() - album loaded");
        return "albums/edit";
    }

    @PostMapping("/update/{id}")
    public String updateAlbum(@PathVariable Long id, @ModelAttribute Album albumDetails, @RequestParam Long artistId) {
        logger.info("Entering updateAlbum() with id={}", id);

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid album ID={}", id);
                    return new IllegalArgumentException("Invalid album ID");
                });

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> {
                    logger.error("Invalid artist ID={}", artistId);
                    return new IllegalArgumentException("Invalid artist ID");
                });

        album.setAlbumname(albumDetails.getAlbumname());
        album.setDescription(albumDetails.getDescription());
        album.setCoverimage(albumDetails.getCoverimage());
        album.setYearpublished(albumDetails.getYearpublished());
        album.setArtist(artist);

        albumRepository.save(album);
        logger.info("Album with id={} updated successfully", id);

        logger.info("Exiting updateAlbum()");
        return "redirect:/albums";
    }

    @PostMapping("/delete/{id}")
    public String deleteAlbum(@PathVariable Long id) {
        logger.info("Entering deleteAlbum() with id={}", id);
        albumRepository.deleteById(id);
        logger.info("Album with id={} deleted successfully", id);
        logger.info("Exiting deleteAlbum()");
        return "redirect:/albums";
    }
    
    @GetMapping("/album/{id}")
    public String viewAlbumDetails(@PathVariable Long id, Model model) {
        logger.info("Entering viewAlbumDetails() with id={}", id);
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        
        if (optionalAlbum.isPresent()) {
            Album album = optionalAlbum.get();
            List<Song> songs = songRepository.findByAlbum(album); 
            model.addAttribute("album", album);
            model.addAttribute("songs", songs); 
            logger.info("Exiting viewAlbumDetails() - album and songs loaded");
            return "albumdetails";
        } else {
            logger.warn("Album with id={} not found, redirecting to /albums", id);
            return "redirect:/albums";
        }
    }

}

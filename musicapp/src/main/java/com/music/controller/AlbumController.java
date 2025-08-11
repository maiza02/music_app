package com.music.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.music.entity.Album;
import com.music.entity.Artist;
import com.music.entity.Song;
import com.music.repository.AlbumRepository;
import com.music.repository.ArtistRepository;
import com.music.repository.SongRepository;

@Controller
@RequestMapping("/albums")
public class AlbumController {

	@Autowired
	private AlbumRepository albumRepository;
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private SongRepository songRepository;
	
	@GetMapping
	public String listAlbums(Model model) {
		model.addAttribute("albums", albumRepository.findAll());
		return "albums";
	}
	
	@GetMapping("/add/album")
	public String showAddAlbumForm(Model model) {
	    model.addAttribute("album", new Album());
	    model.addAttribute("artists", artistRepository.findAll());
	    return "addalbum"; 
	}

	
	@PostMapping("/add")
	public String addAlbum(@RequestParam String albumname,
	                       @RequestParam String yearpublished,
	                       @RequestParam String coverimage,
	                       @RequestParam String description,
	                       @RequestParam String artistName) {

	    // Check if artist exists
	    Artist artist = artistRepository.findByArtistnameIgnoreCase(artistName);

	    // If artist doesn't exist, create new one
	    if (artist == null) {
	        artist = new Artist();
	        artist.setArtistName(artistName);
	       // artist.setArtistInfo("No info yet"); 
	        artistRepository.save(artist);
	    }

	    Album album = new Album();
	    album.setAlbumname(albumname);
	    album.setYearpublished(yearpublished);
	    album.setCoverimage(coverimage);
	    album.setDescription(description);
	    album.setArtist(artist);

	    albumRepository.save(album);

	    return "redirect:/albums";
	}

	
    // Show form to edit album
    @GetMapping("/edit/{id}")
    public String showEditAlbumForm(@PathVariable Long id, Model model) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid album ID"));

        model.addAttribute("album", album);
        model.addAttribute("artists", artistRepository.findAll());
        return "albums/edit";
    }

    // Handle form submission to update album
    @PostMapping("/update/{id}")
    public String updateAlbum(@PathVariable Long id, @ModelAttribute Album albumDetails, @RequestParam Long artistId) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid album ID"));

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid artist ID"));

        album.setAlbumname(albumDetails.getAlbumname());
        album.setDescription(albumDetails.getDescription());
        album.setCoverimage(albumDetails.getCoverimage());
        album.setYearpublished(albumDetails.getYearpublished());
        album.setArtist(artist);

        albumRepository.save(album);
        return "redirect:/albums";
    }

    // Delete album
    @PostMapping("/delete/{id}")
    public String deleteAlbum(@PathVariable Long id) {
        albumRepository.deleteById(id);
        return "redirect:/albums";
    }
    
    @GetMapping("/album/{id}")
    public String viewAlbumDetails(@PathVariable Long id, Model model) {
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        
        if (optionalAlbum.isPresent()) {
            Album album = optionalAlbum.get();
            List<Song> songs = songRepository.findByAlbum(album); 
            model.addAttribute("album", album);
            model.addAttribute("songs", songs); 
            return "albumdetails";
        } else {
            return "redirect:/albums";
        }
    }

}

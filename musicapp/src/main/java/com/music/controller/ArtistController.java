package com.music.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.music.entity.Artist;
import com.music.repository.ArtistRepository;

@Controller
@RequestMapping("/artist")
public class ArtistController {

    private static final Logger logger = LoggerFactory.getLogger(ArtistController.class);

    @Autowired
    private ArtistRepository artistRepository;
    
    @GetMapping("/search")
    @ResponseBody
    public List<Map<String, Object>> searchArtists(@RequestParam String term) {
        logger.info("Entering searchArtists() with search term: {}", term);

        List<Artist> artists = artistRepository.findByArtistnameContainingIgnoreCase(term);
        List<Map<String, Object>> results = artists.stream().map(artist -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", artist.getId());
            map.put("name", artist.getArtistName());
            return map;
        }).collect(Collectors.toList());

        logger.info("Exiting searchArtists() - found {} artists", results.size());
        return results;
    }
}

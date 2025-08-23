package com.music.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class homecontroller {

    private static final Logger logger = LoggerFactory.getLogger(homecontroller.class);

    @GetMapping("/")
    public String redirectToAlbums() {
        logger.info("Entering redirectToAlbums()");
        String view = "redirect:/albums";
        logger.info("Exiting redirectToAlbums() -> {}", view);
        return view;
    }
    
    @GetMapping("/add/song")
    public String addsongs() {
        logger.info("Entering addsongs()");
        String view = "addsong";
        logger.info("Exiting addsongs() -> {}", view);
        return view;
    }
}

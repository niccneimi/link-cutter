package com.linkcutter.linkcutter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkcutter.linkcutter.entity.LinkRequest;
import com.linkcutter.linkcutter.entity.ShortLinkResponse;
import com.linkcutter.linkcutter.service.LinkService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LinkController {

    private LinkService linkService;

    @PostMapping("/link")
    public ResponseEntity<ShortLinkResponse> createShortLink(@RequestBody LinkRequest entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(linkService.createShortLink(entity.getLink()));
    }
    
}

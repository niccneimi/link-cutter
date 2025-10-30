package com.linkcutter.linkcutter.service;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.linkcutter.linkcutter.exception.ShortLinkNotFoundException;
import com.linkcutter.linkcutter.model.Link;
import com.linkcutter.linkcutter.repository.LinkRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RedirectService {

    private LinkRepository linkRepository;

    public HttpHeaders redirect(String shortLinkId) {
        Link link = linkRepository.findByShortLinkId(shortLinkId).orElseThrow(() -> new ShortLinkNotFoundException("Not found link with id: " + shortLinkId));
        String originalLink = link.getOriginalLink();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalLink));
        return headers;
    }
}

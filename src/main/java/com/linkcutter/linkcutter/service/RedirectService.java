package com.linkcutter.linkcutter.service;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.linkcutter.linkcutter.entity.Link;
import com.linkcutter.linkcutter.exception.ShortLinkNotFoundException;
import com.linkcutter.linkcutter.repository.LinkRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RedirectService {

    private static final Logger logger = LoggerFactory.getLogger(RedirectService.class);

    private LinkRepository linkRepository;

    public HttpHeaders redirect(String shortLinkId) {
        logger.info("Redirect requested for shortLinkId: {}", shortLinkId);

        Link link = linkRepository.findByShortLinkId(shortLinkId).orElseThrow(() -> {
            logger.warn("ShortLinkId not found: {}", shortLinkId);
            return new ShortLinkNotFoundException("Not found link with id: " + shortLinkId);
        });

        String originalLink = link.getOriginalLink();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalLink));
        logger.info("Redirecting to URL: {}", originalLink);
        return headers;
    }
}

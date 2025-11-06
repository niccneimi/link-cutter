package com.linkcutter.linkcutter.service;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.linkcutter.linkcutter.config.LinkServiceProperties;
import com.linkcutter.linkcutter.entity.Link;
import com.linkcutter.linkcutter.exception.InsufficientSystemStateException;
import com.linkcutter.linkcutter.model.ShortLinkResponse;
import com.linkcutter.linkcutter.repository.LinkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LinkService {

    private static final Logger logger = LoggerFactory.getLogger(LinkService.class);

    private static final String DATA_FOR_RANDOM_STRING = "abcdefghijklmnopqrstuvwxyz1234567890";
    private static final SecureRandom random = new SecureRandom();

    private final LinkRepository linkRepository;
    private final LinkServiceProperties properties;

    public ShortLinkResponse createShortLink(String link) {
        logger.info("Creating short link for: {}", link);

        if (!isValidLink(link)) {
            logger.warn("Invalid link attemted: {}", link);
            throw new IllegalArgumentException("Not valid link: " + link);
        }

        String shortLinkId;

        Optional<Link> existingLinkOptional = linkRepository.findByOriginalLink(link);
        if (existingLinkOptional.isPresent()) {

            Link existingLink = existingLinkOptional.get();
            shortLinkId = existingLink.getShortLinkId();

            if (shortLinkId.length() != properties.shortLinkIdLength()) {
                logger.warn("Existing shortLinkId length ({}) does not match desired length ({}). Generating new one.",
                        existingLink.getShortLinkId().length(), properties.shortLinkIdLength());

                shortLinkId = generateRandomUniqueString(properties.shortLinkIdLength());
                existingLink.setShortLinkId(shortLinkId);
                linkRepository.save(existingLink);
                
                logger.info("Change shortLinkId for {} to {}", link, shortLinkId);
            } else {
                logger.info("Link already exists. Using shortLinkId: {}", shortLinkId);
            }

        } else {
            
            shortLinkId = generateRandomUniqueString(properties.shortLinkIdLength());
            Link newLink = new Link(link, shortLinkId);
            linkRepository.save(newLink);
            logger.info("Created new short link with id: {}", shortLinkId);
        }

        String shortLink = "https://" + properties.appDomainName() + "/" + shortLinkId;
        logger.debug("Generated short link: {}", shortLink);

        return ShortLinkResponse.builder()
                .shortLink(shortLink)
                .build();
    }

    private String generateRandomUniqueString(int stringLength) {
        int attempts = 0;
        String generatedString;

        do {
            generatedString = generateRandomString(stringLength);
            attempts++;
            if (attempts > 20) {
                logger.warn("Reached max attempts (20) to generate unique link ID");
                throw new InsufficientSystemStateException(
                        "System state insufficient to generate new links due improper configuration");
            }
        } while (linkRepository.existsByShortLinkId(generatedString));

        return generatedString;
    }

    private String generateRandomString(int stringLength) {
        if (stringLength < 1) {
            logger.error("SHORT_LINK_ID_LENGTH bad initialization");
            throw new InsufficientSystemStateException(
                    "System state insufficient to generate new links due improper configuration");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            int randCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char charAt = DATA_FOR_RANDOM_STRING.charAt(randCharAt);
            sb.append(charAt);
        }
        return sb.toString();
    }

    private boolean isValidLink(String link) {
        try {
            URI uri = new URI(link);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
